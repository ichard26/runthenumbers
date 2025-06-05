package runthenumbers.math;

import runthenumbers.math.ast.Expression;
import runthenumbers.math.ast.ExprNode;
import runthenumbers.math.ast.Group;
import runthenumbers.math.ast.Operation;
import runthenumbers.math.ast.Number;
import runthenumbers.math.ast.Variable;

/**
 * Class Name: Evaluator
 * Description: Evaluates math expressions.
 * Programmer: Richard Si
 * Date: May 30, 2025
 */
public class Evaluator {
    public static double evaluate(Expression expr) {
        return evaluate(expr.getBody());
    }
    
    /**
     * Method Name: evaluate
     * Description: Evaluate a math expression and return final number.
     * @param expr The math expression to evaluate.
     * @return The expression's answer.
     */
    public static double evaluate(ExprNode expr) {
        if (expr instanceof Operation op) {
            double left = evaluate(op.getLeft());
            double right = evaluate(op.getRight());
            return switch (op.getOperator()) {
                case "+" -> left + right;
                case "-" -> left - right;
                case "*" -> left * right;
                case "/" -> left / right;
                case "^" -> Math.pow(left, right);
                default -> throw new AssertionError("unknown operator: " + op.getOperator());
            };
        }
        else if (expr instanceof Group group)
            return evaluate(group.getBody());
        else if (expr instanceof Number number)
            return number.getValue();
        
        assert expr instanceof Variable;
        throw new RuntimeException("cannot evaluate expression with a variable");
    }
}
