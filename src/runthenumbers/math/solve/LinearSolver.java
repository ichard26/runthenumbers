package runthenumbers.math.solve;

import runthenumbers.math.ast.Equation;
import runthenumbers.math.ast.ExprNode;
import runthenumbers.math.ast.Number;
import runthenumbers.math.ast.Operation;
import runthenumbers.math.ast.Variable;

/**
 * TODO
 * @author Richard Si
 */
public class LinearSolver implements Solver {

    @Override
    public boolean canSolve(Equation eqn) {
        // TODO: actually check the equation...
        return true;
    }

    @Override
    public double solve(Equation eqn) {
        assert eqn.getRight() instanceof Number;

        while (!(eqn.getLeft() instanceof Variable)) {
            ExprNode left = eqn.getLeft();
            Number right = (Number)eqn.getRight();
            assert left instanceof Operation;

            // Inspect the left outermost operation and perform the inverse
            // operation on the right side (number).
            Operation op = (Operation)left;
            ConstantOperand constant = Simplifier.getConstantFromOperation(op);
            if (op.is("+") || op.is("-") && !constant.isRightOfMinus()) {
                // Addition: apply subtraction.
                right.setValue(right.getValue() - constant.value());
            }
            else if (op.is("-")) {
                // Subtraction: apply addition.
                right.setValue(right.getValue() + constant.value());
            }
            else if (op.is("*")) {
                // Multiplication: apply division.
                right.setValue(right.getValue() / constant.value());
            }
            else if (op.is("/") && constant.node() == op.getRight()) {
                // Division: apply multiplication.
                right.setValue(right.getValue() * constant.value());
            }
            else if (op.is("^") && constant.node() == op.getRight()) {
                // Exponentiation: apply square root/cubic root/etc.
                right.setValue(Math.pow(right.getValue(), 1.0 / constant.value()));
            }

            // Once the inverse operation has been performed, remove the constant
            // from the left tree.
            Simplifier.removeNode(constant.node());
        }

        ExprNode right = eqn.getRight();
        return ((Number)right).getValue();
    }

}
