package runthenumbers.math.solve;

import java.util.ArrayList;
import java.util.List;
import runthenumbers.math.Evaluator;
import runthenumbers.math.ast.Equation;
import runthenumbers.math.ast.Expression;
import runthenumbers.math.ast.Group;
import runthenumbers.math.ast.Number;
import runthenumbers.math.ast.Operation;
import runthenumbers.math.ast.ParseResult;
import runthenumbers.utils.PrettyPrinter;

/**
 * TODO
 * June 2, 2025
 * @author Richard Si
 */
public class Simplifier {
    public static void simplify(ParseResult exprOrEqn) {
        switch (exprOrEqn) {
            case Equation eqn -> simplify(eqn);
            case Expression eqn -> simplify(eqn);
        }
    }
    
    public static void simplify(Equation eqn) {
        simplify(eqn.getLeft());
        simplify(eqn.getRight());
    }
    
    public static void simplify(Expression expr) {
        foldConstantExpr(expr);
    }

    private static void foldConstantExpr(Expression expr) {
        // Walk expression AST recursively for operations that contains
        // constant operands. Matching operations are replaced with
        // number nodes.
        if (expr instanceof Operation op) {
            foldConstantExpr(op.getLeft());
            foldConstantExpr(op.getRight());
            if (op.getLeft() instanceof Number leftNum
                    && op.getRight() instanceof Number rightNum) {
                // TODO: figure out logging
                replaceNode(expr, new Number(Evaluator.evaluate(op)));
                // return new Number(Evaluator.evaluate(op));
            }
        }
        else if (expr instanceof Group group) {
            foldConstantExpr(group.getBody());
            // group.setBody(foldConstantExpr(group.getBody()));
            // Eliminate the group as it simplifies down to a constant.
            if (group.getBody() instanceof Number num)
                // return num;
                replaceNode(group, num);
        }
        
        collectLikeTerms(expr);
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
    
    private static void replaceNode(Expression original, Expression replacement) {
        ParseResult parent = original.getParent();
        
        replacement.setParent(parent);
        if (parent instanceof Group parentGroup)
            parentGroup.setBody(replacement);
        else if (parent instanceof Operation parentOp) {
            if (original == parentOp.getLeft())
                parentOp.setLeft(replacement);
            else {
                assert original == parentOp.getRight() : "is a child but not an operand?!";
                parentOp.setRight(replacement);
            }
        }
        else if (parent instanceof Equation parentEqn) {
            if (original == parentEqn.getLeft()) {
                parentEqn.setLeft(replacement);
            }
            else {
                assert original == parentEqn.getRight() : "is a child but not an operand?!";
                parentEqn.setRight(replacement);
            }
        }
        else {
            throw new AssertionError("Only group/operation can be parents.");
        }
    }
    
    private static void removeNode(Expression node) {
        ParseResult parent = node.getParent();
        if (parent instanceof Group group) {
            // These brackets will be empty, thus remove the group entirely.
            removeNode(group);
        }
        else if (parent instanceof Operation parentOp) {
            if (node == parentOp.getLeft()) {
                // Removing the left operand.
                if (parentOp.is("+")) {
                    replaceNode(parentOp, parentOp.getRight());
                }
                    
                return;
            }
            // Removing the right operand.
            assert node == parentOp.getRight() : "is a child but not an operand?!";
            if (parentOp.is("+")) {
                System.out.println("replacing (rhs) " + parentOp + " " + parentOp.getLeft());
                replaceNode(parentOp, parentOp.getLeft());
            }
            return;
        }
        
        throw new AssertionError("Only group/operation can be parents.");
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
            removeNode(constant);
            
        }
    }
    
    
}
