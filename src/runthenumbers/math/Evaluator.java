package runthenumbers.math;

import runthenumbers.math.ast.Expression;
import runthenumbers.math.ast.Group;
import runthenumbers.math.ast.Operation;
import runthenumbers.math.ast.Number;

/**
 * TODO
 * @date May 30, 2025
 * @author Richard Si
 */
public class Evaluator {
    /**
     * TODO
     * @param expr
     * @return 
     */
    public static double evaluate(Expression expr) {
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
        
        throw new AssertionError("unacceptable expression: " + expr);
    }
}
