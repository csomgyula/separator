package separator.parser;

import java.util.List;

/**
 * Prototype implementation of the tokenizer. Under construction.
 */
public class Tokenizer {
    private List<Tag> tags;

    private String text;

    private int textPosition;

    /**
     * The text to tokenize.
     */
    public void setText(String text) {
        this.text = text;
        textPosition = 0;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    /**
     * The prototype implementation tries to match each token and selects the match with the lowest index.
     * Slow but works.
     */
    public Token.Instance next(){
        return null;
    }
}
