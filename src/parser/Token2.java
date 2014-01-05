package separator.parser;

import separator.Tag2;

/**
 * Represents a separator instance.
 */
public class Token2 {
    private Tag2 tag;
    private int start, end;
    private Kind kind;
    private SpecialKind specialKind;

    public Token2(){
        specialKind = SpecialKind.NOT_SPECIAL;
    }

    public Tag2 getTag() {
        return tag;
    }

    public void setTag(Tag2 tag) {
        this.tag = tag;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public SpecialKind getSpecialKind() {
        return specialKind;
    }

    public void setSpecialKind(SpecialKind specialKind) {
        this.specialKind = specialKind;
    }

    public boolean isSpecial(){
        return specialKind != SpecialKind.NOT_SPECIAL;
    }

    public boolean isA(Kind kind){
        return getKind() == kind;
    }

    protected enum Kind{
        OPEN, CLOSE;
    }

    protected enum SpecialKind{
        SOS, EOS, NOT_SPECIAL;
    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();

        // tag
        stringBuilder.append(getTag().getName());

        // kind
        stringBuilder.append(" : ");
        stringBuilder.append(getKind());
        if (isSpecial()){
            stringBuilder.append("/");
            stringBuilder.append(getSpecialKind());
        }

        // pos
        stringBuilder.append(" @");
        stringBuilder.append(getStart());
        stringBuilder.append(",");
        stringBuilder.append(getEnd());

        return stringBuilder.toString();
    }
}
