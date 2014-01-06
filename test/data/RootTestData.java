package separator.test.data;

import separator.Tag2;

/**
 * Simple tokenizer test
 */
public class RootTestData extends AbstractTestData {

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
