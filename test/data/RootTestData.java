package separator.test.data;

import separator.Tag;

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
        Tag root = new Tag();
        root.setName("root");
        root.setKind(Tag.Kind.ROOT);
        root.setIndex(0);
        getTags().add(root);
    }

    @Override
    protected void buildTexts() {
        getTexts().add("");
        getTexts().add("árvíztükörfúrógép");
    }
}
