package runthenumbers.math;

import java.util.HashMap;
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
        return evaluate(expr, new HashMap());
    }

    public static double evaluate(Expression expr, HashMap<String, Double> variables) {
        return evaluate(expr.getBody(), variables);
    }

    /**
     * Method Name: evaluate
     * Description: Evaluate a math expression and return final number.
     * @param expr The math expression to evaluate.
     * @param variables Mapping of variable values to substitute in.
     * @return The expression's answer.
     */
    public static double evaluate(ExprNode expr, HashMap<String, Double> variables) {
        if (expr instanceof Operation op) {
            double left = evaluate(op.getLeft(), variables);
            double right = evaluate(op.getRight(), variables);
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
            return evaluate(group.getBody(), variables);
        else if (expr instanceof Number number)
            return number.getValue();
        else if (expr instanceof Variable var)
            return variables.getOrDefault(var.getName(), 0.0);

        throw new AssertionError("should be impossible");
    }
}
