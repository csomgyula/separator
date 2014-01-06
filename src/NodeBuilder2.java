package separator;

import java.util.List;

/**
 * Prototype implementation of the node builder.
 */
public class NodeBuilder2 {
    // -- config
    private List<Tag2> tags;

    private String text;

    private Tag2 leafTag;

    public NodeBuilder2(List<Tag2> tags, String text) {
        this.tags = tags;
        this.text = text;
        leafTag = tags.get(tags.size() - 1);
    }

    // -- states
    private int textPosition;

    private Node2 root, cursor;

    private Token2 prevToken, nextToken;

    /**
     * Sets content to the given node.
     */
    protected String getContent() {
        return text.substring(prevToken.getEnd(), nextToken.getStart());
    }

    // -- inputs/outputs

    /**
     * Builds the tagged tree and returns its root.
     */
    public Node2 build() {
        Tokenizer2 tokenizer = new Tokenizer2(tags, text);
        while ((nextToken = tokenizer.next()) != null) {
            handleNextToken();
        }
        return root;
    }

    // -- internals

    /**
     * Handles the next token.
     */
    protected void handleNextToken() {
        switch (nextToken.getTag().getKind()) {
            case ROOT:
                handleRoot();
                break;
            case SIMPLE:
                handleSimple();
                break;
            case SIMPLE_BLOCK:
                handleSimpleBlock();
                break;
        }
        textPosition = nextToken.getEnd();
        prevToken = nextToken;
    }

    /**
     * Handles a simple separator token.
     * <p/>
     * - handle new content
     * - handle node close / cursor
     */
    protected void handleSimple() {
        newContent();
        closeAppropriateNodes();
    }

    /**
     * Handles a simple block.
     */
    protected void handleSimpleBlock() {
        newContent();
        closeAppropriateNodes();
        if (nextToken.isOpen()) {
            openSimpleBlockNode();
        }
    }

    /**
     * Handles root token.
     */
    protected void handleRoot() {
        if (nextToken.isSOS()) {
            root = new Node2();
            root.setOpen(nextToken);
            root.setTag(tags.get(0));
            root.setKind(Node2.Kind.ROOT);
            cursor = root;
        } else {
            newContent();
            closeAppropriateNodes();
        }
    }

    /**
     * Handles new content.
     * <p/>
     * If there's no open leaf create a new leaf node and add it.
     * Point cursor to the content node.
     */
    protected void newContent() {
        // if no open leaf create new node
        if (cursor.getTag() != leafTag) {
            Tag2 tag = !leafTag.isA(Tag2.Kind.SIMPLE_BLOCK) ? leafTag : leafTag.getBlockExt();
            String content = getContent();

            if (tag == leafTag || (content != null && !content.equals("")) ) {
                Node2 node = new Node2();
                node.setKind(Node2.Kind.LEAF);
                // tag is leaf tag or leaf's block ext
                node.setTag(tag);
                node.setOpen(prevToken);
                node.setContent(getContent());
                addNode(node);
                cursor = node;
            }
        } else {
            cursor.setContent(getContent());
        }
    }

    /**
     * Closes nodes associated with the next token's tag and nodes below it.
     */
    protected void closeAppropriateNodes() {
        Tag2 tokenTag = nextToken.getTag();

        // close only if there's any open node not higher then the token's tag
        if (tokenTag.getIndex() <= cursor.getTag().getIndex()) {

            // close nodes below the token's tag
            while (cursor.getTag().getIndex() != tokenTag.getIndex()) {
                cursor.setClose(nextToken);
                cursor = cursor.getParent();
            }

            // close node associated with the token's tag
            cursor.setClose(nextToken);
            cursor = cursor.getParent();
        }
    }

    /**
     * Adds node to the cursor, possibly creating its parents.
     */
    protected void addNode(Node2 node) {
        // for the current separator types cursor must be higher in the tag hierarchy
        // TODO: note that this won't be true for recursive blocks
        assert node.getTag().getIndex() > cursor.getTag().getIndex();

        // if the cursor's tag is the node parent then add directly
        if (node.getTag().getParent() == cursor.getTag()) {
            cursor.addChild(node);
        }
        // if the node cursor is higher in the hierarchy (grandpa or higher) create necessary parent nodes
        else {
            Node2 childNode = node, parentNode;

            // move upto cursor's child, create parents add children
            while (childNode.getTag().getParent().getIndex() != cursor.getTag().getIndex()) {
                parentNode = new Node2();
                Tag2 tag = childNode.getTag().getParent();
                parentNode.setTag(!tag.isBlock() ? tag : tag.getBlockExt());
                parentNode.setKind(Node2.Kind.BRANCH);
                parentNode.setOpen(node.getOpen());
                parentNode.addChild(childNode);
                childNode = parentNode;
            }
            // cursor's child is not added by the previous loop, do it now
            cursor.addChild(childNode);
        }
    }

    /**
     * Create a simple block (not SOS) and add it to the cursors.
     * Cursor is positioned to the new block node..
     */
    protected void openSimpleBlockNode() {
        Node2 node = new Node2();

        node.setTag(nextToken.getTag());

        // node kind depends on whether the block is a leaf or not
        Node2.Kind nodeKind = nextToken.getTag() != leafTag ? Node2.Kind.BRANCH : Node2.Kind.LEAF;
        node.setKind(nodeKind);

        node.setOpen(nextToken);

        addNode(node);

        cursor = node;
    }
}