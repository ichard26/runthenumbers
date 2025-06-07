package runthenumbers.math.solve;

import java.util.ArrayList;
import runthenumbers.math.Evaluator;
import runthenumbers.math.ast.Equation;
import runthenumbers.math.ast.ExprNode;
import runthenumbers.math.ast.Group;
import runthenumbers.math.ast.Number;
import runthenumbers.math.ast.Operation;
import runthenumbers.math.ast.Expression;
import runthenumbers.math.ast.ParentNode;
import runthenumbers.math.ast.RootNode;

record ConstantOperand(double value, boolean isRightOfMinus, Number node) {};

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

    public static ConstantOperand getConstantFromOperation(Operation op) {
        if (op.getLeft() instanceof Number number)
            return new ConstantOperand(number.getValue(), false, number);
        if (op.getRight() instanceof Number number)
            return new ConstantOperand(number.getValue(), op.is("-"), number);

        throw new Error(op.toString() + " does not contain a constant LHS or RHS");
    }

    public static void replaceNode(ExprNode original, ExprNode replacement) {
        // System.out.println("Removing " + original + " replacement: " + replacement);
        switch (original.getParent()) {
            case Expression parentExpr -> parentExpr.setBody(replacement);
            case Group parentGroup -> parentGroup.setBody(replacement);
            case Equation parentEqn -> parentEqn.replaceSide(original, replacement);
            case Operation parentOp -> parentOp.replaceOperand(original, replacement);
        }
    }

    public static void removeNode(ExprNode node) {
        ParentNode parent = node.getParent();

        switch (parent) {
            case Equation unused -> replaceNode(node, new Number(0));
            case Expression unused -> replaceNode(node, new Number(0));
            case Group group -> // The brackets will be empty, thus remove the group outright.
                removeNode(group);
            case Operation parentOp -> {
                assert node == parentOp.getLeft() || node == parentOp.getRight()
                        : "is a child but not an operand?!";
                if (node == parentOp.getLeft()) {
                    // Removing the left operand is a bit involved depending on
                    // the operator.
                    if (parentOp.is("+", "*"))
                        // Addition and multiplication are associative so order
                        // doesn't matter.
                        replaceNode(parentOp, parentOp.getRight());
                    else if (parentOp.is("-")) {
                        // The minus sign needs be preserved so multiply by -1.
                        replaceNode(node, new Number(-1));
                        parentOp.setOperator("*");
                    }
                    else
                        throw new Error("Removing LHS of division is unsupported");

                    return;
                }

                // The right operand can be removed without additional adjustments.
                replaceNode(parentOp, parentOp.getLeft());
            }
        }
    }

    private static void collectLikeTerms(ExprNode expr) {
        ArrayList<Operation> terms = findLikeTerms(expr);
        if (terms.size() < 2)
            return;

        Number firstTerm = getConstantFromOperation(terms.removeFirst()).node();
        for (Operation t : terms) {
            ConstantOperand constant = getConstantFromOperation(t);
            // If the number is to the right of a minus sign, it needs to be
            // treated as negative.
            if (constant.isRightOfMinus())
                firstTerm.setValue(firstTerm.getValue() - constant.value());
            else
                firstTerm.setValue(firstTerm.getValue() + constant.value());
            removeNode(constant.node());
        }
    }

}
