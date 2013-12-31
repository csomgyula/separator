package separator;

import separator.parser.Parser;

/**
 * The main class.
 */
public class Separator {
    /**
     * Parses the given text according to the given rules.
     */
    public Node parse(String rules, String text){
        return new Parser(rules).parse(text);
    }
}
