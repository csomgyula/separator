package separator.test.nodebuilder;

import separator.Node;
import separator.NodeBuilder;
import separator.test.data.AbstractTestData;

/**
 *
 */
public abstract class AbstractNodeBuilderTest {
    public void test() {
        AbstractTestData data = getData();
        for (String text : data.getTexts()) {
            NodeBuilder builder = new NodeBuilder(data.getTags(), text);
            Node root = builder.build();
            System.out.println("test: " + data.getName());
            System.out.println("text: \"" + text + "\" (len: " + text.length() + ")");
            System.out.println("tree: \n" + root);

            System.out.println();
        }
    }

    public abstract AbstractTestData getData();
}
