package separator.test.tokenizer;

import separator.test.data.AbstractTestData;
import separator.test.data.RootTestData;

/**
 * Simple tokenizer test
 */
public class RootTokenizerTest extends AbstractTokenizerTest{

    public static void main(String[] args) {
        RootTokenizerTest test = new RootTokenizerTest();
        test.test();
    }

    @Override
    public AbstractTestData getData() {
        return new RootTestData();
    }
}
