package separator.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a tag
 */
public class Tag {
    /**
     * Return a root tag.
     */
    public static Tag root(){
        Tag tag = new Tag();
        tag.setType(Type.ROOT);
        return tag;
    }

    /**
     * Return an end-of-stream tag.
     */
    public static Tag eos(){
        Tag tag = new Tag();
        tag.setType(Type.EOS);
        return tag;
    }

    private String name;

    private List<TokenType> tokens;

    private List<TokenTypePair> tokenPairs;

    private List<TokenType> escapes;

    private Type type;

    private int index;

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        if(name==null){
            name = ("@"+getType().toString()).intern();
        }
        return name;
    }

    public List<TokenType> getTokens() {
        if (tokens==null){
            tokens = new ArrayList<TokenType>();
        }
        return tokens;
    }

    public List<TokenTypePair> getTokenPairs() {
        if (tokenPairs ==null){
            tokenPairs = new ArrayList<TokenTypePair>();
        }
        return tokenPairs;
    }

    public List<TokenType> getEscapes() {
        if (escapes==null){
            escapes = new ArrayList<TokenType>();
        }
        return escapes;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public enum Type{
        ROOT,
        EMPTY,
        SIMPLE,
        BLOCK,
        RECURSIVE_BLOCK,
        SKIP,
        ESCAPE,
        END,
        EOS;
    }
}