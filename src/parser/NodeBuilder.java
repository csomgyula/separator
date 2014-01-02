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

    /**
     * Builds the output tree according to the input text and the tokens emited by the tokenizer.
     * <p/>
     * Logic:
     * * new nodes are added as leaves, their tag is always the lowest in hiearchy that is independent from the tag of the token
     * * if the node cursor is higher in the hierarchy (grandpa or higher) create necessary parent nodes
     * * if the token is
     * * the root tag close nodes below by moving the cursor to the root
     * * the leaf tag then move the cursor to the new node's parent
     * * is not the leaf tag and not the root then close the node and nodes below by moving the cursor to its parent
     */
    public Node build() {
        // -- ------------------------------------------------------------------------------------------------------
        // -- create tokenizer
        // -- ------------------------------------------------------------------------------------------------------
        Tokenizer tokenizer = new Tokenizer();
        tokenizer.setTags(tags);
        tokenizer.setText(text);

        // -- ------------------------------------------------------------------------------------------------------
        // -- create root node
        // -- ------------------------------------------------------------------------------------------------------
        Node root = Node.root();
        root.setTag(tags.get(0));

        Tag rootTag = tags.get(0);
        Token.Instance openTokenInstance = new Token.Instance();
        openTokenInstance.setStartPosition(0);
        openTokenInstance.setEndPosition(0);
        openTokenInstance.setToken(rootTag.getTokenPairs().get(0).getOpen());
        root.setOpen(openTokenInstance);

        // read tokens and add nodes
        Token.Instance tokenInstance;
        Node node = root, newNode, childNode, parentNode;
        Tag tokenInstanceTag;
        Tag leafTag = tags.get(tags.size() - 1);

        // do while text doesn't reach end
        while (textPosition < text.length()) { // TODO: cache text length
            // -- ------------------------------------------------------------------------------------------------------
            // -- read next token
            // -- ------------------------------------------------------------------------------------------------------
            tokenInstance = tokenizer.next();

            // -- ------------------------------------------------------------------------------------------------------
            // -- build new node
            // -- ------------------------------------------------------------------------------------------------------
            // * new nodes are added as leaves,
            // * their tag is always the lowest in hiearchy that is independent from the tag of the token
            newNode = new Node();
            newNode.setTag(leafTag);
            newNode.setKind(Node.Kind.LEAF);
            newNode.setContent(text.substring(textPosition, tokenInstance.getStartPosition()));
            newNode.setOpen(openTokenInstance);
            newNode.setClose(tokenInstance);

            // -- ------------------------------------------------------------------------------------------------------
            // -- add new node
            // -- ------------------------------------------------------------------------------------------------------
            // if the node cursor's tag is same or the parent then add directly
            if (newNode.getTag() == node.getTag()) {
                node.getParent().addChild(newNode);
            } else if (newNode.getTag().getParent() == node.getTag()) {
                node.addChild(newNode);
            }
            // if the node cursor is higher in the hierarchy (grandpa or higher) create necessary parent nodes
            else {
                childNode = newNode;
                while (childNode.getTag().getParent() != node.getTag()) {
                    parentNode = new Node();
                    parentNode.setTag(childNode.getTag().getParent());
                    parentNode.setKind(Node.Kind.BRANCH);
                    parentNode.setOpen(openTokenInstance);
                    parentNode.addChild(childNode);
                    childNode = parentNode;
                }
                node.addChild(childNode);
            }

            // -- ------------------------------------------------------------------------------------------------------
            // -- move the cursor and close higher nodes if necessary
            // -- ------------------------------------------------------------------------------------------------------
            // after this the cursor will point to the deepest node that is not yet closed, or null

            tokenInstanceTag = tokenInstance.getToken().getTag();
            // if the token tag is the root tag (ie. EOS) then close everything by moving the cursor up to the root
            if (tokenInstanceTag == rootTag) {
                while (node != null) {
                    node.setClose(tokenInstance);
                    node = node.getParent();
                }
            }
            // if the token is the leaf tag and not the root tag, nothing is closed just move the cursor to the new node's parent
            else if (tokenInstanceTag == leafTag && tokenInstanceTag != rootTag) {
                node = newNode.getParent();
            }
            // if the token is neither the leaf tag nor the root tag then close the associated node that is move the cursor to its parent
            else if (tokenInstanceTag != rootTag) {
                node = newNode;
                while (node.getTag() != tokenInstanceTag.getParent()) {
                    node.setClose(tokenInstance);
                    node = node.getParent();
                }
            }

            // -- ------------------------------------------------------------------------------------------------------
            // -- increment the text position, shift open token
            // -- ------------------------------------------------------------------------------------------------------
            textPosition = tokenInstance.getEndPosition();
            openTokenInstance = tokenInstance;
        }

        // it is possible that some nodes are not closed (if the end of the string is a separator then EOS is not read
        // hence root is not closed)

        // if root is closed then everything else is closed, hence it is enough to check whether root is closed
        if (root.getClose() == null){
            // read EOS
            tokenInstance = tokenizer.next();

            // move up to the root and close everything
            while (node != null) {
                // defensive coding
                if (node.getClose() == null){
                    node.setClose(tokenInstance);
                }
                node = node.getParent();
            }
        }
        return root;
    }
}
