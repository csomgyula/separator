package separator.sample.simpleblock;

import separator.Node;
import separator.Separator;

import java.util.List;
import java.util.Hashtable;

/**
 * Demonstrates simple blocks.
 */
public class Template {
    private Separator separator;

    public static void main(String[] args){
        Template template = new Template();
        System.out.println("template grammar: " + template.getRules());

        // parse template
        String templateText = "Hello {{name}}!";
        System.out.println("template: " + templateText);
        Node root = template.parse(templateText);
        System.out.print("tree: " + root);

        // build vars
        Hashtable<String, String> vars = new Hashtable<String, String>();
        String name = args.length == 0 ? "world" : args[0];
        vars.put("name", name);

        // generate text
        System.out.println("generated text: " + template.generate(root.getChildren(), vars));
    }

    public Template(){
        separator = new Separator("[var]const \\{\\{ \\}\\}");
        // TODO: simple patterns should be handled as strings instead of regexps. For instance the ability to use "{{" instead  of "\\{\\{"
    }
    public Node parse(String text){
        return separator.separate(text);
    }

    public String generate(List<Node> nodes, Hashtable<String, String> vars){
        StringBuilder sb = new StringBuilder();

        // iterate through nodes
        for (Node node : nodes){
            String content = node.getContent();

            // if node represents a variable then get the dynamic value
            if ( node.isA("variable") || node.isA("var") ){
                content = vars.get(content);
            }

            sb.append(content);
        }

        return sb.toString();
    }

    public String getRules() {
        return separator.getRules();
    }
}
