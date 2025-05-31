package runthenumbers.math.tokenize;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO
 * @date May 30, 2025
 * @author Richard Si
 */
public class Tokenizer {
    // LinkedHashMap maintains insertion order.
    private final LinkedHashMap<String, String> TOKEN_PATTERNS = new LinkedHashMap<>();
    private final Pattern TOKENIZE_PATTERN;
    
    public Tokenizer() {
        TOKEN_PATTERNS.put("Number", """
                                     # Negative sign.
                                     [\\-]?
                                     # Value portion.
                                     (
                                         # .NN
                                         (\\.[0-9]*[1-9])
                                         | (
                                             # NN.NN
                                             [0-9]+(?:\\.[0-9]*[1-9]?)
                                             # NNN
                                             | [1-9][0-9]*
                                             # Zero, literally.
                                             | 0)
                                     )
                                     """);
        TOKEN_PATTERNS.put("Variable", """
                                       # Match any letter.
                                       [a-zA-Z]
                                       # ... but only if no letters follow.
                                       (?![a-zA-Z])
                                       """);
        TOKEN_PATTERNS.put("Operator", "[\\+\\-\\*\\/\\^]");
        TOKEN_PATTERNS.put("Parenthesis", "\\(|\\)");
        TOKEN_PATTERNS.put("Whitespace", "\\s");
        TOKEN_PATTERNS.put("Unknown", ".");
        
        // Combine individual regexes into one single regex in order.
        ArrayList<String> parts = new ArrayList<>();
        TOKEN_PATTERNS.forEach((k, v) -> {
            parts.add(String.format("(?<%s>%s)", k, v));
        });
        // Pattern.COMMENTS enables extended mode, i.e. whitespace and comments
        // are ignored in regexes (which makes them much easier to read/maintain).
        TOKENIZE_PATTERN = Pattern.compile(String.join("|", parts), Pattern.COMMENTS);
    }
    
    /**
     * TODO
     * @param expression
     * @return 
     */
    public TokenStream tokenize(String expression) {
        ArrayList<Token> tokens = new ArrayList<>();
        Matcher matcher = TOKENIZE_PATTERN.matcher(expression);
        String kind = "", value;
        
        while (matcher.find()) {
            // Determine which group (AKA token type) matched.
            if ((value = matcher.group("Number")) != null)
                kind = "Number";
            else if ((value = matcher.group("Operator")) != null)
                kind = "Operator";
            else if ((value = matcher.group("Parenthesis")) != null)
                kind = "Parenthesis";
            else if ((value = matcher.group("Whitespace")) != null)
                kind = "Whitespace";
            else {
                assert matcher.group("Unknown") != null;
                // TODO: raise a proper error
                throw new Error("unexpected token: " + matcher.group());
            }
            // Add all matched tokens except for whitespace as they're irrelevant.
            if (!kind.equals("Whitespace"))
                tokens.add(new Token(kind, value, new Point(0, 0))); // TODO: handle position
        }
        
        tokens = fixupTokens(tokens);
        validateTokens(tokens);
        return new TokenStream(tokens);
    }
    
    /**
     * TODO
     * @param tokens
     * @return 
     */
    private static ArrayList<Token> fixupTokens(ArrayList<Token> tokens) {
        return tokens;
    }
    
    /**
     * TODO
     * @param tokens 
     */
    private static void validateTokens(ArrayList<Token> tokens) {
        
    }
    
}
