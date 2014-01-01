package separator.parser;

import separator.Node;

import java.util.List;

/**
 * Prototype implementation of the parser.
 *
 * The prototype implementation delegates to NodeBuilder.
 *
 * Why is NodeBuilder different from Parser?
 * One reason is thread safety. It is the builder who encapsulates internal parsing state.
 * Since a new builder is created on each parsing session (@see #parse()), parsing is thread safe.
 */
public class Parser {
    private List<Tag> tags;

    /**
     * Compile the rules.
     */
    public Parser(String rules) {
        tags = new Compiler().compile(rules);
    }

    /**
     * Parses the given text and return the root node.
     *
     * The prototype implementation delegates to NodeBuilder.
     */
    public Node parse(String text) {
        NodeBuilder nodeBuilder = new NodeBuilder();
        nodeBuilder.setTags(tags);
        nodeBuilder.setText(text);
        return nodeBuilder.build();
    }
}
