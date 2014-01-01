package separator;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node within the tagged tree that is the output of separation.
 */
public class Node {
    private String tag;
    private Kind kind;
    private Node parent;
    private String content;
    private List<Node> children;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public enum Kind{
        ROOT,
        BRANCH,
        LEAF
    }
}
