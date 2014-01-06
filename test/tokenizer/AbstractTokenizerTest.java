package separator.test.tokenizer;

import separator.Token2;
import separator.Tokenizer2;
import separator.test.data.AbstractTestData;

/**
 *
 */
public abstract class AbstractTokenizerTest {
    public void test() {
        AbstractTestData data = getData();
        for (String text : data.getTexts()) {
            Tokenizer2 tokenizer = new Tokenizer2(data.getTags(), text);
            System.out.println("test: " + data.getName());
            System.out.println("text: \"" + text + "\" (len: " + text.length() + ")");
            System.out.println("tokens: ");

            Token2 token;
            while ((token = tokenizer.next()) != null) {
                System.out.println("\t" + token);
            }

            System.out.println();
        }
    }

    public abstract AbstractTestData getData();
}
