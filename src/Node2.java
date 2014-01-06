package separator;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node within the tagged tree that is the output of separation.
 *
 * TODO: add source refs to nodes: open/close token, start/end index
 */
public class Node2 {
    private Tag2 tag;
    private Kind kind;
    private Node2 parent;
    private String content;
    private List<Node2> children;
    private Token2 open, close;


    public Tag2 getTag() {
        return tag;
    }

    public void setTag(Tag2 tag) {
        this.tag = tag;
    }

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public Node2 getParent() {
        return parent;
    }

    public void setParent(Node2 parent) {
        this.parent = parent;
    }

    public List<Node2> getChildren() {
        if (children==null){
            children = new ArrayList<Node2>();
        }
        return children;
    }

    public void addChild(Node2 node){
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
    public Token2 getOpen() {
        return open;
    }

    public void setOpen(Token2 open) {
        this.open = open;
    }

    /**
     * Returns the token at the end of the node.
     */
    public Token2 getClose() {
        return close;
    }

    public void setClose(Token2 close) {
        this.close = close;
    }

    /**
     * Returns the text position at the start of the node (inclusive).
     *
     * That is this node represents substring(startPosition, endPosition).
     */
    public int getStartPosition(){
        Token2 open = getOpen();
        if (open != null){
            return open.getEnd();
        }
        else{
            return -1;
        }
    }

    /**
     * Returns the text position at the end of the node (exclusive).
     */
    public int getEndPosition(){
        Token2 close = getClose();
        if (close != null){
            return close.getStart();
        }
        else{
            return -1;
        }
    }

    public boolean isA(String tag){
        return getTag().getName().equals(tag);
    }

    public boolean isA(Kind kind){
        return getKind() == kind;
    }

    public enum Kind{
        ROOT,
        BRANCH,
        LEAF
    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        toString(0, this, stringBuilder, 0);
        return stringBuilder.toString();
    }

    protected void toString(int index, Node2 node, StringBuilder stringBuilder, int ident){
        // ident
        for (int i=0; i< ident; i++){
            stringBuilder.append("  ");
        }

        // source position
        stringBuilder.append("@(");
        stringBuilder.append(node.getStartPosition());
        stringBuilder.append(",");
        stringBuilder.append(node.getEndPosition());
        stringBuilder.append(") ");

        // tag name and index
        stringBuilder.append(node.getTag().getName());
        // string.append("@");
        // string.append(node.getKind());
        if (node.getTag().getKind() != Tag2.Kind.ROOT){
            stringBuilder.append("(");
            stringBuilder.append(index);
            stringBuilder.append(")");
        }
        stringBuilder.append(": ");

        // content
        if (node.getContent()!=null){
            stringBuilder.append(node.getContent());
        }

        // head-body separator
        stringBuilder.append("\n");

        // children
        index = 0;
        for (Node2 child : node.getChildren()){
            assert child != node;
            toString(index++, child, stringBuilder, ident+1);
        }
    }
}
