package separator.test.parser.tokenizer;

import separator.Tag2;

import java.util.regex.Pattern;

/**
 * nesting simple blocks and simple separators
 */
public class SimpleBlockNestingTest extends AbstractTokenizerTest{
    public static void main(String[] args) {
        SimpleBlockNestingTest test = new SimpleBlockNestingTest();
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
        tag.setName("block");
        tag.setKind(Tag2.Kind.SIMPLE_BLOCK);
        tag.setOpen(  Pattern.compile("\\{") );
        tag.setClose( Pattern.compile("\\}") );
        tag.setIndex(getTags().size());
        getTags().add(tag);

        tag = new Tag2();
        tag.setName("expr");
        tag.setKind(Tag2.Kind.SIMPLE);
        tag.setClose(Pattern.compile(";"));
        tag.setIndex(getTags().size());
        getTags().add(tag);

        tag = new Tag2();
        tag.setName("paren");
        tag.setKind(Tag2.Kind.SIMPLE_BLOCK);
        tag.setOpen(  Pattern.compile("\\(") );
        tag.setClose( Pattern.compile("\\)") );
        tag.setIndex(getTags().size());
        getTags().add(tag);
    }

    @Override
    protected void buildTexts() {
        getTexts().add("{ext1;ext2(paren1)(paren2);ext3(paren3)(paren4)}{(paren5)}");
        getTexts().add("ext1;ext2;(paren1);(paren2);ext3(paren3)");
        getTexts().add("(paren1)(paren2);(paren3)");
        getTexts().add("ext1;ext2");
    }
}
