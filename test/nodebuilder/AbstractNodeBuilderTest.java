package separator.test.nodebuilder;

import separator.Node2;
import separator.NodeBuilder2;
import separator.test.data.AbstractTestData;

/**
 *
 */
public abstract class AbstractNodeBuilderTest {
    public void test() {
        AbstractTestData data = getData();
        for (String text : data.getTexts()) {
            NodeBuilder2 builder = new NodeBuilder2(data.getTags(), text);
            Node2 root = builder.build();
            System.out.println("test: " + data.getName());
            System.out.println("text: \"" + text + "\" (len: " + text.length() + ")");
            System.out.println("tree: \n" + root);

            System.out.println();
        }
    }

    public abstract AbstractTestData getData();
}
