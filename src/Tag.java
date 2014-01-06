package separator;

import java.util.regex.Pattern;

/**
 * Represents a tag, ie. node type.
 */
public class Tag {
    private String name;
    private Kind kind;
    private Tag parent;
    private int index;
    private Tag blockExt;
    private Pattern open, close;

    public String getName() {
        if (name == null && isRoot()){
           name = "ROOT";
        }
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

    public Tag getParent() {
        return parent;
    }

    public void setParent(Tag parent) {
        this.parent = parent;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Tag getBlockExt() {
        if (blockExt == null){
            blockExt = new Tag();
            blockExt.setName(getName()+"Ext");
            blockExt.setIndex(getIndex());
            blockExt.setParent(getParent());
            blockExt.setKind(Kind.SIMPLE_BLOCK_EXT);
        }
        return blockExt;
    }

    public void setBlockExt(Tag blockExt) {
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
