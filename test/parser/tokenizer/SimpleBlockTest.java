package separator.test.parser.tokenizer;

import separator.Tag2;

import java.util.regex.Pattern;

/**
 * simple block
 */
public class SimpleBlockTest extends AbstractTokenizerTest {
    public static void main(String[] args) {
        SimpleBlockTest test = new SimpleBlockTest();
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
    }

    @Override
    protected void buildTexts() {
        getTexts().add("ext1{block1}ext2{block2}");
        getTexts().add("ext1{}ext2");
        getTexts().add("{block}");
    }
}
