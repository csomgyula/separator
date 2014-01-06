package separator.test.nodebuilder;

import separator.test.data.AbstractTestData;
import separator.test.data.SimpleTestData;

/**
 * Simple tokenizer test
 */
public class SimpleBuilderTest extends AbstractNodeBuilderTest{

    public static void main(String[] args) {
        SimpleBuilderTest test = new SimpleBuilderTest();
        test.test();
    }

    @Override
    public AbstractTestData getData() {
        return new SimpleTestData();
    }
}
