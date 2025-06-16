package runthenumbers.math.ast;

import runthenumbers.math.tokenize.Token;
import runthenumbers.math.tokenize.TokenStream;
import runthenumbers.math.tokenize.Tokenizer;

/**
 * Record Name: BindingPower
 * Description: Utility tuple for storing the "binding powers" (used for
 *               precedence) of operators.
 */
record BindingPower(int left, int right) {};

/**
 * Class Name: Parser
 * Description: Process tokens and convert them into an Abstract Syntax Tree
 *              (AST) that faithfully represents a math expression/equation
 *              with BEDMAS support. The AST can be further manipulated and
 *              inspected to simplify, evaluate, and solve.
 * Programmer: Richard Si
 * Date: May 25, 2025
 */
public class Parser {
    /**
     * Method Name: parse
     * Description: Convenience method to parse a string directly.
     * @param input The input string.
     * @return The math AST.
     */
    public static RootNode parse(String input) {
        return parse(Tokenizer.tokenize(input));
    }

    /**
     * Method Name: parse
     * Description: Parse an stream of tokens into a math AST.
     * @param stream The token stream provided by the tokenizer.
     * @return The math AST.
     */
    public static RootNode parse(TokenStream stream) {
        // TODO: flag expressions with variables
        TokenStream[] substreams = stream.split("EqualSign");
        // If there's an equal sign, parse each side of the eqn separately.
        if (substreams.length > 1) {
            assert substreams.length == 2 : "there should only be a left and right side";
            ExprNode leftExpr = parseExpression(substreams[0]);
            ExprNode rightExpr = parseExpression(substreams[1]);
            return new Equation(leftExpr, rightExpr);
        }

        ExprNode innerExpr = parseExpression(stream);
        assert stream.isExhausted() : "did not consume all tokens";
        return new Expression(innerExpr);
    }

    /**
     * Method Name: parseExpression
     * Description: Thin wrapper over parseExpression that stipulates no
     *               minimum binding power requirement for parsing.
     * @param stream
     * @return
     */
    private static ExprNode parseExpression(TokenStream stream) {
        return parseExpression(stream, 0);
    }

    /**
     * Method Name: parseExpression
     * Description: The core parsing logic for parsing a math expression,
     *               returning an operation or the LHS of an operation.
     * @param minimumBP The minimum operator binding power to construct a new
     *                   operation (used to handle BEDMAS).
     * @return The math expression AST.
     */
    private static ExprNode parseExpression(TokenStream stream, int minimumBP) {
        // NOTE: LHS = left hand side, RHS = right hand side.
        ExprNode lhs = null;
        ExprNode rhs;
        Token lhs_token = stream.next();

        // Check if the LHS is going to be preceded by a minus sign, then treat it
        // as an unary negative operator. We can't do anything until we've parsed
        // the LHS though, so just keep track of this and continue to the next token.
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
            lhs = new Group(parseExpression(stream));
            if (!stream.next().is("RightBracket"))
                throw new AssertionError("next token should be a closing bracket");
        }
        else
            throw new Error("unacceptable LHS token: " + lhs_token);

        // HACK: if the LHS was preceded by a minus sign, multiply the LHS by
        // -1 to emulate an infix negative operator.
        if (lhs_preceded_by_minus && (lhs instanceof Variable || lhs instanceof Group))
            lhs = new Operation(new Number(-1), "*", lhs);

        while (!stream.isExhausted()) {
            Token op_token = stream.peek();
            if (op_token.is("RightBracket"))
                // The sub-expression is finished so return the LHS now.
                break;

            // Otherwise, this token is an operator. Check it and decide
            // whether to return the LHS now or construct a new operation.
            BindingPower bp = infixBindingPower(op_token.getValue());
            if (bp.left() < minimumBP)
                break;

            // We've decided to parse an entire operation, move past the
            // operator token and parse the RHS in a recursive call.
            stream.next();
            rhs = parseExpression(stream, bp.right());
            lhs = new Operation(lhs, op_token.getValue(), rhs);
        }

        return lhs;
    }

    /**
     * Method Name: infixBindingPower
     * Description: Return the binding powers of an operator.
     * @param op The operator.
     * @return The operator's binding power to its (left, right) operands.
     */
    private static BindingPower infixBindingPower(String op) {
        return switch (op) {
            case "+", "-" -> new BindingPower(1, 2);
            case "*", "/" -> new BindingPower(3, 4);
            case "^" -> new BindingPower(6, 5);
            default -> throw new AssertionError("unexpected operator: " + op);
        };
    }
}
