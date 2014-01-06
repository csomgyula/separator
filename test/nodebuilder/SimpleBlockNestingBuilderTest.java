package separator.test.nodebuilder;

import separator.test.data.AbstractTestData;
import separator.test.data.SimpleBlockNestingTestData;
import separator.test.tokenizer.AbstractTokenizerTest;

/**
 * nesting simple blocks and simple separators
 */
public class SimpleBlockNestingBuilderTest extends AbstractNodeBuilderTest{
    public static void main(String[] args) {
        SimpleBlockNestingBuilderTest test = new SimpleBlockNestingBuilderTest();
        test.test();
    }

    @Override
    public AbstractTestData getData() {
        return new SimpleBlockNestingTestData();
    }
}
