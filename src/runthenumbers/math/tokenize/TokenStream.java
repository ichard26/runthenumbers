package runthenumbers.math.tokenize;

import java.util.ArrayList;

/**
 * @date May 25, 2025
 * @author Richard Si
 */
public class TokenStream {
    private final ArrayList<Token> tokens;
    private int index = 0;

    public TokenStream(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }
    
    public Token peek() {
        return tokens.get(index);
    }
    
    public Token next() {
        return tokens.get(index++);
    }
    
    public boolean onLastToken() {
        return tokens.size() <= index;
    }
    
    public boolean isExhausted() {
        return tokens.size() <= index + 1;
    }
    
    public void rollback(int by) {
        index -= by;
    }
    
    public void reset() {
        index = 0;
    }
}
