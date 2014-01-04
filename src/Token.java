package separator;

import java.util.regex.Pattern;

/**
 * Represents a separator token.
 */
public class Token {
    private Tag tag;
    private Pattern pattern;
    private Kind kind;

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public boolean isA(Kind kind){
        return getKind() == kind;
    }

    /**
     * Token types
     */
    public enum Kind {
        SIMPLE,
        SIMPLE_BLOCK_OPEN, SIMPLE_BLOCK_CLOSE,
        // RECURSIVE_BLOCK_OPEN, RECURSIVE_BLOCK_CLOSE,
        // SKIP_OPEN, SKIP_ESCAPE, SKIP_CLOSE,
        // ESCAPE_OPEN, ESCAPE_ESCAPE, ESCAPE_CLOSE,
        // END,
        SOS, EOS;
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
    }
}
