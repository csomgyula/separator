package separator;

import separator.parser.Parser;

/**
 * The main class.
 */
public class Separator {
    /**
     * Separate the given text according to the given rules.
     */
    public Node separate(String rules, String text){
        return new Parser(rules).parse(text);
    }
}
