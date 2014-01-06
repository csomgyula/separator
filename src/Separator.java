package separator;

import java.util.List;

/**
 * The main class.
 */
public class Separator {
    private List<Tag> tags;
    private String rules;

    public Separator(String rules){
        this.rules = rules;
        tags = new Compiler().compile(rules);
    }

    public String getRules() {
        return rules;
    }

    /**
     * Separate the given text according to the given rules.
     */
    public Node separate(String text){
        return new NodeBuilder(tags, text).build();
    }
}
