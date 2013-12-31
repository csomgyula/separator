package separator.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility function of tags.
 */
public class Tags {
    private List<Tag> tags;

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
        tokens = null;
    }

    List<TokenType> tokens;

    /**
     * Returns all tokens found in tags.
     */
    public List<TokenType> getTokens(){
       if (tokens==null && tags != null){
          tokens = new ArrayList<TokenType>();
          for (Tag tag : tags){
              switch (tag.getType()){
                  case SIMPLE: tokens.addAll(tag.getTokens()); break;
                  case BLOCK: tokens.addAll(getBlockTokens(tag)); break;
                  case RECURSIVE_BLOCK: tokens.addAll(getBlockTokens(tag)); break;
                  case ESCAPE: tokens.addAll(getBlockTokens(tag)); tokens.addAll(tag.getEscapes()); break;
                  case SKIP: tokens.addAll(getBlockTokens(tag)); tokens.addAll(tag.getEscapes()); break;
                  default: break;
              }
          }
       }
       return tokens;
    }

    protected List<TokenType> getBlockTokens(Tag tag){
        List<TokenType> tokens = new ArrayList<TokenType>();
        for (TokenTypePair pair : tag.getTokenPairs()){
            tokens.add(pair.getOpen());
            tokens.add(pair.getClose());
        }
        return tokens;
    }
}
