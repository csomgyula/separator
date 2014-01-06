package separator.test.nodebuilder;

import separator.test.data.AbstractTestData;
import separator.test.data.RootTestData;

/**
 * Simple tokenizer test
 */
public class RootBuilderTest extends AbstractNodeBuilderTest{

    public static void main(String[] args) {
        RootBuilderTest test = new RootBuilderTest();
        test.test();
    }

    @Override
    public AbstractTestData getData() {
        return new RootTestData();
    }
}
