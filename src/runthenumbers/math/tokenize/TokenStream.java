package runthenumbers.math.tokenize;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Name: TokenStream
 * Description: File-like wrapper over a list of tokens.
 * Programmer: Richard Si
 * Date: May 25, 2025
 */
public class TokenStream {
    private final ArrayList<Token> tokens;
    private int index = 0;

    public TokenStream(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    // Getters (no setters as these attributes should be modified).

    public List<Token> getTokens() {
        // Return an immutable copy.
        return List.copyOf(tokens);
    }

    public int getIndex() {
        return index;
    }

    /**
     * Method Name: peek
     * Description: Peek at the current token, but do not advance the stream.
     * @return The current token.
     */
    public Token peek() {
        return tokens.get(index);
    }

    /**
     * Method Name: next
     * Description: Get the current token and advance to the next token.
     * @return The current token.
     */
    public Token next() {
        return tokens.get(index++);
    }

    /**
     * Method Name: isExhausted
     * Description: Is the current also the last token in the stream?
     * @return yes/no.
     */
    public boolean isExhausted() {
        return tokens.size() <= index + 1;
    }

    /**
     * Method Name: rollback
     * Description: Go back N tokens.
     * @param by How many tokens to go back by.
     */
    public void rollback(int by) {
        index -= by;
    }

    /**
     * Method Name: reset
     * Description: Rollback the stream back to the start.
     */
    public void reset() {
        index = 0;
    }

    /**
     * Method Name: split
     * Description: Split the token stream into several token streams.
     * @param onType Token type on which to divide the stream.
     * @return The sub-streams.
     */
    public TokenStream[] split(String onType) {
        ArrayList<ArrayList<Token>> sections = new ArrayList<>();
        sections.add(new ArrayList<>());
        for (Token t : tokens) {
            if (t.is(onType))
                // Encountered the divider type, start a new sub-stream.
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
