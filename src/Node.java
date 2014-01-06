package separator;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node within the tagged tree that is the output of separation.
 */
public class Node {
    private Tag tag;
    private Kind kind;
    private Node parent;
    private String content;
    private List<Node> children;
    private Token open, close;


    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildren() {
        if (children == null) {
            children = new ArrayList<Node>();
        }
        return children;
    }

    public void addChild(Node node) {
        getChildren().add(node);
        node.setParent(this);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Returns the token at the start of the node.
     */
    public Token getOpen() {
        return open;
    }

    public void setOpen(Token open) {
        this.open = open;
    }

    /**
     * Returns the token at the end of the node.
     */
    public Token getClose() {
        return close;
    }

    public void setClose(Token close) {
        this.close = close;
    }

    /**
     * Returns the text position at the start of the node (inclusive).
     * <p/>
     * That is this node represents substring(startPosition, endPosition).
     */
    public int getStartPosition() {
        Token open = getOpen();
        if (open != null) {
            return open.getEnd();
        } else {
            return -1;
        }
    }

    /**
     * Returns the text position at the end of the node (exclusive).
     */
    public int getEndPosition() {
        Token close = getClose();
        if (close != null) {
            return close.getStart();
        } else {
            return -1;
        }
    }

    public boolean isA(String tag) {
        return getTag().getName().equals(tag);
    }

    public boolean isA(Kind kind) {
        return getKind() == kind;
    }

    public enum Kind {
        ROOT,
        BRANCH,
        LEAF
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        toString(0, this, stringBuilder, 0);
        return stringBuilder.toString();
    }

    protected void toString(int index, Node node, StringBuilder stringBuilder, int ident) {
        // ident
        for (int i = 0; i < ident; i++) {
            stringBuilder.append("  ");
        }

        // source position
        stringBuilder.append("@(");
        stringBuilder.append(node.getStartPosition());
        stringBuilder.append(",");
        stringBuilder.append(node.getEndPosition());
        stringBuilder.append(") ");

        // tag name and index
        if (node.getTag() != null) {
            stringBuilder.append(node.getTag().getName());
            // string.append("@");
            // string.append(node.getKind());
            if (node.getTag().getKind() != Tag.Kind.ROOT) {
                stringBuilder.append("(");
                stringBuilder.append(index);
                stringBuilder.append(")");
            }
        }
        else{
            stringBuilder.append("untagged");
        }
        stringBuilder.append(": ");

        // content
        if (node.getContent() != null) {
            stringBuilder.append(node.getContent());
        }

        // head-body separator
        stringBuilder.append("\n");

        // children
        index = 0;
        for (Node child : node.getChildren()) {
            // defensive coding
            if( child != node ){
                toString(index++, child, stringBuilder, ident + 1);
            }
            // should not happen but...
            else{
                for (int i = 0; i < ident+2; i++) {
                    stringBuilder.append("  ");
                }
                stringBuilder.append("this (infinite cycle)");
            }
        }
    }
}
