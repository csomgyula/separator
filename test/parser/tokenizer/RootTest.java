package separator.test.parser.tokenizer;

import separator.Tag2;

/**
 * Simple tokenizer test
 */
public class RootTest extends AbstractTokenizerTest{

    public static void main(String[] args) {
        RootTest test = new RootTest();
        test.test();
    }

    @Override
    protected void buildParent() {
        // do nothing
    }

    @Override
    protected void buildTags() {
        Tag2 root = new Tag2();
        root.setName("root");
        root.setKind(Tag2.Kind.ROOT);
        root.setIndex(0);
        getTags().add(root);
    }

    @Override
    protected void buildTexts() {
        getTexts().add("");
        getTexts().add("árvíztükörfúrógép");
    }
}
