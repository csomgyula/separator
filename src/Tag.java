package separator;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a tag, ie. node type.
 */
public class Tag {
    /**
     * Return a root tag.
     */
    public static Tag root(){
        Tag tag = new Tag();
        tag.setKind(Kind.ROOT);
        tag.setIndex(0); // highest in hierarchy

        Token.Pair tokenPair = new Token.Pair();

        Token sosToken = new Token();
        sosToken.setKind(Token.Kind.SOS);
        sosToken.setTag(tag);

        Token eosToken = new Token();
        eosToken.setKind(Token.Kind.EOS);
        eosToken.setTag(tag);

        tokenPair.setOpen(sosToken);
        tokenPair.setClose(eosToken);
        tokenPair.setTag(tag);

        tag.getTokenPairs().add(tokenPair);

        return tag;
    }

    private String name;

    private List<Token> tokens;

    private List<Token.Pair> tokenPairs;

    private Kind kind;

    private int index;

    private Tag parent;

    private Tag blockExt;

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public Kind getKind() {
        return kind;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        if(name==null){
            name = ("@"+ getKind().toString()).intern();
        }
        return name;
    }

    public List<Token> getTokens() {
        if (tokens==null){
            tokens = new ArrayList<Token>();
        }
        return tokens;
    }

    public List<Token.Pair> getTokenPairs() {
        if (tokenPairs==null){
            tokenPairs = new ArrayList<Token.Pair>();
        }
        return tokenPairs;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Tag getParent() {
        return parent;
    }

    public void setParent(Tag parent) {
        this.parent = parent;
    }

    public Tag getBlockExt() {
        return blockExt;
    }

    public void setBlockExt(Tag blockExt) {
        this.blockExt = blockExt;
    }

    public boolean isA(String tag){
        return getName().equals(tag);
    }

    public boolean isA(Kind kind){
        return getKind() == kind;
    }

    /**
     * Tag type.
     */
    public enum Kind {
        ROOT,
        // EMPTY,
        SIMPLE,
        SIMPLE_BLOCK, SIMPLE_BLOCK_EXT;
        // RECURSIVE_BLOCK,
        // SKIP,
        // ESCAPE,
        // END
    }
}
