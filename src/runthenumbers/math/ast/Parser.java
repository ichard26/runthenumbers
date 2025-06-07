package runthenumbers.math.ast;

import runthenumbers.math.tokenize.Token;
import runthenumbers.math.tokenize.TokenStream;
import runthenumbers.math.tokenize.Tokenizer;

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
     * 
     * @param input
     * @return 
     */
    public static RootNode parse(String input) {
        return parse(Tokenizer.tokenize(input));
    }
    
    /**
     * TODO
     * @param stream
     * @return 
     */
    public static RootNode parse(TokenStream stream) {
        // TODO: flag expressions with variables
        TokenStream[] substreams = stream.split("EqualSign");
        // If there's an equal sign, parse each side of the eqn separately.
        if (substreams.length > 1) {
            assert substreams.length == 2 : "there should only be a left and right side";
            ExprNode leftExpr = _parseExpression(substreams[0]);
            ExprNode rightExpr = _parseExpression(substreams[1]);
            return new Equation(leftExpr, rightExpr);
        }
        
        ExprNode innerExpr = _parseExpression(stream);
        assert stream.isExhausted() : "did not consume all tokens";
        return new Expression(innerExpr);
    }
    
    private static ExprNode _parseExpression(TokenStream stream) {
        return _parseExpression(stream, 0);
    }
    
    /**
     * TODO
     * @param minimumBP
     * @return 
     */
    private static ExprNode _parseExpression(TokenStream stream, int minimumBP) {
        // NOTE: LHS = left hand side, RHS = right hand side.
        ExprNode lhs = null;
        ExprNode rhs;
        Token lhs_token = stream.next();
        boolean lhs_preceded_by_minus = lhs_token.is("Operator") && lhs_token.getValue().equals("-");
        if (lhs_preceded_by_minus)
            lhs_token = stream.next();
        
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
        else
            throw new Error("unacceptable LHS token: " + lhs_token);
        
        // HACK: if the LHS was preceded by a minus sign, multiply the LHS by
        // -1 to emulate an infix negative operator.
        if (lhs_preceded_by_minus && (lhs instanceof Variable || lhs instanceof Group))
            lhs = new Operation(new Number(-1), "*", lhs);

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
