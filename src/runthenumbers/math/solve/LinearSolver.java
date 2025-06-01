package runthenumbers.math.solve;

import runthenumbers.math.ast.Equation;

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
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
