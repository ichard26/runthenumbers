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
    public static ParseResult parse(TokenStream stream) {
        Expression expr = _parseExpression(stream);
        assert stream.isExhausted() : "did not consume all tokens";
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
            lhs = new Number(Double.parseDouble(lhs_token.getValue()));
        }
        else if (lhs_token.is("Variable")) {
            lhs = new Variable(lhs_token.getValue());
        }
        else if (lhs_token.is("LeftBracket")) {
            // We're entering a parenthesized sub-expression (it's recursion time!)
            lhs = new Group(_parseExpression(stream));
            if (!stream.next().is("RightBracket"))
                throw new AssertionError("next token should be a closing bracket");
        }
        
        while (!stream.onLastToken()) {
            Token op_token = stream.peek();
            if (op_token.is("RightBracket"))
                // The sub-expression is finished so return the LHS now.
                break;
            
            BindingPower bp = _infixBindingPower(op_token.getValue());
            if (bp.left() < minimumBP)
                break;
            
            stream.next();
            rhs = _parseExpression(stream, bp.right());
            lhs = new Operation(lhs, op_token.getValue(), rhs);
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
