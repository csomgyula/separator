package separator.test.parser.tokenizer;

import separator.Tag2;
import separator.parser.Token2;
import separator.parser.Tokenizer2;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Simple tokenizer test
 */
public class SimpleTest extends AbstractTokenizerTest{

    public static void main(String[] args) {
        SimpleTest test = new SimpleTest();
        test.test();
    }

    @Override
    protected void buildParent() {
        setParent(new RootTest());
    }

    @Override
    protected void buildTags() {
        Tag2 tag;

        tag = new Tag2();
        tag.setName("field");
        tag.setKind(Tag2.Kind.SIMPLE);
        tag.setClose(Pattern.compile(";"));

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
