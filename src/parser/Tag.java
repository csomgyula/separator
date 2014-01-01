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
        tag.setKind(Kind.ROOT);
        return tag;
    }

    /**
     * Return an end-of-stream tag.
     */
    public static Tag eos(){
        Tag tag = new Tag();
        tag.setKind(Kind.EOS);
        return tag;
    }

    private String name;

    private List<Token> tokens;

    private List<Token.Pair> tokenPairs;

    private List<Token> escapes;

    private Kind kind;

    private int index;

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
        if (tokenPairs ==null){
            tokenPairs = new ArrayList<Token.Pair>();
        }
        return tokenPairs;
    }

    public List<Token> getEscapes() {
        if (escapes==null){
            escapes = new ArrayList<Token>();
        }
        return escapes;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Tag type.
     */
    public enum Kind {
        ROOT,
        EMPTY,
        SIMPLE,
        BLOCK,
        RECURSIVE_BLOCK,
        // SKIP,
        ESCAPE,
        END,
        EOS;
    }
}
