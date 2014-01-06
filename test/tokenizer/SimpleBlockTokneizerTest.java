package separator.test.tokenizer;

import separator.test.data.AbstractTestData;
import separator.test.data.SimpleBlockTestData;

/**
 * simple block
 */
public class SimpleBlockTokneizerTest extends AbstractTokenizerTest {
    public static void main(String[] args) {
        SimpleBlockTokneizerTest test = new SimpleBlockTokneizerTest();
        test.test();
    }

    @Override
    public AbstractTestData getData() {
        return new SimpleBlockTestData();
    }
}
