package runthenumbers.math.tokenize;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class Name: Tokenizer
 * Description: Handles splitting a math expression into its constituent tokens.
 * Programmer: Richard Si
 * Date: May 25, 2025
 */
public class Tokenizer {
    // LinkedHashMap maintains insertion order, needed for proper tokenization.
    private static final LinkedHashMap<String, String> TOKEN_PATTERNS = new LinkedHashMap<>();
    private static final Pattern TOKENIZE_PATTERN;
    
    static {
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
        TOKEN_PATTERNS.put("EqualSign", "=");
        TOKEN_PATTERNS.put("LeftBracket", "\\(");
        TOKEN_PATTERNS.put("RightBracket", "\\)");
        TOKEN_PATTERNS.put("Whitespace", "\\s");
        TOKEN_PATTERNS.put("Unknown", ".");
        
        // Combine individual regexes into one single regex, in order.
        ArrayList<String> parts = new ArrayList<>();
        TOKEN_PATTERNS.forEach((k, v) -> {
            parts.add(String.format("(?<%s>%s)", k, v));
        });
        // Pattern.COMMENTS enables extended mode, i.e. whitespace and comments
        // are ignored in regexes (which makes them much easier to read/maintain).
        TOKENIZE_PATTERN = Pattern.compile(String.join("|", parts), Pattern.COMMENTS);
    }
    
    /**
     * Method Name: tokenize
     * Description: Split a math expression into its constituent tokens.
     * @param expression The math expression to tokenize.
     * @return A stream of parsed tokens.
     */
    public static TokenStream tokenize(String expression) {
        ArrayList<Token> tokens = new ArrayList<>();
        Matcher matcher = TOKENIZE_PATTERN.matcher(expression);
        String type = "", value = "";
        Span position;
        
        // Search expression for matching tokens using combined regex.
        while (matcher.find()) {
            // Determine which group (AKA token type) matched.
            for (String potentialType : TOKEN_PATTERNS.keySet()) {
                if ((value = matcher.group(potentialType)) != null) {
                    type = potentialType; 
                    break;
                }
            }
            position = new Span(matcher.start(), matcher.end());
            
            if (type.equals("Unknown"))
                throw new TokenizeError(
                        "Unexpected character: " + value, position, expression);
            
            // Add all matched tokens except for whitespace as they're irrelevant.
            if (!type.equals("Whitespace"))
                tokens.add(new Token(type, value, position));
        }
        
        tokens = fixupTokens(tokens);
        validateTokens(tokens, expression);
        return new TokenStream(tokens);
    }
    
    /**
     * Method Name: fixupTokens
     * Description: Apply adjustments to tokens to replace unsupported syntax
     *               with the supported standard form.
     * @param tokens the array of tokens to fix.
     * @return the array of fixed tokens.
     */
    private static ArrayList<Token> fixupTokens(ArrayList<Token> tokens) {
        ArrayList<Token> fixedTokens = new ArrayList<>();
        Token current, next;
        
        for (int i = 0; i < tokens.size() - 1; i++) {
            current = tokens.get(i);
            next = tokens.get(i+1);
            
            fixedTokens.add(current);
            // Fix-up 1: Replace a negative number with a subtraction operator
            //           if followed by a number/variable/opening bracket.
            if (current.is("Number", "Variable", "RightBracket")
                    && next.is("Number") 
                    && next.getValue().startsWith("-")) {
                fixedTokens.add(new Token("Operator", "-", new Span(-1, -1)));
                // Remove the negative sign from the next number token.
                next.setValue(next.getValue().substring(1));
            }
            // Fix-up 2: Add a multiply between adjacent closing/opening brackets.
            else if (current.is("RightBracket") && next.is("LeftBracket")) {
                fixedTokens.add(new Token("Operator", "*", new Span(-1, -1)));
            }
            // Fix-up 3: Add a multiply between a number/opening bracket and variable.
            else if (current.is("Number") && next.is("Variable", "LeftBracket")) {
                fixedTokens.add(new Token("Operator", "*", new Span(-1, -1)));
            }
        }
        fixedTokens.add(tokens.getLast());
        
        return fixedTokens;
    }
    
    /**
     * Method Name: validateTokens
     * Description: Search for nonsense token sequences, raising an error if found.
     * @param tokens The array of tokens to validate.
     */
    private static void validateTokens(ArrayList<Token> tokens, String originalInput) {
        Token current, next;
        ArrayList<Token> equalSigns = new ArrayList<>();
        
        for (int i = 0; i < tokens.size(); i++) {
            current = tokens.get(i);
            if (current.is("EqualSign"))
                equalSigns.add(current);
            
            // The following checks require the next token to be non-null.
            next = (i+1 < tokens.size()) ? tokens.get(i+1) : null;
            if (next == null)
                continue;
            
            // Error 1: adjacent operators
            if (current.is("Operator") && next.is("Operator"))
                throw new TokenizeError("Adjacent operators", next.getPosition(), originalInput);
            // Error 2: adjacent numbers
            if (current.is("Number") && next.is("Number"))
                throw new TokenizeError("Adjacent numbers", next.getPosition(), originalInput);
            // Error 3: operator that is not followed by a number, variable, or opening bracket
            if (current.is("Operator") && !next.is("Number", "Variable", "LeftBracket"))
                throw new TokenizeError(
                        next.getType() + " cannot come after an operator", next.getPosition(), originalInput);
        }
        
        // Error 4: more than one equal sign
        if (equalSigns.size() > 1) {
            // Place the error caret at the second equal sign.
            throw new TokenizeError(
                    "More than one equal sign", equalSigns.get(1).getPosition(), originalInput);
        }
    }
    
}
