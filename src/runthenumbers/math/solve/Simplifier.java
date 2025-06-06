package runthenumbers.math.solve;

import java.util.ArrayList;
import runthenumbers.math.Evaluator;
import runthenumbers.math.ast.Equation;
import runthenumbers.math.ast.ExprNode;
import runthenumbers.math.ast.Group;
import runthenumbers.math.ast.Number;
import runthenumbers.math.ast.Operation;
import runthenumbers.math.ast.Node;
import runthenumbers.math.ast.Expression;
import runthenumbers.math.ast.ParentNode;
import runthenumbers.math.ast.RootNode;

record ConstantTerm(double value, Number node) {};

/**
 * TODO
 * June 2, 2025
 * @author Richard Si
 */
public class Simplifier {
    public static void simplify(RootNode exprOrEqn) {
        switch (exprOrEqn) {
            case Equation eqn -> {
                foldConstantExpr(eqn.getLeft());
                foldConstantExpr(eqn.getRight());
            }
            case Expression expr -> foldConstantExpr(expr.getBody());
        }
    }

    private static void foldConstantExpr(ExprNode expr) {
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
            }
        }
        else if (expr instanceof Group group) {
            foldConstantExpr(group.getBody());
            // Eliminate the group as it simplifies down to a constant.
            if (group.getBody() instanceof Number num)
                replaceNode(group, num);
        }
        
        collectLikeTerms(expr);
    } 
   
    private static ArrayList<Operation> findLikeTerms(ExprNode expr) {
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
    
    public static ConstantTerm getConstantFromOperation(Operation op) {
        ExprNode left = op.getLeft();
        ExprNode right = op.getRight();
        assert left instanceof Number || right instanceof Number;
        
        if (left instanceof Number leftNumber)
            return new ConstantTerm(leftNumber.getValue(), leftNumber);
        
        Number rightNumber = (Number)right;
        if (op.is("-"))
            // If the number is the right subtraction operand, it needs to be
            // treated as negative.
            return new ConstantTerm(-rightNumber.getValue(), rightNumber);
        
        return new ConstantTerm(rightNumber.getValue(), rightNumber);
    }
    
    public static void replaceNode(ExprNode original, ExprNode replacement) {
        ParentNode parent = original.getParent();
        // System.out.println("Removing " + original + " replacement: " + replacement);
        
        replacement.setParent(parent);
        if (parent instanceof Group parentGroup)
            parentGroup.setBody(replacement);
        else if (parent instanceof Operation parentOp) {
            if (original == parentOp.getLeft()) {
                parentOp.setLeft(replacement);
            }
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
        else if (parent instanceof Expression parentExpr) {
            parentExpr.setBody(replacement);
        }
        else {
            throw new AssertionError("Only group/operation can be parents.");
        }
    }
    
    public static void removeNode(ExprNode node) {
        ParentNode parent = node.getParent();
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
                else
                    throw new RuntimeException("not implemented yet");
                    
                return;
            }
            // Removing the right operand.
            assert node == parentOp.getRight() : "is a child but not an operand?!";
            if (parentOp.is("+", "-")) {
                replaceNode(parentOp, parentOp.getLeft());
            }
            else
                throw new RuntimeException("not implemented yet");
            return;
        }
        
        throw new RuntimeException("unexpected parent: " + parent);
    }
    
    private static void collectLikeTerms(ExprNode expr) {
        ArrayList<Operation> terms = findLikeTerms(expr);
        if (terms.isEmpty())
            return;
        
        ConstantTerm first = getConstantFromOperation(terms.removeFirst());
        for (Operation t : terms) {
            ConstantTerm constant = getConstantFromOperation(t);
            first.node().setValue(first.node().getValue() + constant.value());
            removeNode(constant.node());            
        }
    }
    
    
}
