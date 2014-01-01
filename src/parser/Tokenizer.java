package separator.parser;

import separator.Tag;
import separator.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Prototype implementation of the tokenizer.
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
        matcherMap = null;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
        matcherMap = null;
    }

    /**
     * The prototype implementation tries to match each token (pattern) and selects the match with the best index(es).
     * Slow but works.
     */
    public Token.Instance next() {
        Tag bestTag = null;

        Token.Instance tokenInstance = new Token.Instance();

        int bestStart = text.length(), bestEnd = textPosition, bestTokenIndex = -1; // TODO: cache text length

        if (textPosition < text.length()) {
            int start, end, matcherIndex;
            HashMap<Tag, List<Matcher>> matcherMap = getMatcherMap();
            for (Tag tag : tags) {
                matcherIndex = 0;
                for (Matcher matcher : matcherMap.get(tag)) {
                    // if there is a match for the current tag/matcher
                    if (matcher.find(textPosition)) {
                        // get the positions
                        start = matcher.start();
                        end = matcher.end();

                        // if this match is better than the current best one
                        if (start < bestStart || (start == bestStart && end > bestEnd)) {
                            // replace the best match with this
                            bestStart = start;
                            bestEnd = end;
                            bestTag = tag;
                            bestTokenIndex = matcherIndex;
                        }
                    }
                    matcherIndex++;
                }
            }
        }

        Token token;
        // if there was a match then emit the associated token
        if (bestTag != null) {
            token = bestTag.getTokens().get(bestTokenIndex);
            tokenInstance.setToken(token);
            tokenInstance.setStartPosition(bestStart);
            tokenInstance.setEndPosition(bestEnd);
            textPosition = bestEnd;
        }
        // else emit and end-of-source token
        else {
            token = tags.get(0).getTokenPairs().get(0).getClose();
            tokenInstance.setToken(token);
            tokenInstance.setStartPosition(text.length()); // TODO: cache text length
            tokenInstance.setEndPosition(text.length()); // TODO: cache text length
            textPosition = text.length(); // TODO: cache text length
        }
        return tokenInstance;
    }

    protected HashMap<Tag, List<Matcher>> matcherMap;

    // TODO: should be moved to the compiler?
    protected HashMap<Tag, List<Matcher>> getMatcherMap() {
        if (matcherMap == null) {
            matcherMap = new HashMap<Tag, List<Matcher>>();

            List<Matcher> matchers;
            Matcher matcher;
            String pattern;
            for (Tag tag : tags) {
                matchers = new ArrayList<Matcher>();
                // build the matcher list
                for (Token token : tag.getTokens()) {
                    pattern = token.getPattern();
                    if (pattern!=null){
                        matchers.add(Pattern.compile(pattern).matcher(text));
                    }
                }

                // add the matcher list to the map
                matcherMap.put(tag, matchers);
            }
        }
        return matcherMap;
    }
}
