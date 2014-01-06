package separator.test.data;

import separator.Tag2;

import java.util.ArrayList;
import java.util.List;

/**
 *  Test data.
 */
public abstract class AbstractTestData {

    private AbstractTestData parent;
    private List<Tag2> tags;
    private List<String> texts;
    private String name;

    public AbstractTestData getParent() {
        if (parent == null){
            buildParent();
        }
        return parent;
    }

    public void setParent(AbstractTestData parent){
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

    public String getName(){
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
