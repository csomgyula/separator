package separator.test.simpleblock;

import separator.Node;
import separator.Separator;
import separator.sample.NodeToString;

/**
 * Demonstrates nesting
 */
public class NestingTest {
    public static void main(String[] args){
        // rules
        String rules = "[block] \\{ \\} line \n [var]const \\(\\( \\)\\)";

        // text
        String block1 = "";
        block1 += "{";
        block1 += "const0\n";
        block1 += "((var1))\n";
        block1 += "const2.0((var2.1))\n";
        block1 += "((var3.0))const3.1\n";
        block1 += "((var4.0))((var4.1))\n";
        block1 += "const5.0((var5.1))const5.2\n";
        block1 += "((var6.0))const6.1((var6.2))\n";
        block1 += "}";

        String block2 = "{}";

        String block3 = "{const3}";

        String block4 = "{((var4))}";

        String block5 = "{\n}";

        String text = block1; // block1 + block2 + block3 + block4 + block5;

        // separate
        Separator separator = new Separator();

        Node root = separator.separate(rules, text);

        System.out.println(new NodeToString().toString(root));
    }
}
