package separator.parser;

import separator.Node;

import java.util.List;

/**
 * Prototype implementation of the node builder. Under construction.
 */
public class NodeBuilder {
    private List<Tag> tags;

    private String text;

    private int textPosition;

    /**
     * The text to build nodes from.
     */
    public void setText(String text) {
        this.text = text;
        textPosition = 0;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Node build(){
        // create tokenizer

        // read next token

        // add node according to token

        return null;
    }
}
