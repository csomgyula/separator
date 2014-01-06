package separator.test.data;

import separator.Tag;

import java.util.regex.Pattern;

/**
 * Simple tokenizer test
 */
public class SimpleTestData extends AbstractTestData {
    @Override
    protected void buildParent() {
        setParent(new RootTestData());
    }

    @Override
    protected void buildTags() {
        Tag tag;

        tag = new Tag();
        tag.setName("field");
        tag.setKind(Tag.Kind.SIMPLE);
        tag.setClose(Pattern.compile(";"));

        tag.setParent(getTags().get(getTags().size() - 1));
        tag.setIndex(getTags().size());
        getTags().add(tag);
    }

    @Override
    protected void buildTexts() {
        getTexts().add("field;");
        getTexts().add("field1;field2;field3");
        getTexts().add(";");
        getTexts().add(";;;");
        getTexts().add(";field2;;");
    }
}
