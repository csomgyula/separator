package separator;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tokenizer.
 */
public class Tokenizer {
    // -- config
    private int textLength;

    public Tokenizer(List<Tag> tags, String text) {
        textLength = text.length();
        textPosition = -1;
        tagHierarchyMatcher = new TagHierarchyMatcher(tags, text);
    }

    // -- states
    private int textPosition;
    private TagHierarchyMatcher tagHierarchyMatcher;

    // -- inputs/outputs

    /**
     * Returns the next token if any. Especially first it returns the special Start-of-Source (SOS) token and last it
     * returns the special End-of-Source (EOS) token.
     */
    public Token next() {
        if (tagHierarchyMatcher.find(textPosition)) {
            Token token = tagHierarchyMatcher.getToken();
            textPosition = token.getEnd();
            return token;
        } else {
            return null;
        }
    }

    /**
     * Represents a tag hierarchy matcher.
     */
    protected static class TagHierarchyMatcher {
        // -- config
        private TagMatcher[] tagMatchers;
        private int textLength;

        public TagHierarchyMatcher(List<Tag> tags, String text) {
            // init tag matchers
            tagMatchers = new TagMatcher[tags.size()];
            int index = 0;
            for (Tag tag : tags) {
                tagMatchers[index++] = new TagMatcher(tag, text);
            }
            openMatchers = new ArrayDeque<TagMatcher>();
            textLength = text.length();
        }

        // -- states
        private Deque<TagMatcher> openMatchers;
        private Token token;

        protected TagMatcher getDeepestOpenMatcher() {
            return openMatchers.peek();
        }

        // -- inputs

        /**
         * Loop through tags including and below the deepest openMatcher tag and try to match.
         * Select the best match into token.
         * Push onto / pop from openMatcher tags if necessary.
         * Return true if there was a match otherwise false.
         */
        public boolean find(int pos) {
            // init best matcher
            TagMatcher bestMatcher = null;
            int bestStart = textLength, bestEnd = pos; // TODO: cache text.length()

            // init loop start
            int loopStart = 0;

            // init deepest open matcher
            TagMatcher deepestOpenMatcher = getDeepestOpenMatcher();
            if (deepestOpenMatcher != null) {
                loopStart = deepestOpenMatcher.getTag().getIndex();
            }

            // loop through tags and select the best match
            TagMatcher matcher;
            int start, end;
            for (int i = loopStart; i < tagMatchers.length; i++) { // TODO: cache length
                matcher = tagMatchers[i];
                if (matcher.find(pos)) {
                    start = matcher.getToken().getStart();
                    end = matcher.getToken().getEnd();
                    if (start < bestStart || (start == bestStart && end > bestEnd)) {
                        bestMatcher = matcher;
                        bestStart = start;
                        bestEnd = end;
                    }
                }
            }

            if (bestMatcher != null) {

                // notify the matcher - this must be invoked before close / openMatcher
                bestMatcher.matched();

                // close / openMatcher tag if necessary
                if (bestMatcher == deepestOpenMatcher && !bestMatcher.isOpen()) {
                    closeDeepestOpenMatcher();
                } else if (bestMatcher.isOpen()) {
                    openMatcher(bestMatcher);
                }

                // set best match
                token = bestMatcher.getToken();
            }

            // return true if there was a match otherwise false.
            return bestMatcher != null;
        }

        // -- internals
        protected void openMatcher(TagMatcher tagMatcher) {
            openMatchers.push(tagMatcher);
        }

        protected void closeDeepestOpenMatcher() {
            openMatchers.pop();
        }

        // -- outputs

        /**
         * Returns the match associated with the previous find.
         */
        public Token getToken() {
            return token;
        }
    }

    /**
     * Represents a matcher associated with a tag.
     */
    protected static class TagMatcher {
        // -- config
        private Tag tag;
        private Matcher open, close;
        private int textLength;

        public TagMatcher(Tag tag, String text) {
            this.tag = tag;

            open = buildMatcher(tag.getOpen(), text);
            close = buildMatcher(tag.getClose(), text);

            if (tag.isA(Tag.Kind.SIMPLE)) {
                tokenKind = Token.Kind.CLOSE;
            } else {
                tokenKind = Token.Kind.OPEN;
            }

            textLength = text.length();
        }

        protected Matcher buildMatcher(Pattern pattern, String text){
            return pattern != null ? pattern.matcher(text) : null;
        }

        /**
         * Returns the tag associated with the matcher.
         */
        public Tag getTag() {
            return tag;
        }

        // -- state
        private Token.Kind tokenKind;
        private Token token;

        /**
         * Returns the matcher associated with the current kind.
         */
        protected Matcher getMatcher() {
            Matcher matcher = null;
            switch (tokenKind) {
                case OPEN:
                    matcher = open;
                    break;
                case CLOSE:
                    matcher = close;
                    break;
            }
            return matcher;
        }

        // -- inputs

        /**
         * Tries to finds the separator. Returns true if there's a match, false otherwise. The match is saved in the
         * token property.
         */
        public boolean find(int pos) {
            Matcher matcher = getMatcher();
            // normal text pos
            if (0 <= pos && pos < textLength) {
                if (matcher != null && matcher.find(pos)) {
                    token = new Token();
                    token.setTag(tag);
                    token.setKind(tokenKind); // no need to set special kind since it defaults to not special
                    token.setStart(matcher.start());
                    token.setEnd(matcher.end());
                    return true;
                } else if (getTag().isRoot()) {   // FIXME: avoid DRY
                    token = new Token();
                    token.setTag(tag);
                    token.setKind(tokenKind);
                    token.setSpecialKind(Token.SpecialKind.EOS);
                    token.setStart(textLength);
                    token.setEnd(textLength + 1);
                    return true;
                } else {
                    token = null;
                    return false;
                }
            }
            // special text pos (SOS, EOS)
            else if (getTag().isRoot() && (pos == -1 || pos == textLength)) { // FIXME: make it faster
                token = new Token();
                token.setTag(tag);
                token.setKind(tokenKind);
                token.setSpecialKind(pos == -1 ? Token.SpecialKind.SOS : Token.SpecialKind.EOS);
                token.setStart(pos);
                token.setEnd(pos + 1);
                return true;
            } else {
                token = null;
                return false;
            }
        }

        /**
         * Notify this matcher that its match is selected as a best match. Could be called after a find() only.
         */
        public void matched() {
            if (tag.isBlock()) {
                switch (tokenKind) {
                    case OPEN:
                        tokenKind = Token.Kind.CLOSE;
                        break;
                    case CLOSE:
                        tokenKind = Token.Kind.OPEN;
                        break;
                }
            }
        }

        // -- outputs

        /**
         * Returns the match associated with the previous find.
         */
        public Token getToken() {
            return token;
        }

        /**
         * Returns true if it is a block and state is open, ie. the current token kind is close.
         */
        public boolean isOpen() {
            return tag.isBlock() && tokenKind == Token.Kind.CLOSE;
        }
    }
}
