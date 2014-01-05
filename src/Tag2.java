package separator;

import java.util.regex.Pattern;

/**
 * Represents a tag, ie. node type.
 */
public class Tag2 {
    private String name;
    private Kind kind;
    private Tag2 parent;
    private int index;
    private Tag2 blockExt;
    private Pattern open, close;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public Tag2 getParent() {
        return parent;
    }

    public void setParent(Tag2 parent) {
        this.parent = parent;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Tag2 getBlockExt() {
        return blockExt;
    }

    public void setBlockExt(Tag2 blockExt) {
        this.blockExt = blockExt;
    }

    public Pattern getOpen() {
        return open;
    }

    public void setOpen(Pattern open) {
        this.open = open;
    }

    public Pattern getClose() {
        return close;
    }

    public void setClose(Pattern close) {
        this.close = close;
    }

    public boolean isA(Kind kind){
        return getKind() == kind;
    }

    public boolean isBlock(){
        return kind != Kind.SIMPLE;
    }

    public boolean isRoot(){
        return kind == Kind.ROOT;
    }

    public enum Kind{
        ROOT,
        SIMPLE,
        SIMPLE_BLOCK, SIMPLE_BLOCK_EXT;
    }
}
