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
    
    /*
    def solve(eqn: Equation):
    # This assumes the variable term will always be on the left side.
    while (eqn.left is not Variable):
        # Apply inverse operation
        eqn.left
        if eqn.left.operator is ADD or SUBTRACT:
            inverseValue = getConstantValue(eqn.left)
            eqn.right += inverseValue
        elif eqn.left.operator is MULTIPLY:
            inverseValue = getConstantValue(eqn.left)
            eqn.right /= inverseValue
        elif eqn.left.operator is DIVIDE:
            # Assume that we aren't dividing by a variable.
            if (eqn.left.right is Variable): raise Error()
            inverseValue = eqn.left.right
            eqn.right *= inverseValue

    # The variable is isolated on the left, we're done! 🎉
    return eqn.right
    */

    @Override
    public double solve(Equation eqn) {
        assert eqn.getRight() instanceof Number;
        
        while (!(eqn.getLeft() instanceof Variable)) {
            ExprNode left = eqn.getLeft();
            Number right = (Number)eqn.getRight();
            assert left instanceof Operation;
            Operation leftOp = (Operation)left;
            
            if (leftOp.is("+", "-")) {
                ConstantOperand term = Simplifier.getConstantFromOperation(leftOp);
                double inverseValue = term.isRightOfMinus() ? term.value() : -term.value(); 
                right.setValue(right.getValue() + inverseValue);
                Simplifier.removeNode(term.node());
            }
            else if (leftOp.is("*")) {
                ConstantOperand term = Simplifier.getConstantFromOperation(leftOp);
                right.setValue(right.getValue() / term.value());
                Simplifier.removeNode(term.node());
            }
            else {
                if (leftOp.getRight() instanceof Number divisor) {
                    right.setValue(right.getValue() * divisor.getValue());
                    Simplifier.removeNode(divisor);
                }
                else {
                    throw new Error("not implemented");
                }
            }
        }
        
        ExprNode right = eqn.getRight();
        return ((Number)right).getValue();
    }
    
}
