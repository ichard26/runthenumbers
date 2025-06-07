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
            Operation leftOp = (Operation)left;
            
            ConstantOperand constant = Simplifier.getConstantFromOperation(leftOp);
            if (leftOp.is("+") || leftOp.is("-") && !constant.isRightOfMinus()){
                right.setValue(right.getValue() - constant.value());
            }
            else if (leftOp.is("-")) {
                right.setValue(right.getValue() + constant.value());   
            }
            else if (leftOp.is("*")) {
                right.setValue(right.getValue() / constant.value());
            }
            else if (leftOp.is("/") && constant.node() == leftOp.getRight()){
                right.setValue(right.getValue() * constant.value());
            }
            else
                throw new Error("not implemented");
            Simplifier.removeNode(constant.node());
        }
        
        ExprNode right = eqn.getRight();
        return ((Number)right).getValue();
    }
    
}
