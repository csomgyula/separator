package separator;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node within the tagged tree
 */
public class Node {
    private String tag;
    private String content;
    private Node parent;
    private List<Node> children;

    public Node(String tag){
       this.tag = tag;
    }

    public Node(Node parent, String tag){
        this.parent = parent;
        this.tag = tag;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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
}
