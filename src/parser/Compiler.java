package separator.parser;

import separator.Tag;
import separator.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Represents the DSL compiler.
 */
public class Compiler {

    /**
     * Compiles the given rules into a tag list. The first item always represents the root tag (with the special
     * start-of-source and end-of-source tokens).
     */
    public List<Tag> compile(String rules){
        // instantiate tag list
        List<Tag> tags = new ArrayList<Tag>();

        // add root tag
        Tag tag = Tag.root();
        tags.add(tag);

        // build tag list according to the rules
        Tag parent;
        int index = 1;
        String[] items = rules.split(" ");
        Token token;
        Pattern tagNamePattern = Pattern.compile("[a-zA-Z]+");

        for (String item : items){
            // if item is a tagname:
            if ( tagNamePattern.matcher(item).matches() ){
                parent = tag;
                tag = new Tag();
                tag.setName(item);
                tag.setIndex(index++);
                tag.setKind(Tag.Kind.SIMPLE);
                tag.setParent(parent);
                tags.add(tag);
            }
            // else it is a token
            else{
                if (tag == null) throw new NullPointerException("token without tag: " + item);
                token = new Token();
                token.setPattern(item);
                token.setKind(Token.Kind.SIMPLE);
                token.setTag(tag);
                tag.getTokens().add(token);
            }
        }
        return tags;
    }
}
