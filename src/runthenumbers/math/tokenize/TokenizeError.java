package runthenumbers.math.tokenize;

import java.util.List;

/**
 * Class Name: TokenizeError
 * Description: Error subclass raised when a math expression is invalid.
 * Programmer: Richard Si
 * Date: May 31, 2025
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
        String pointer = " ".repeat(position.getStart() + 1) + "~^~";
        formattedError = String.join("\n", List.of(message, original, pointer));
    }

    // Getters (no setters as this is an immutable class).

    public Span getPosition() {
        return position;
    }

    public String getFullInput() {
        return fullInput;
    }

    /**
     * Method Name: getMessage
     * Description: Provide an alternative error message for custom Error.
     * @return the primary error message used by Java's traceback.
     */
    @Override
    public String getMessage() {
        return formattedError;
    }

}
