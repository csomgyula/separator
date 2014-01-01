package separator;

/**
 * Represents a separator token.
 */
public class Token {
    public static Token eos(){
        Token token = new Token();
        token.setTag(Tag.eos());
        token.setKind(Kind.EOS);
        return token;
    }

    private Tag tag;
    private String pattern;
    private Kind kind;

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

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public boolean isEOS(){
        return kind == Kind.EOS;
    }

    /**
     * Token types
     */
    public enum Kind {
        SIMPLE,
        // BLOCK_OPEN, BLOCK_CLOSE,
        // RECURSIVE_BLOCK_OPEN, RECURSIVE_BLOCK_CLOSE,
        // SKIP_OPEN, SKIP_ESCAPE, SKIP_CLOSE,
        // ESCAPE_OPEN, ESCAPE_ESCAPE, ESCAPE_CLOSE,
        // END,
        EOS;
    }


    /**
     * Represents a pair of tokens associated with a block.
     */
    public static class Pair {
        private Tag tag;
        private Token open, close;

        public Tag getTag() {
            return tag;
        }

        public void setTag(Tag tag) {
            this.tag = tag;
        }

        public Token getOpen() {
            return open;
        }

        public void setOpen(Token open) {
            this.open = open;
        }

        public Token getClose() {
            return close;
        }

        public void setClose(Token close) {
            this.close = close;
        }
    }

    /**
     * Represents a separator token found in the input string.
     * An instance has start position (inclusive) and end position (exclusive).
     */
    public static class Instance {
        private Token token;
        private int startPosition, endPosition;

        public Token getToken() {
            return token;
        }

        public void setToken(Token token) {
            this.token = token;
        }

        public int getStartPosition() {
            return startPosition;
        }

        public void setStartPosition(int startPosition) {
            this.startPosition = startPosition;
        }

        public int getEndPosition() {
            return endPosition;
        }

        public void setEndPosition(int endPosition) {
            this.endPosition = endPosition;
        }

        public boolean isEOS(){
            return token.isEOS();
        }
    }
}
