package separator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the DSL compiler.
 *
 * TODO: simple patterns should be handled as strings instead of regexps. For instance the ability to use "{{" instead  of "\\{\\{"
 */
public class Compiler {

    /**
     * Compiles the given rules into a tag list. The first item always represents the root tag (with the special
     * start-of-source and end-of-source tokens).
     */
    public List<Tag> compile(String rules){
        // states
        List<Tag> tags;
        Tag nextTag, prevTag;
        Tag blockExtTag;
        String tagName, blockExtName;

        // instantiate tag list
        tags = new ArrayList<Tag>();

        // add root tag
        nextTag = new Tag();
        nextTag.setKind(Tag.Kind.ROOT);
        nextTag.setIndex(0); // highest in hierarchy
        tags.add(nextTag);
        prevTag = nextTag;

        // build tag list according to the rules
        String[] rulesParts = rules.split(" +");
        Token nextToken, previousToken = null;
        Pattern simple = Pattern.compile("[a-zA-Z]+");
        Pattern simpleBlock = Pattern.compile("\\[([a-zA-Z]+)\\]([a-zA-Z]+)?");
        Matcher simpleMatcher, simpleBlockMatcher;
        int blockTokenParity = 0;

        for (String rulesPart : rulesParts){
            simpleMatcher = simple.matcher(rulesPart);
            simpleBlockMatcher = simpleBlock.matcher(rulesPart);

            // simple separator:
            if ( simpleMatcher.matches() ){
                nextTag = new Tag();
                nextTag.setName(rulesPart);
                nextTag.setIndex(prevTag.getIndex() + 1);
                nextTag.setKind(Tag.Kind.SIMPLE);
                nextTag.setParent(prevTag);
                tags.add(nextTag);
                prevTag = nextTag;
            }
            // simple block:
            else if (simpleBlockMatcher.matches()){
                tagName = simpleBlockMatcher.group(1);
                blockExtName = simpleBlockMatcher.group(2);

                // block tag
                nextTag = new Tag();
                nextTag.setName(tagName);
                nextTag.setIndex(prevTag.getIndex() + 1);
                nextTag.setKind(Tag.Kind.SIMPLE_BLOCK);
                nextTag.setParent(prevTag);
                tags.add(nextTag);

                // block-ext
                blockExtTag = new Tag();
                blockExtTag.setName(blockExtName != null ? blockExtName : tagName+"Ext");
                blockExtTag.setIndex(nextTag.getIndex());
                blockExtTag.setKind(Tag.Kind.SIMPLE_BLOCK_EXT);
                blockExtTag.setParent(nextTag.getParent());
                nextTag.setBlockExt(blockExtTag);

                prevTag = nextTag;
            }
            // token
            else{
                if (nextTag == null) throw new NullPointerException("token without tag: " + rulesPart);
                Pattern pattern = Pattern.compile(rulesPart);

                // simple separator:
                if (nextTag.isA(Tag.Kind.SIMPLE)){
                    nextTag.setClose(pattern);
                }
                // simple block:
                else{
                    if (blockTokenParity == 0){
                        nextTag.setOpen(pattern);
                        blockTokenParity = 1;
                    }
                    else{
                        nextTag.setClose(pattern);
                        blockTokenParity = 0;
                    }
                }
            }
        }
        return tags;
    }
}
