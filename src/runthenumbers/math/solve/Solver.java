package runthenumbers.math.solve;

import java.util.List;
import runthenumbers.math.ast.Equation;

/**
 * Interface Name: Solver
 * Description: Common methods of equation solvers.
 * Programmer: Richard Si
 * Date: June 2, 2025.
 */
public interface Solver {
    /**
     * Method Name: canSolve
     * Description: Determine if this equation can (likely) be solved by
     *               this solver.
     * @param eqn The equation to check.
     * @return True if the equation can likely be solved and thus it can be
     *          attempted. False otherwise.
     */
    public abstract boolean canSolve(Equation eqn);

    /**
     * Method Name: solve
     * Description: Solve the equation and return the solutions.
     * @param eqn The equation to solve.
     * @return The solutions, if there are any.
     */
    public abstract List<Double> solve(Equation eqn);
}
