package runthenumbers.math.solve;

import java.util.ArrayList;
import runthenumbers.math.Evaluator;
import runthenumbers.math.ast.Equation;
import runthenumbers.math.ast.Expression;
import runthenumbers.math.ast.Group;
import runthenumbers.math.ast.Number;
import runthenumbers.math.ast.Operation;
import runthenumbers.math.ast.ParseResult;

/**
 * TODO
 * June 2, 2025
 * @author Richard Si
 */
public class Simplifier {
    public static ParseResult simplify(ParseResult exprOrEqn) {
        if (exprOrEqn instanceof Equation eqn)
            return simplify(eqn);
        
        assert exprOrEqn instanceof Expression;
        return simplify((Expression)exprOrEqn);
    }
    
    public static Equation simplify(Equation eqn) {
        eqn.setLeft(simplify(eqn.getLeft()));
        eqn.setRight(simplify(eqn.getRight()));
        return eqn;
    }
    
    public static Expression simplify(Expression expr) {
        return foldConstantExpr(expr);
    }

    private static Expression foldConstantExpr(Expression expr) {
        // Walk expression AST recursively for operations that contains
        // constant operands. Matching operations are replaced with
        // number nodes.
        if (expr instanceof Operation op) {
            op.setLeft(foldConstantExpr(op.getLeft()));
            op.setRight(foldConstantExpr(op.getRight()));
            if (op.getLeft() instanceof Number leftNum
                    && op.getRight() instanceof Number rightNum)
                // TODO: figure out logging
                return new Number(Evaluator.evaluate(op));
        }
        else if (expr instanceof Group group) {
            group.setBody(foldConstantExpr(group.getBody()));
            // Eliminate the group as it simplifies down to a constant.
            if (group.getBody() instanceof Number num)
                return num;
        }
        
        collectLikeTerms(expr);
        return expr;
    }
    
    private static ArrayList<Operation> findLikeTerms(Expression expr) {
        ArrayList<Operation> addMinusTerms = new ArrayList<>();
        // Addition/subtraction are associative, thus we can collect and
        // fold them into a single term. 
        if (expr instanceof Operation op && op.is("+", "-")) {
            addMinusTerms.addAll(findLikeTerms(op.getLeft()));
            addMinusTerms.addAll(findLikeTerms(op.getRight()));
            if (op.getLeft() instanceof Number || op.getRight() instanceof Number)
                addMinusTerms.add(op);
        }
        else if (expr instanceof Group)
            return new ArrayList<>();
        
        return addMinusTerms;
    }
    
    private static Number getConstantFromOperation(Operation op) {
        Expression left = op.getLeft();
        Expression right = op.getRight();
        if (left instanceof Number leftNumber)
            return leftNumber;
        
        assert right instanceof Number;
        Number rightNumber = (Number)right;
        if (op.is("-")) {
            rightNumber.setValue(-rightNumber.getValue());
            op.setOperator("+");
        }
        return rightNumber;
    }
    
    
    private static void collectLikeTerms(Expression expr) {
        ArrayList<Operation> terms = findLikeTerms(expr);
        if (terms.isEmpty())
            return;
        
        Number first = getConstantFromOperation(terms.removeFirst());
        for (Operation t : terms) {
            Number constant = getConstantFromOperation(t);
            first.setValue(first.getValue() + constant.getValue());
            constant.setValue(0);
        }
    }
    
    
}
