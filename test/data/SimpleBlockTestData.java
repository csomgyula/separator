package separator.test.data;

import separator.Tag;

import java.util.regex.Pattern;

/**
 * simple block
 */
public class SimpleBlockTestData extends AbstractTestData {
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
    }

    @Override
    protected void buildTexts() {
        getTexts().add("ext1{block1}ext2{block2}");
        getTexts().add("ext1{}ext2");
        getTexts().add("{block}");
    }
}
