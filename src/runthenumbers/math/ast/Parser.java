package runthenumbers.math.ast;

import runthenumbers.math.tokenize.Token;
import runthenumbers.math.tokenize.TokenStream;

/**
 * TODO
 * TODO: are java records acceptable?
 */
record BindingPower(int left, int right) {};

/**
 * TODO
 * @date May 25, 2025
 * @author Richard Si
 */
public class Parser {    
    /**
     * TODO
     * @param stream
     * @return 
     */
    public static Expression parse(TokenStream stream) {
        Expression expr = _parseExpression(stream);
        if (!stream.isExhausted())
            throw new AssertionError("did not consume all tokens");
        return expr;
    }
    
    private static Expression _parseExpression(TokenStream stream) {
        return _parseExpression(stream, 0);
    }
    
    /**
     * TODO
     * @param minimumBP
     * @return 
     */
    private static Expression _parseExpression(TokenStream stream, int minimumBP) {
        // NOTE: LHS = left hand side, RHS = right hand side.
        Expression lhs = null, rhs;
        Token lhs_token = stream.next();
        if (lhs_token.is("Number")) {
            lhs = new Number(Double.parseDouble(lhs_token.value));
        }
        else if (lhs_token.is("Variable")) {
            lhs = new Variable(lhs_token.value);
        }
        else if (lhs_token.is("Parenthesis") && lhs_token.value.equals("(")) {
            // We're entering a parenthesized sub-expression (it's recursion time!)
            lhs = new Group(_parseExpression(stream));
            if (!stream.next().value.equals(")"))
                throw new AssertionError("next token should be a closing paren");
        }
        
        while (!stream.onLastToken()) {
            Token op_token = stream.peek();
            if (op_token.is("Parenthesis") && op_token.value.equals(")"))
                // The sub-expression is finished so stop parsing.
                break;
            
            BindingPower bp = _infixBindingPower(op_token.value);
            if (bp.left() < minimumBP)
                break;
            
            stream.next();
            rhs = _parseExpression(stream, bp.right());
            lhs = new Operation(lhs, op_token.value, rhs);
        }
        
        return lhs;
    }
    
    /**
     * TODO
     * @param op
     * @return 
     */
    private static BindingPower _infixBindingPower(String op) {
        return switch (op) {
            case "+", "-" -> new BindingPower(1, 2);
            case "*", "/" -> new BindingPower(3, 4);
            case "^" -> new BindingPower(6, 5);
            default -> throw new AssertionError("unexpected operator: " + op);
        };
    }   
}
