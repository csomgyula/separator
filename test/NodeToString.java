package separator.test;

import separator.Node;
import separator.Tag;

/**
 *
 */
public class NodeToString {
    private String identAtom;

    public NodeToString(){
        identAtom = IDENT_ATOM;
    }

    public NodeToString(String identAtom){
        this.identAtom = identAtom;
    }

    public String toString(Node node){
        StringBuilder string = new StringBuilder();
        toString(0, node, string, 0);
        return string.toString();
    }

    protected void toString(int index, Node node, StringBuilder string, int ident){
        // ident
        ident(string, ident);

        // tag name and index
        string.append(node.getTag().getName());
        if (node.getTag().getKind() != Tag.Kind.ROOT){
            string.append("(");
            string.append(index);
            string.append(")");
        }
        string.append(": ");

        // content
        if (node.getContent()!=null){
            string.append(node.getContent());
        }
        string.append("\n");

        // children
        index = 0;
        for (Node child : node.getChildren()){
            toString(index++, child, string, ident+1);
        }
    }

    protected final static String IDENT_ATOM = "  ";

    protected void ident(StringBuilder string, int ident){
        for (int i=0; i< ident; i++){
            string.append(identAtom);
        }
    }
}
