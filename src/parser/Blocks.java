package separator.parser;

/**
 * Utility function of blocks. Under construction
 */
public class Blocks {
    private Block block;

    public void closeTag(){

    }

    protected void toParent() {
        if (!block.isRoot()){
            block = block.getParent();
        }
    }
    protected void toTag(Tag tag) {
        Block block = this.block;
        while (!block.isRoot()) {
            if (block.getTag() != tag) {
                block = block.getParent();
            }
        }
        this.block = block;
    }

    protected void toParent(Tag tag) {
        toTag(tag);
        toParent();
    }
}
