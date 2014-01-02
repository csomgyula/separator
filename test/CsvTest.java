package separator.test;

import separator.Node;
import separator.Separator;

/**
 * Separators xan be nested
 */
public class CsvTest {
    public static void main(String[] args){
        String rules = "record \n field ;";

        String line0 = "\n";
        String line1 = "special\n";
        String line2 = "1;2;3;4\n";
        String line3 = "a;b;c;d\n";
        String line4 = "árvíz;tükör;fúró;gép\n";

        String csv = line0 + line1 + line2 + line3 + line4;

        Separator separator = new Separator();

        Node node = separator.separate(rules, csv);

        System.out.println("Number of records: " + node.getChildren().size());
        NodeToString nodeToString = new NodeToString();
        System.out.println(nodeToString.toString(node));

/*        int index1 = 0, index2;
        for (Node record : node.getChildren()){
            System.out.println(record.getTag().getName()+ "(" +  (index1++) + "):");
            index2 = 0;
            for (Node field : record.getChildren()){
                System.out.println("\t" +field.getTag().getName()+ "(" +  (index2++) + "): " + field.getContent());
            }
        }*/
    }
}
