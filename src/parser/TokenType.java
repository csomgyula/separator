package separator.parser;

/**
 * Represents a separator token.
 */
public class TokenType {

    private Tag tag;
    private String pattern;
    private Type type;

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    /**
     * TokenType types
     */
    public enum Type{
        SIMPLE,
        BLOCK_OPEN, BLOCK_CLOSE,
        RECURSIVE_BLOCK_OPEN, RECURSIVE_BLOCK_CLOSE,
        SKIP_OPEN, SKIP_ESCAPE, SKIP_CLOSE,
        ESCAPE_OPEN, ESCAPE_ESCAPE, ESCAPE_CLOSE,
        END,
        EOS;
    }
}
