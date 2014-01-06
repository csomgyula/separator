package separator.test.nodebuilder;

import separator.test.data.AbstractTestData;
import separator.test.data.SimpleBlockTestData;

/**
 * simple block
 */
public class SimpleBlockBuilderTest extends AbstractNodeBuilderTest {
    public static void main(String[] args) {
        SimpleBlockBuilderTest test = new SimpleBlockBuilderTest();
        test.test();
    }

    @Override
    public AbstractTestData getData() {
        return new SimpleBlockTestData();
    }
}
