package separator.sample.simple;

import separator.Separator;

/**
 * Separators are regular expressions.
 */
public class Sentences {
    public static void main(String[] args){
        String rules = "sentence \\.|!|\\?";
        String text = "What is separator? Separator is an enchanced form of split! It can be used to parse simple structures.";
        Separator separator = new Separator(rules);

        System.out.println(separator.separate(text));
    }
}
