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

    public TokenStream[] split(String onType) {
        ArrayList<ArrayList<Token>> sections = new ArrayList<>();
        sections.add(new ArrayList<>());
        for (Token t : tokens) {
            if (t.is(onType))
                sections.add(new ArrayList<>());
            else
                sections.getLast().add(t);
        }

        TokenStream[] substreams = new TokenStream[sections.size()];
        for (int i = 0; i < sections.size(); i++)
            substreams[i] = new TokenStream(sections.get(i));

        return substreams;
    }
}
