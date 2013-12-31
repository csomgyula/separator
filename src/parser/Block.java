package separator.parser;

import separator.Node;

/**
 * Represents a block of nodes associated with the same tags. Necessary to handle recursive blocks.
 */
public class Block {
    public static Block root(Node rootNode) {
        Block block = new Block();
        block.setTag(Tag.root());
        block.setBottomNode(rootNode);
        return block;
    }

    Block parent;
    Tag tag;
    Node bottomNode;

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Tag getTag() {
        return tag;
    }

    public Node getBottomNode() {
        return bottomNode;
    }

    public void setBottomNode(Node bottomNode) {
        this.bottomNode = bottomNode;
    }

    public void setParent(Block parent) {
        this.parent = parent;
    }

    public Block getParent() {
        return parent;
    }

    protected boolean isRoot() {
        return tag.getType() == Tag.Type.ROOT;
    }
}