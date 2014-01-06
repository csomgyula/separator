package separator;

/**
 * Represents a separator instance.
 */
public class Token {
    private Tag tag;
    private int start, end;
    private Kind kind;
    private SpecialKind specialKind;

    public Token(){
        specialKind = SpecialKind.NOT_SPECIAL;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
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

    public boolean isOpen(){
        return getKind() == Kind.OPEN;
    }

    public boolean isClose(){
        return getKind() == Kind.CLOSE;
    }

    public boolean isNotSpecial(){
        return getSpecialKind() == SpecialKind.NOT_SPECIAL;
    }

    public boolean isSOS(){
        return getSpecialKind() == SpecialKind.SOS;
    }

    public boolean isEOS(){
        return getSpecialKind() == SpecialKind.EOS;
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
