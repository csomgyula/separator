package separator.parser;

import separator.Node;
import separator.Tag;
import separator.Token;

import java.util.List;

/**
 * Prototype implementation of the node builder.
 */
public class NodeBuilder {
    private List<Tag> tags;

    private String text;

    /**
     * Sets text to build nodes from.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Sets tags representing separation rules.
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    /**
     * Builds the tagged tree and returns its root.
     */
    public Node build() {
        // -- ----------------------------------------------------------------------------------------------------------
        // -- create tokenizer
        // -- ----------------------------------------------------------------------------------------------------------
        Tokenizer tokenizer = new Tokenizer();
        tokenizer.setTags(tags);
        tokenizer.setText(text);

        // -- ----------------------------------------------------------------------------------------------------------
        // -- init node automata
        // -- ----------------------------------------------------------------------------------------------------------
        // TODO: SOS, EOS could be moved to shared code
        Tag rootTag = tags.get(0);
        Token sosToken = rootTag.getTokenPairs().get(0).getOpen();
        Token.Instance sosTokenInstance = new Token.Instance();
        sosTokenInstance.setStartPosition(0);
        sosTokenInstance.setEndPosition(0);
        sosTokenInstance.setToken(sosToken);

        NodeAutomata nodeAutomata = new NodeAutomata(sosTokenInstance);

        // -- ----------------------------------------------------------------------------------------------------------
        // -- main loop
        // -- ----------------------------------------------------------------------------------------------------------
        Token.Instance nextTokenInstance;
        int textPosition = 0;

        // do while text doesn't reach end
        while (textPosition < text.length()) { // TODO: cache text length
            nextTokenInstance = tokenizer.next();
            nodeAutomata.handleNextToken(nextTokenInstance);
            textPosition = nextTokenInstance.getEndPosition();
        }

        // -- ----------------------------------------------------------------------------------------------------------
        // -- closing
        // -- ----------------------------------------------------------------------------------------------------------
        // it is possible that some nodes are not closed (if the end of the string is a separator then EOS is not read
        // hence root is not closed)
        // if root is closed then everything else is closed, hence it is enough to check whether root is closed

        Node root = nodeAutomata.getRoot();

        if (root.getClose() == null) {
            // read EOS
            nextTokenInstance = tokenizer.next();
            nodeAutomata.handleNextToken(nextTokenInstance);
        }

        return root;
    }

    /**
     * Main logic is implemented as a finite automata. Not thread safe, serial in nature.
     */
    protected class NodeAutomata {

        // -- ----------------------------------------------------------------------------------------------------------
        // -- States
        // -- ----------------------------------------------------------------------------------------------------------

        private Tag leafTag;

        private Node cursorNode, root;

        private Token.Instance prevTokenInstance, nextTokenInstance;

        /**
         * Returns the root node.
         */
        public Node getRoot() {
            return root;
        }

        /**
         * Returns the leaf tag. Cached.
         */
        protected Tag getLeafTag() {
            if (leafTag == null) {
                leafTag = tags.get(tags.size() - 1);
            }
            return leafTag;
        }

        // -- ----------------------------------------------------------------------------------------------------------
        // -- Init
        // -- ----------------------------------------------------------------------------------------------------------

        /**
         * Initializes the automata, especially creates the root node.
         */
        public NodeAutomata(Token.Instance sosTokenInstance) {
            // create root node
            Node root = Node.root();
            root.setTag(tags.get(0));
            root.setOpen(sosTokenInstance);
            this.root = root;
            cursorNode = root;

            // init prev token
            prevTokenInstance = sosTokenInstance;
        }

        // -- ----------------------------------------------------------------------------------------------------------
        // -- Input actions
        // -- ----------------------------------------------------------------------------------------------------------
        public void handleNextToken(Token.Instance nextTokenInstance) {
            this.nextTokenInstance = nextTokenInstance;

            switch (nextTokenInstance.getToken().getKind()) {
                case SIMPLE_BLOCK_OPEN:
                    handleSimpleBlockOpen();
                    break;
                case EOS:
                    handleEOS();
                    break;
                default:
                    handleDefaultCase();
                    break;

            }

            this.prevTokenInstance = nextTokenInstance;
        }

        // -- ----------------------------------------------------------------------------------------------------------
        // -- Internal actions
        // -- ----------------------------------------------------------------------------------------------------------
        protected void handleSimpleBlockOpen() {
            newContentNode(false);
            closeNodeAssociatedWithToken();
            openSimpleBlockNode();
        }

        private void handleEOS() {
            newContentNode(false);
            closeNodeAssociatedWithToken();
        }

        protected void handleDefaultCase() {
            newContentNode(true);
            closeNodeAssociatedWithToken();
        }

        /**
         * Creates a new content node and adds it to the cursor.
         * Cursor points to the new node.
         */
        protected void newContentNode(boolean maybeEmpty) {
            int startPosition = prevTokenInstance.getEndPosition(), endPosition = nextTokenInstance.getStartPosition();

            boolean empty = startPosition == endPosition;

            // FIXME: avoid empty nodes, something like this (but its to agressive):
            // maybeEmpty = maybeEmpty && nextTokenInstance.getToken().getTag() == getLeafTag();

            if (!empty || maybeEmpty ) {
                Node node;
                Token nextToken = nextTokenInstance.getToken();
                boolean newNode = false;

                // special case when root is the leaf
                if (getLeafTag().isA(Tag.Kind.ROOT)) {
                    node = root;
                }
                // special case when cursor is a leaf open block
                else if (cursorNode.isA(Node.Kind.LEAF) && cursorNode.getTag().isA(Tag.Kind.SIMPLE_BLOCK)) {
                    node = cursorNode;
                }
                // normal case when (1) root is not the leaf and (2) there's no open block as leaf
                else {
                    node = new Node();
                    newNode = true;

                    // special case when
                    // (1) the leaf is a block and
                    // (2) the next token is not the close token associated with the leaf tag
                    if (getLeafTag().isA(Tag.Kind.SIMPLE_BLOCK) &&
                            !(nextToken.isA(Token.Kind.SIMPLE_BLOCK_CLOSE) && nextToken.getTag() == getLeafTag())) {
                        node.setTag(getLeafTag().getBlockExt());
                    }
                    // normal case
                    else {
                        node.setTag(getLeafTag());
                    }

                    node.setKind(Node.Kind.LEAF);
                    node.setOpen(prevTokenInstance);
                }

                node.setContent(text.substring(startPosition, endPosition));
                node.setClose(nextTokenInstance);

                if (newNode) {
                    addNode(node);
                }

                cursorNode = node;
            }
        }

        /**
         * Closes the token associated with the input token and everything below it.
         * Node cursor is moved to the deepest open tag.
         */
        protected void closeNodeAssociatedWithToken() {
            Tag tokenTag = nextTokenInstance.getToken().getTag();

            // close only if there's any open node below the token's tag
            if (tokenTag.getIndex() <= cursorNode.getTag().getIndex()) {

                // close nodes below the token's tag
                while (cursorNode.getTag().getIndex() != tokenTag.getIndex()) {
                    cursorNode.setClose(nextTokenInstance);
                    cursorNode = cursorNode.getParent();
                }

                // close node associated with the token's tag
                cursorNode.setClose(nextTokenInstance);
                cursorNode = cursorNode.getParent();
            }
        }

        /**
         * Create a simple block (not SOS) and add it to the cursors.
         * Cursor is positioned to the new block node..
         */
        protected void openSimpleBlockNode() {
            Node node = new Node();

            node.setTag(nextTokenInstance.getToken().getTag());

            // node kind depends on whether the block is a leaf or not
            Node.Kind nodeKind = nextTokenInstance.getToken().getTag() != leafTag ? Node.Kind.BRANCH : Node.Kind.LEAF;
            node.setKind(nodeKind);

            node.setOpen(nextTokenInstance);

            addNode(node);

            cursorNode = node;
        }

        /**
         * Add node to the cursor. Possibly create its parents.
         */
        protected void addNode(Node node) {
            // if the node cursor's tag is the same or the parent then add directly
            if (node.getTag() == cursorNode.getTag()) {
                // normal case when root is not leaf
                if (node.getTag().getKind() != Tag.Kind.ROOT) {
                    cursorNode.getParent().addChild(node);
                }
            } else if (node.getTag().getParent() == cursorNode.getTag()) {
                cursorNode.addChild(node);
            }
            // if the node cursor is higher in the hierarchy (grandpa or higher) create necessary parent nodes
            else {
                Node childNode = node, parentNode;
                while (childNode.getTag().getParent() != cursorNode.getTag()) {
                    parentNode = new Node();
                    parentNode.setTag(childNode.getTag().getParent());
                    parentNode.setKind(Node.Kind.BRANCH);
                    parentNode.setOpen(node.getOpen());
                    parentNode.addChild(childNode);
                    childNode = parentNode;
                }
                cursorNode.addChild(childNode);
            }
        }
    }
}
