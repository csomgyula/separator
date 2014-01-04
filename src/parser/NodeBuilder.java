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
        // -- ------------------------------------------------------------------------------------------------------
        // -- create tokenizer
        // -- ------------------------------------------------------------------------------------------------------
        Tokenizer tokenizer = new Tokenizer();
        tokenizer.setTags(tags);
        tokenizer.setText(text);

        // -- ------------------------------------------------------------------------------------------------------
        // -- init node automata
        // -- ------------------------------------------------------------------------------------------------------
        // TODO: SOS, EOS could be moved to shared code
        Tag rootTag = tags.get(0);
        Token sosToken = rootTag.getTokenPairs().get(0).getOpen();
        Token.Instance sosTokenInstance = new Token.Instance();
        sosTokenInstance.setStartPosition(0);
        sosTokenInstance.setEndPosition(0);
        sosTokenInstance.setToken(sosToken);

        NodeAutomata nodeAutomata = new NodeAutomata(sosTokenInstance);

        // -- ------------------------------------------------------------------------------------------------------
        // -- main loop
        // -- ------------------------------------------------------------------------------------------------------
        Token.Instance nextTokenInstance;
        int textPosition = 0;

        // do while text doesn't reach end
        while (textPosition < text.length()) { // TODO: cache text length
            nextTokenInstance = tokenizer.next();
            nodeAutomata.handleNextToken(nextTokenInstance);
            textPosition = nextTokenInstance.getEndPosition();
        }

        // -- ------------------------------------------------------------------------------------------------------
        // -- closing
        // -- ------------------------------------------------------------------------------------------------------
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
     * Implements the main logic.
     */
    protected class NodeAutomata{

        private Tag leafTag;

        private Node cursorNode, root;

        private Token.Instance prevTokenInstance, nextTokenInstance;

        /**
         * Initializes the automata, especially creates the root node.
         */
        public NodeAutomata(Token.Instance sosTokenInstance){
            // create root node
            Node root = Node.root();
            root.setTag(tags.get(0));
            root.setOpen(sosTokenInstance);
            this.root = root;
            cursorNode = root;

            // init prev token
            prevTokenInstance = sosTokenInstance;
        }

        public Node getRoot() {
            return root;
        }

        // -- --------------------------------------------------------------------------------------------------------------
        // -- Handles
        // -- --------------------------------------------------------------------------------------------------------------

        public void handleNextToken(Token.Instance nextTokenInstance) {
            this.nextTokenInstance = nextTokenInstance;

            switch (nextTokenInstance.getToken().getKind()){
                case SIMPLE_BLOCK_OPEN: handleSimpleBlockOpen(); break;
                case EOS: handleEOS(); break;
                default: handleDefaultCase(); break;

            }

            this.prevTokenInstance = nextTokenInstance;
        }

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

        // -- --------------------------------------------------------------------------------------------------------------
        // -- Utils
        // -- --------------------------------------------------------------------------------------------------------------

        /**
         * Creates a new content node and adds it to the cursor.
         * Cursor points to the new node.
         */
        protected void newContentNode(boolean maybeEmpty) {
            int startPosition = prevTokenInstance.getEndPosition(), endPosition = nextTokenInstance.getStartPosition();

            boolean empty = startPosition == endPosition;

            if (maybeEmpty || !empty) {
                Node node = new Node();

                node.setTag(getLeafTag());
                node.setKind(Node.Kind.LEAF);

                node.setContent(text.substring(startPosition, endPosition));

                node.setOpen(prevTokenInstance);
                node.setClose(nextTokenInstance);

                addNode(node);

                cursorNode = node;
            }
        }

        /**
         * Closes the token associated with the input token and everything below it.
         * Node cursor is moved to the deepest open tag.
         */
        protected void closeNodeAssociatedWithToken() {
            Tag tokenTag = nextTokenInstance.getToken().getTag();

            while (cursorNode.getTag() != tokenTag) {
                cursorNode.setClose(nextTokenInstance);
                cursorNode = cursorNode.getParent();
            }

            cursorNode.setClose(nextTokenInstance);
            cursorNode = cursorNode.getParent();
        }

        /**
         * Create a simple block (not SOS) and add it to the cursors.
         * Cursor is positioned to the new block node.
         *
         * FIXME: Block is created as a branch node, which is not good if the block is the lowest in the tag hierarchy.
         */
        protected void openSimpleBlockNode() {
            Node node = new Node();

            node.setTag(nextTokenInstance.getToken().getTag());

            node.setKind(Node.Kind.BRANCH); // FIXME: handle special case when simple block is the deepest tag

            node.setOpen(prevTokenInstance);

            addNode(node);

            cursorNode = node;
        }

        /**
         * Add node to the cursor. Possibly create its parents.
         */
        protected void addNode(Node node) {
            // if the node cursor's tag is the same or the parent then add directly
            if (node.getTag() == cursorNode.getTag()) {
                cursorNode.getParent().addChild(node);
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
                    parentNode.setOpen(nextTokenInstance);
                    parentNode.addChild(childNode);
                    childNode = parentNode;
                }
                cursorNode.addChild(childNode);
            }
        }

        /**
         * Returns the leaf tag. Cached.
         */
        protected Tag getLeafTag() {
            if (leafTag==null){
                leafTag = tags.get(tags.size() - 1);
            }
            return leafTag;
        }
    }

}
