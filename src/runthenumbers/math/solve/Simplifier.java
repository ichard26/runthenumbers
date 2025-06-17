package runthenumbers.math.solve;

import java.util.ArrayList;
import java.util.HashMap;
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
 * Class Name: Simplifier
 * Description: AST simplification passes and utilities.
 * Programmer: Richard Si
 * Date: June 2, 2025
 */
public class Simplifier {
    /**
     * Method Name: simplify
     * Description: AST simplification entrypoint. Simplifies an equation
     *              or expression as much as possible.
     * @param exprOrEqn The equation or expression to simplify.
     */
    public static void simplify(RootNode exprOrEqn) {
        switch (exprOrEqn) {
            case Equation eqn -> {
                simplifyExprNode(eqn.getLeft());
                simplifyExprNode(eqn.getRight());
            }
            case Expression expr -> simplifyExprNode(expr.getBody());
        }
    }

    /**
     * Method Name: getConstantFromOperation
     * Description: Return the constant (number) operand of an operation.
     *               Raises an error if there is no constant operand.
     * @param op The operation to inspect.
     * @return A description of the constant operand: its value, its position
     *         and the underlying number node.
     */
    public static ConstantOperand getConstantFromOperation(Operation op) {
        if (op.getLeft() instanceof Number number)
            return new ConstantOperand(number.getValue(), false, number);
        if (op.getRight() instanceof Number number)
            return new ConstantOperand(number.getValue(), op.is("-"), number);

        throw new Error(op.toString() + " does not contain a constant LHS or RHS");
    }

    /**
     * Method Name: replaceNode
     * Description: Replace a child node in the AST with a new node.
     * @param original The child node to replace.
     * @param replacement The replacement node.
     */
    public static void replaceNode(ExprNode original, ExprNode replacement) {
        switch (original.getParent()) {
            case Expression parentExpr -> parentExpr.setBody(replacement);
            case Group parentGroup -> parentGroup.setBody(replacement);
            case Equation parentEqn -> parentEqn.replaceSide(original, replacement);
            case Operation parentOp -> parentOp.replaceOperand(original, replacement);
        }
    }

    /**
     * Method Name: removeNode
     * Description: Remove a node from the AST outright, modifying it as if
     *               it never existed.
     * @param node The child node to remove.
     */
    public static void removeNode(ExprNode node) {
        ParentNode parent = node.getParent();

        switch (parent) {
            // We're about to remove everything from (one side of) an equation
            // or an expression, replace it with zero.
            case Equation unused -> replaceNode(node, new Number(0));
            case Expression unused -> replaceNode(node, new Number(0));
            // The brackets will be empty, thus remove the group outright.
            case Group group -> removeNode(group);
            // Removing one operand. Care needs to be taken to preserve semantics.
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
                        throw new Error("Removing left operand of division is unsupported");

                    return;
                }

                // The right operand can be removed without additional adjustments.
                replaceNode(parentOp, parentOp.getLeft());
            }
        }
    }

    /**
     * Method Name: simplifyExprNode
     * Description: Simplification implementation entrypoint. Constant
     *               expressions, like terms, and other unnecessary nodes are
     *               combined and pruned.
     * @param expr The expression child node to start from.
     */
    private static void simplifyExprNode(ExprNode expr) {
        // Walk expression AST recursively for operations that contains
        // constant operands. Matching operations are replaced with
        // number nodes.
        if (expr instanceof Operation op) {
            simplifyExprNode(op.getLeft());
            simplifyExprNode(op.getRight());
            if (op.getLeft() instanceof Number leftNum
                    && op.getRight() instanceof Number rightNum) {
                // TODO: figure out logging
                replaceNode(expr, new Number(Evaluator.evaluate(op, new HashMap())));
            }
        }
        else if (expr instanceof Group group) {
            simplifyExprNode(group.getBody());
            // Eliminate the group as it simplifies down to a constant.
            if (group.getBody() instanceof Number num)
                replaceNode(group, num);
        }

        collectLikeTerms(expr);
    }

    /**
     * Method Name: findLikeTerms
     * Description: Find like terms that can be combined. Does not recurse into
     *               parenthesized sub-expressions.
     * @param expr The expression to start from.
     * @return The operations containing like terms that can be combined.
     */
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

    /**
     * Method Name: collectLikeTerms
     * Description: Combine like terms in an expression. Does not recurse into
     *               parenthesized sub-expressions.
     * @param expr The expression to start from.
     */
    private static void collectLikeTerms(ExprNode expr) {
        ArrayList<Operation> terms = findLikeTerms(expr);
        if (terms.size() < 2)
            return;

        // There are >=2 like terms, keep the first time and combine the rest
        // into the first term.
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
