package runthenumbers.math.tokenize;

import java.util.List;

/**
 * TODO
 * @author Richard Si
 */
public class TokenizeError extends RuntimeException {
    private final String message;
    private final Span position;
    private final String fullInput;
    private final String formattedError;

    public TokenizeError(String message, Span position, String fullInput) {
        this.message = message;
        this.position = position;
        this.fullInput = fullInput;
        
        // Format a friendly error for unexpected tokens.
        // Also show the original input and point to the first bad character.
        String original = "  " + fullInput;
        String pointer = " ".repeat(position.getStart() + 2) + "^";
        formattedError = String.join("\n", List.of(message, original, pointer));
    }

    // Getters (no setters as this is an immutable class).
    
    public Span getPosition() {
        return position;
    }

    public String getFullInput() {
        return fullInput;
    }
    
    @Override
    public String getMessage() {
        return formattedError;
    }
    
}
