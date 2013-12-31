package separator.parser;

/**
 * Prototype implementation of the tokenizer.
 */
public class Tokenizer {
    private Tags tags;

    private String text;

    private int textPosition;

    /**
     * The text to tokenize.
     */
    public void setText(String text) {
        this.text = text;
        textPosition = 0;
    }

    public void setTags(Tags tags) {
        this.tags = tags;
    }

    /**
     * The prototype implementation tries to match each token and selects the match with the lowest index.
     * Slow but works.
     */
    public Token next(){
        return null;
    }
}
