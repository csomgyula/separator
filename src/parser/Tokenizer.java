package separator.parser;

import separator.Tag;
import separator.Token;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Prototype implementation of the tokenizer.
 *
 * TODO: simple patterns should be handled as strings instead of regexps. For instance the ability to use "{{" instead  of "\\{\\{"
 */
public class Tokenizer {
    private List<Tag> tags;

    private String text;

    private int textPosition;

    private MatcherAutomata matcherAutomata;

    /**
     * Set the text to tokenize.
     */
    public void setText(String text) {
        this.text = text;
        textPosition = 0;
        matcherAutomata = null;
    }

    /**
     * Set the tokenization rules.
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
        matcherAutomata = null;
    }

    /**
     * The prototype implementation tries to match each token (pattern) and selects the match with the best index(es).
     * Slow but works.
     */
    public Token.Instance next() {
        MatcherAutomata matcherAutomata = getMatcherAutomata();

        // if tokenizer not finished
        if (!matcherAutomata.isFinished()){

            // get the best match if any
            Token.Instance tokenInstance = match();

            // if there was a match then move the text position to token's end position
            if (tokenInstance != null) {
                textPosition = tokenInstance.getEndPosition();
            }
            // else emit an end-of-source token unless there's a block open
            else if (matcherAutomata.getOpenCount() == 1) {
                Token.Pair rootTokenPair = tags.get(0).getTokenPairs().get(0);

                // close the root tag in the automata
                matcherAutomata.close(rootTokenPair);

                // emit EOS
                tokenInstance = new Token.Instance();
                tokenInstance.setToken(rootTokenPair.getClose());
                tokenInstance.setStartPosition(text.length()); // TODO: cache text length
                tokenInstance.setEndPosition(text.length()); // TODO: cache text length

                // move text position to the end
                textPosition = text.length(); // TODO: cache text length
            }
            // else throw exception
            else {
                throw new RuntimeException("A block is not closed"); // TODO: better error message
            }
            return tokenInstance;
        }
        // if tokenizer finished throw exception
        else{
            throw new RuntimeException("No more tokens");
        }
    }

    /**
     * The prototype implementation tries to match each token (pattern) and selects the match with the best index(es).
     * Slow but works.
     */
    // if there's more text then execute the following algorithm:
    //
    // 1. iterate through tag matchers
    //     1.1. iterate through the current tag's matchers
    //         1.1.1. match against the current matcher and if there is a match
    //             1.1.1.1. get the match positions
    //             1.1.1.2. compare with the current best one and if it is better then
    //                 1.1.1.2.1. replace the best match with this
    //                 1.1.1.2.2. if it is a block match then notify the finite automata, especially
    //                     1.1.1.2.2.1. if it is a block open then trigger open
    //                     1.1.1.2.2.2. if it is a block close then trigger close
    protected Token.Instance match() {
        // -- ----------------------------------------------------------------------------------------------------------
        // -- match
        // -- ----------------------------------------------------------------------------------------------------------
        MatcherAutomata.TokenMatcher bestTokenMatcher = null;

        int bestStart = text.length(), bestEnd = textPosition, bestTokenIndex = -1; // TODO: cache text length

        MatcherAutomata matcherAutomata = getMatcherAutomata();

        if (textPosition < text.length()) {
            int start, end;
            MatcherAutomata.TagMatchers tagMatchers = matcherAutomata.getTagMatchers();
            Matcher matcher;
            Token token;

            // 1. iterate through tag matchers
            for (List<MatcherAutomata.TokenMatcher> tagTokenMatchers : tagMatchers) {
                // 1.1. iterate through the current tag's matchers
                for (MatcherAutomata.TokenMatcher tokenMatcher : tagTokenMatchers) {
                    // 1.1.1. match against the current matcher and if there is a match
                    matcher = tokenMatcher.getMatcher();
                    if (matcher.find(textPosition)) {
                        token = tokenMatcher.getToken();

                        // 1.1.1.1. get the match positions
                        start = matcher.start();
                        end = matcher.end();

                        // 1.1.1.2. compare with the current best one and if it is better
                        if (start < bestStart || (start == bestStart && end > bestEnd)) {
                            //  1.1.1.2.1. replace the best match with this
                            bestStart = start;
                            bestEnd = end;
                            bestTokenMatcher = tokenMatcher;
                        }
                    }
                }
            }
        }

        if (bestTokenMatcher != null) {
            Token bestToken = bestTokenMatcher.getToken();

            //  1. if it is a block match then notify the finite automata
            if (bestToken.getTag().getKind() == Tag.Kind.SIMPLE_BLOCK) {

                // 1.1. if it is a block open then trigger open
                if (bestToken.getKind() == Token.Kind.SIMPLE_BLOCK_OPEN) {
                    matcherAutomata.open(bestTokenMatcher.getTokenPair());
                }
                // 1.2. if it is a block close then trigger close
                else if (bestToken.getKind() == Token.Kind.SIMPLE_BLOCK_CLOSE) {
                    matcherAutomata.close(bestTokenMatcher.getTokenPair());
                }
            }

            Token.Instance tokenInstance = new Token.Instance();
            tokenInstance.setToken(bestToken);
            tokenInstance.setStartPosition(bestStart);
            tokenInstance.setEndPosition(bestEnd);
            return tokenInstance;
        } else {
            return null;
        }
    }

    protected MatcherAutomata getMatcherAutomata() {
        if (matcherAutomata == null) {
            matcherAutomata = new MatcherAutomata();
        }
        return matcherAutomata;
    }

    /**
     * Finite automata of matchers. Must not be used in parallel.
     * <p/>
     * States
     * ------
     * <p/>
     * STATE := (T1, T2, ...) a stack of token pairs, including the root (SOS, EOS)
     * <p/>
     * impl.: STATE will be represented as a stack (ArrayDeque).
     * <p/>
     * The top most token is special, so lets give it a name for now:
     * <p/>
     * top := the topmost token pair
     * <p/>
     * Matchers
     * --------
     * The main function of STATE is to determine the matchers associated with it:
     * <p/>
     * matchers(STATE) := closeMatcher(top) + normalMatcersBelow(top)
     * unless top is null, in this special case matcher list is null as well
     * <p/>
     * that is:
     * * closeMatcher(top) - the matcher of the close token of the topmost token pair, plus
     * * normalMatchersBelow(top) - the normal tokens below the top most tag, where normal tokens is defined as:
     * <p/>
     * normalMatchersBelow(T) = the matchers of normal separators, and the matchers of the open tokens of a block.
     * <p/>
     * impl.: this will be represented as a list with a sliding start pointer.
     * <p/>
     * Initial and end states
     * ----------------------
     * <p/>
     * initial:  initially only the root token pair is added, so matchers represent EOS + normal matchers below root
     * end:      the stack is empty, no token pairs
     * <p/>
     * State changes
     * -------------
     * open(B):  when a new block B is opened => the associated token pair is pushed to the STATE stack
     * close(B): when a block B is closed => the associated token pair is popped from the STATE stack
     */
    protected class MatcherAutomata {

        private Deque<Token.Pair> state;
        private TagMatchers tagMatchers;
        private int openCount;

        public MatcherAutomata() {
            Token.Pair rootTokenPair = tags.get(0).getTokenPairs().get(0);
            open(rootTokenPair);
        }

        protected Deque<Token.Pair> getState() {
            if (state == null) {
                state = new ArrayDeque<Token.Pair>();
            }
            return state;
        }

        public void open(Token.Pair tokenPair) {
            getState().push(tokenPair);
            getTagMatchers().open(tokenPair.getTag());
            openCount++;
        }

        public void close(Token.Pair tokenPair) {
            getState().pop();
            getTagMatchers().close();
            openCount--;
        }

        public int getOpenCount() {
            return openCount;
        }

        public boolean isFinished() {
            return getState().isEmpty();
        }

        /**
         * Represents the matchers for and below a tag. Initially the tag is root, that is it represents EOS + normal
         * matchers below the root.
         */
        public TagMatchers getTagMatchers() {
            if (tagMatchers == null) {
                tagMatchers = new TagMatchers();
            }
            return tagMatchers;
        }

        /**
         * Represents the token matchers for and below a tag. That is the matchers of the close tokens of the tag +
         * the normal matchers below the tag.
         * The tag is handled as a pointer. Initially it points up to the root which is a special position - means
         * not yet started. The MatcherAutomata when initialized will then move it automatically to position 0.
         */
        protected class TagMatchers implements Iterable<List<TokenMatcher>> {
            private int tagPosition;
            private boolean[] openTags;

            private List<List<TokenMatcher>> normalMatchersList; // FIXME: normal matcher list could be an array
            private List<List<TokenMatcher>> closeMatchersList;  // FIXME: close matcher list could be an array

            public TagMatchers() {
                tagPosition = -1; // initially it is asscoiated with a special position.
                openTags = new boolean[tags.size()];

                normalMatchersList = new ArrayList<List<TokenMatcher>>();
                closeMatchersList = new ArrayList<List<TokenMatcher>>();

                for (Tag tag : tags) {
                    normalMatchersList.add(getNormalMatchers(tag, text));
                    closeMatchersList.add(getCloseMatchers(tag, text));
                }
            }

            /**
             * Move down in tag hierarchy to the given tag.
             */
            public void open(Tag tag) {
                while (tags.get(++tagPosition) != tag) {
                }
                openTags[tagPosition] = true;
            }

            /**
             * Move up in tag hierarchy.
             */
            public void close() {
                // mark tag not open
                openTags[tagPosition] = false;

                // move to the first open tag or 0  FIXME: is slow
                while (tagPosition > 0 && openTags[--tagPosition] == false) {
                }
                // move to -1 if there's no open tag
                if (tagPosition == 0 && openTags[0] == false){
                    tagPosition--;
                }
            }

            private Iterator<List<TokenMatcher>> iterator;

            @Override
            public Iterator<List<TokenMatcher>> iterator() {
                return new MatchersIterator();
            }

            /**
             * Iterator of matchers. Must not be used in parallel.
             */
            protected class MatchersIterator implements Iterator<List<TokenMatcher>> {
                private int iteratorPosition, endPosition;

                public MatchersIterator() {
                    this.iteratorPosition = tagPosition;
                    this.endPosition = tags.size();
                }

                public boolean hasNext() {
                    if (tagPosition == -1) {
                        return false;
                    } else {
                        return iteratorPosition < endPosition;
                    }
                }

                /**
                 * Return the next matchers.
                 * * If the cursor points to the tag, then return the matchers of the close tokens.
                 * * If the cursor points below the tag, then return the normal matchers of the tag.
                 *
                 * @return
                 */
                public List<TokenMatcher> next() {
                    if (hasNext()) {
                        if (iteratorPosition == tagPosition) {
                            return closeMatchersList.get(iteratorPosition++);
                        } else {
                            return normalMatchersList.get(iteratorPosition++);
                        }
                    } else {
                        throw new ArrayIndexOutOfBoundsException();
                    }
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }

            }

            /**
             * Return the normal matchers of a tag. That is matchers:
             * * of tokens for a simple separator.
             * * of open tokens for a simple block separator.
             */
            protected List<TokenMatcher> getNormalMatchers(Tag tag, String text) {
                Pattern pattern;
                List<TokenMatcher> tokenMatchers = new ArrayList<TokenMatcher>();
                TokenMatcher tokenMatcher;

                // build the matcher list
                if (tag.getKind() == Tag.Kind.SIMPLE) {
                    for (Token token : tag.getTokens()) {
                        pattern = token.getPattern();
                        if (pattern != null) {
                            tokenMatcher = new TokenMatcher();
                            tokenMatcher.setMatcher(pattern.matcher(text));
                            tokenMatcher.setToken(token);
                            tokenMatchers.add(tokenMatcher);
                        }
                    }
                } else if (tag.getKind() == Tag.Kind.SIMPLE_BLOCK) {
                    Token token;
                    for (Token.Pair tokenPair : tag.getTokenPairs()) {
                        token = tokenPair.getOpen();
                        pattern = token.getPattern();
                        if (pattern != null) {
                            tokenMatcher = new TokenMatcher();
                            tokenMatcher.setMatcher(pattern.matcher(text));
                            tokenMatcher.setTokenPair(tokenPair);
                            tokenMatcher.setToken(token);
                            tokenMatchers.add(tokenMatcher);
                        }
                    }
                }
                return tokenMatchers;
            }

            /**
             * Return the matchers of close tokens of block separators and empty matcher list for simple separators.
             */
            protected List<TokenMatcher> getCloseMatchers(Tag tag, String text) {
                Pattern pattern;
                List<TokenMatcher> tokenMatchers = new ArrayList<TokenMatcher>();
                // build the matcher list
                if (tag.getKind() == Tag.Kind.SIMPLE_BLOCK) {
                    TokenMatcher tokenMatcher;
                    Token token;
                    for (Token.Pair tokenPair : tag.getTokenPairs()) {
                        token = tokenPair.getClose();
                        pattern = token.getPattern();
                        if (pattern != null) {
                            tokenMatcher = new TokenMatcher();
                            tokenMatcher.setMatcher(pattern.matcher(text));
                            tokenMatcher.setTokenPair(tokenPair);
                            tokenMatcher.setToken(token);
                            tokenMatchers.add(tokenMatcher);
                        }
                    }
                }
                return tokenMatchers;
            }
        }

        /**
         * Represents a regexp matcher associated with a token.
         */
        protected class TokenMatcher {
            Matcher matcher;
            Token.Pair tokenPair;
            Token token;

            public Matcher getMatcher() {
                return matcher;
            }

            public void setMatcher(Matcher matcher) {
                this.matcher = matcher;
            }

            public Token.Pair getTokenPair() {
                return tokenPair;
            }

            public void setTokenPair(Token.Pair tokenPair) {
                this.tokenPair = tokenPair;
            }

            public Token getToken() {
                return token;
            }

            public void setToken(Token token) {
                this.token = token;
            }
        }
    }
}