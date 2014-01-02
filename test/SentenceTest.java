package separator.test;

import separator.Node;
import separator.Separator;

/**
 * Separators are regular expressions.
 */
public class SentenceTest {
    public static void main(String[] args){
        String rules = "sentence \\.|!|\\?";
        String text = "Separator is an enchanced form of split. It can be used to parse simple structures.";
        Separator separator = new Separator();

        Node sentences = separator.separate(rules, text);

        NodeToString nodeToString = new NodeToString();
        System.out.println(nodeToString.toString(sentences));
    }
}
