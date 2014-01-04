package separator.parser;

import separator.Tag;
import separator.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the DSL compiler.
 *
 * TODO: simple patterns should be handled as strings instead of regexps. For instance the ability to use "{{" instead  of "\\{\\{"
 *
 * FIXME: handle parity error in block defs
 */
public class Compiler {

    /**
     * Compiles the given rules into a tag list. The first item always represents the root tag (with the special
     * start-of-source and end-of-source tokens).
     */
    public List<Tag> compile(String rules){
        // states
        List<Tag> tags;
        Tag nextTag, previousTag;
        Token.Pair nextTokenPair;
        int parity = 0;
        Tag blockExtTag;
        String tagName, blockExtName;

        // instantiate tag list
        tags = new ArrayList<Tag>();

        // add root tag
        nextTag = Tag.root();
        tags.add(nextTag);
        previousTag = nextTag;

        // build tag list according to the rules
        String[] rulesParts = rules.split(" +");
        Token nextToken, previousToken = null;
        Pattern simple = Pattern.compile("[a-zA-Z]+");
        Pattern simpleBlock = Pattern.compile("\\[([a-zA-Z]+)\\]([a-zA-Z]+)?");
        Matcher simpleMatcher, simpleBlockMatcher;

        for (String rulesPart : rulesParts){
            simpleMatcher = simple.matcher(rulesPart);
            simpleBlockMatcher = simpleBlock.matcher(rulesPart);

            // simple separator:
            if ( simpleMatcher.matches() ){
                nextTag = new Tag();
                nextTag.setName(rulesPart);
                nextTag.setIndex(previousTag.getIndex() + 1);
                nextTag.setKind(Tag.Kind.SIMPLE);
                nextTag.setParent(previousTag);
                tags.add(nextTag);
                previousTag = nextTag;
            }
            // simple block
            else if (simpleBlockMatcher.matches()){
                tagName = simpleBlockMatcher.group(1);
                blockExtName = simpleBlockMatcher.group(2);

                // block tag
                nextTag = new Tag();
                nextTag.setName(tagName);
                nextTag.setIndex(previousTag.getIndex() + 1);
                nextTag.setKind(Tag.Kind.SIMPLE_BLOCK);
                nextTag.setParent(previousTag);
                tags.add(nextTag);

                // block-ext
                blockExtTag = new Tag();
                blockExtTag.setName(blockExtName != null ? blockExtName : nextTag.getName()+"Ext");
                blockExtTag.setIndex(nextTag.getIndex());
                blockExtTag.setKind(Tag.Kind.SIMPLE_BLOCK_EXT);
                blockExtTag.setParent(nextTag.getParent());
                nextTag.setBlockExt(blockExtTag);

                previousTag = nextTag;
            }
            // token
            else{
                if (nextTag == null) throw new NullPointerException("token without tag: " + rulesPart);

                // general
                nextToken = new Token();
                nextToken.setPattern(Pattern.compile(rulesPart));
                nextToken.setTag(nextTag);

                // simple separator
                if (nextTag.isA(Tag.Kind.SIMPLE)){
                    nextToken.setKind(Token.Kind.SIMPLE);
                    nextTag.getTokens().add(nextToken);
                }
                // simple block
                else{
                    if (parity == 0){
                        nextToken.setKind(Token.Kind.SIMPLE_BLOCK_OPEN);
                        previousToken = nextToken;
                        parity = 1;
                    }
                    else{
                        nextToken.setKind(Token.Kind.SIMPLE_BLOCK_CLOSE);
                        nextTokenPair = new Token.Pair();
                        nextTokenPair.setTag(nextTag);
                        nextTokenPair.setOpen(previousToken);
                        nextTokenPair.setClose(nextToken);
                        nextTag.getTokenPairs().add(nextTokenPair);
                        parity = 0;
                    }
                }
            }
        }
        return tags;
    }
}
