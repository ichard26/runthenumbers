package runthenumbers.math.solve;

import runthenumbers.math.ast.Equation;

/**
 * TODO
 * @author Richard Si
 */
public interface Solver {
    public abstract boolean canSolve(Equation eqn);
    public abstract double solve(Equation eqn);
}
