package separator.parser;

/**
 * Represents a pair of tokens associted with a block.
 */
public class TokenTypePair {
    private Tag tag;
    private TokenType open, close;

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public TokenType getOpen() {
        return open;
    }

    public void setOpen(TokenType open) {
        this.open = open;
    }

    public TokenType getClose() {
        return close;
    }

    public void setClose(TokenType close) {
        this.close = close;
    }
}
