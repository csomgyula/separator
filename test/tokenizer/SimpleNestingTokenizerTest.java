package separator.test.tokenizer;

import separator.test.data.AbstractTestData;
import separator.test.data.SimpleNestingTestData;

/**
 * Simple tokenizer test
 */
public class SimpleNestingTokenizerTest extends AbstractTokenizerTest{

    public static void main(String[] args) {
        SimpleNestingTokenizerTest test = new SimpleNestingTokenizerTest();
        test.test();
    }

    @Override
    public AbstractTestData getData() {
        return new SimpleNestingTestData();
    }
}
