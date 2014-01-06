package separator.test.data;

import separator.Tag;

import java.util.regex.Pattern;

/**
 * nesting simple blocks and simple separators
 */
public class SimpleBlockNestingTestData extends AbstractTestData {
    @Override
    protected void buildParent() {
        setParent(new RootTestData());
    }

    @Override
    protected void buildTags() {
        Tag tag;

        tag = new Tag();
        tag.setName("block");
        tag.setKind(Tag.Kind.SIMPLE_BLOCK);
        tag.setOpen(  Pattern.compile("\\{") );
        tag.setClose( Pattern.compile("\\}") );
        tag.setParent(getTags().get(getTags().size() - 1));
        tag.setIndex(getTags().size());
        getTags().add(tag);

        tag = new Tag();
        tag.setName("expr");
        tag.setKind(Tag.Kind.SIMPLE);
        tag.setClose(Pattern.compile(";"));
        tag.setParent(getTags().get(getTags().size() - 1));
        tag.setIndex(getTags().size());
        getTags().add(tag);

        tag = new Tag();
        tag.setName("paren");
        tag.setKind(Tag.Kind.SIMPLE_BLOCK);
        tag.setOpen(  Pattern.compile("\\(") );
        tag.setClose( Pattern.compile("\\)") );
        tag.setParent(getTags().get(getTags().size() - 1));
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
