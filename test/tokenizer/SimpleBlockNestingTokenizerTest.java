package separator.test.tokenizer;

import separator.test.data.AbstractTestData;
import separator.test.data.SimpleBlockNestingTestData;

/**
 * nesting simple blocks and simple separators
 */
public class SimpleBlockNestingTokenizerTest extends AbstractTokenizerTest{
    public static void main(String[] args) {
        SimpleBlockNestingTokenizerTest test = new SimpleBlockNestingTokenizerTest();
        test.test();
    }

    @Override
    public AbstractTestData getData() {
        return new SimpleBlockNestingTestData();
    }
}
