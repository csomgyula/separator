package separator.test.nodebuilder;

import separator.test.data.AbstractTestData;
import separator.test.data.SimpleNestingTestData;

/**
 * Simple tokenizer test
 */
public class SimpleNestingBuilderTest extends AbstractNodeBuilderTest{

    public static void main(String[] args) {
        SimpleNestingBuilderTest test = new SimpleNestingBuilderTest();
        test.test();
    }

    @Override
    public AbstractTestData getData() {
        return new SimpleNestingTestData();
    }
}
