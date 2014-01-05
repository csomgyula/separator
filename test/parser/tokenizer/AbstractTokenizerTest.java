package separator.test.parser.tokenizer;

import separator.Tag2;
import separator.parser.Token2;
import separator.parser.Tokenizer2;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class AbstractTokenizerTest {

    private AbstractTokenizerTest parent;
    private List<Tag2> tags;
    private List<String> texts;
    private String name;

    public void test() {
        for (String text : getTexts()) {
            Tokenizer2 tokenizer = new Tokenizer2(getTags(), text);
            System.out.println("test: " + getName());
            System.out.println("text: \"" + text + "\" (len: " + text.length() + ")");
            System.out.println("tokens: ");

            Token2 token;
            while ((token = tokenizer.next()) != null) {
                System.out.println("\t" + token);
            }

            System.out.println();
        }
    }

    public AbstractTokenizerTest getParent() {
        if (parent == null){
            buildParent();
        }
        return parent;
    }

    public void setParent(AbstractTokenizerTest parent){
        this.parent = parent;
    }

    protected abstract void buildParent();

    public List<Tag2> getTags() {
        if (tags == null) {
            tags = new ArrayList<Tag2>();
            if (getParent() != null) {
                tags.addAll(getParent().getTags());
            }
            buildTags();
        }
        return tags;
    }

    protected abstract void buildTags();

    public List<String> getTexts() {
        if (texts == null) {
            texts = new ArrayList<String>();
            buildTexts();
            if (getParent() != null) {
                texts.addAll(getParent().getTexts());
            }
        }
        return texts;
    }

    protected abstract void buildTexts();

    protected String getName(){
         if (name == null){
             String nameBuild;
             String[]  namesBuild;

             namesBuild = this.getClass().getName().split("\\.");
             nameBuild = namesBuild[namesBuild.length - 1];

             namesBuild = nameBuild.split("Test");
             nameBuild = namesBuild[0];

             name = nameBuild.toLowerCase();
         }
        return name;
    }
}
