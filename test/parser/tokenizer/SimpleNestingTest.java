package separator.test.parser.tokenizer;

import separator.Tag2;

import java.util.regex.Pattern;

/**
 * Simple tokenizer test
 */
public class SimpleNestingTest extends AbstractTokenizerTest{

    public static void main(String[] args) {
        SimpleNestingTest test = new SimpleNestingTest();
        test.test();
    }

    @Override
    protected void buildParent() {
        setParent(new SimpleTest());
    }

    @Override
    protected void buildTags() {
        Tag2 tag;

        tag = new Tag2();
        tag.setName("part");
        tag.setKind(Tag2.Kind.SIMPLE);
        tag.setClose(Pattern.compile(" +"));

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
