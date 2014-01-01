package separator.parser;

import separator.Node;
import separator.Tag;
import separator.Token;

import java.util.List;

/**
 * Prototype implementation of the node builder. Under construction.
 * <p/>
 * TODO: add source refs to nodes: open/close token, start/end index
 */
public class NodeBuilder {
    private List<Tag> tags;

    private String text;

    private int textPosition;

    /**
     * The text to build nodes from.
     */
    public void setText(String text) {
        this.text = text;
        textPosition = 0;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Node build() {
        // create tokenizer
        Tokenizer tokenizer = new Tokenizer();
        tokenizer.setTags(tags);
        tokenizer.setText(text);

        // create root node
        Node root = Node.root();
        root.setTag(tags.get(0));

        // read tokens and add nodes
        Token.Instance tokenInstance;
        Node node = root, newNode, childNode, parentNode;
        Tag tokenInstanceTag;
        while (textPosition < text.length()) { // TODO: cache text length
            // read next token
            tokenInstance = tokenizer.next();

            // build the new node
            newNode = new Node();
            newNode.setTag(tokenInstance.getToken().getTag());
            newNode.setKind(Node.Kind.LEAF);
            newNode.setContent(text.substring(textPosition, tokenInstance.getStartPosition()));

            // add node - the add logic depends on the tag:
            // whether it is higher or deeper in the tag hierarchy than the node cursor

            // if it is deeper in the tag hierarchy than the node cursor:
            if (newNode.getTag().isDeeper(node.getTag())) {
                // create missing parent nodes if necessary
                childNode = newNode;
                while (childNode.getTag().getParent() != node.getTag()) {
                    parentNode = new Node();
                    parentNode.setTag(childNode.getTag().getParent());
                    parentNode.setKind(Node.Kind.BRANCH);
                    parentNode.addChild(childNode);
                    childNode = parentNode;
                }
                node.addChild(childNode);

                // move node cursor down to the new node
                node = newNode;
            }
            // if it is higher in the hierarchy than the node cursor:
            else if (newNode.getTag().isHigher(node.getTag())) {
                // replace tag with the node's tag
                newNode.setTag(node.getTag());

                // add this node to the parent of the node cursor
                node.getParent().addChild(newNode);

                tokenInstanceTag = tokenInstance.getToken().getTag();

                // if not root move the cursor up to the parent of the token tag
                if (tokenInstanceTag.getKind() != Tag.Kind.ROOT) {
                    while (node.getTag() != tokenInstanceTag.getParent()) {
                        node = node.getParent();
                    }
                }
                else{
                    node = root;
                }
            }
            // if they are same in the hierarchy
            else {
                // add this node to the parent of the node cursor  except it is the root node
                if (node.getTag().getKind() != Tag.Kind.ROOT) {
                    node.getParent().addChild(newNode);
                    // set the node cursor to this
                    node = newNode;
                } else {
                    node.setContent(newNode.getContent());
                }
            }

            // increment the text position
            textPosition = tokenInstance.getEndPosition();
        }

        return root;
    }
}
