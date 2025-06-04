package runthenumbers.math.solve;

import runthenumbers.math.ast.Equation;
import runthenumbers.math.ast.Expression;
import runthenumbers.math.ast.Number;

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
        Expression left = eqn.getLeft();
        Number right = (Number)eqn.getRight();
        return right.getValue();
    }
    
}
