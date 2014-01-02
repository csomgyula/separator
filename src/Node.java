package separator;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node within the tagged tree that is the output of separation.
 *
 * TODO: add source refs to nodes: open/close token, start/end index
 */
public class Node {
    private Tag tag;
    private Kind kind;
    private Node parent;
    private String content;
    private List<Node> children;
    private Token.Instance open, close;

    public static Node root(){
        Node node = new Node();
        node.setKind(Kind.ROOT);
        node.setTag(Tag.root());
        return node;
    }

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
        if (children==null){
            children = new ArrayList<Node>();
        }
        return children;
    }

    public void addChild(Node node){
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
    public Token.Instance getOpen() {
        return open;
    }

    public void setOpen(Token.Instance open) {
        this.open = open;
    }

    /**
     * Returns the token at the end of the node.
     */
    public Token.Instance getClose() {
        return close;
    }

    public void setClose(Token.Instance close) {
        this.close = close;
    }

    /**
     * Returns the text position at the start of the node (inclusive).
     *
     * That is this node represents substring(startPosition, endPosition).
     */
    public int getStartPosition(){
        Token.Instance open = getOpen();
        if (open != null){
            return open.getEndPosition();
        }
        else{
            return -1;
        }
    }

    /**
     * Returns the text position at the end of the node (exclusive).
     */
    public int getEndPosition(){
        Token.Instance close = getClose();
        if (close != null){
            return close.getStartPosition();
        }
        else{
            return -1;
        }
    }

    public enum Kind{
        ROOT,
        BRANCH,
        LEAF
    }
}
