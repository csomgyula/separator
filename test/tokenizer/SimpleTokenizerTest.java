package separator.test.tokenizer;

import separator.test.data.AbstractTestData;
import separator.test.data.SimpleTestData;

/**
 * Simple tokenizer test
 */
public class SimpleTokenizerTest extends AbstractTokenizerTest{

    public static void main(String[] args) {
        SimpleTokenizerTest test = new SimpleTokenizerTest();
        test.test();
    }

    @Override
    public AbstractTestData getData() {
        return new SimpleTestData();
    }
}
