package separator.test.data;

import separator.Tag;

import java.util.regex.Pattern;

/**
 * Simple tokenizer test
 */
public class SimpleNestingTestData extends AbstractTestData {
    @Override
    protected void buildParent() {
        setParent(new SimpleTestData());
    }

    @Override
    protected void buildTags() {
        Tag tag;

        tag = new Tag();
        tag.setName("part");
        tag.setKind(Tag.Kind.SIMPLE);
        tag.setClose(Pattern.compile(" +"));

        tag.setParent(getTags().get(getTags().size() - 1));
        tag.setIndex(getTags().size());
        getTags().add(tag);
    }

    @Override
    protected void buildTexts() {
        getTexts().add("part1 part2;part3 part4;part5 part6 part7");
        getTexts().add(" part1 part2 ; part3 part4 ;");
        getTexts().add(" ");
        getTexts().add(" ; ; ");
        getTexts().add(" ; part ; ; ");
    }
}
