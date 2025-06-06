package runthenumbers;

import runthenumbers.math.TestSuite;
import runthenumbers.utils.PrettyPrinter;
import runthenumbers.utils.prompt.StringPrompt;
import runthenumbers.math.Evaluator;
import runthenumbers.math.ast.Equation;
import runthenumbers.math.ast.Parser;
import runthenumbers.math.solve.Simplifier;
import runthenumbers.math.ast.Expression;
import runthenumbers.math.ast.RootNode;
import runthenumbers.math.solve.LinearSolver;

/*
[TODO LIST]

- [x] Port over calculator math implementation
- [x] Extend math impl. to handle variables and equations
- [x] Extend tokenizer and parser with fix-ups and error handling
- [] DOCUMENTATION (class, methods) & COMMENT CHECKPOINT
- [] Complete simplification barebones
- [] Extend simplifier to support distribution, multiply folding, and collection
     of variable terms
- [] Implement basic linear solver
- [] Sketch out GUI
- [] Extend unit tests
- [] Add logging to math package
*/

/**
 *
 * @author Richard Si
 */
public class RunTheNumbers {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Run test suite before starting the application.
        boolean passed = TestSuite.runSelfCheck();
        if (!passed && false) {
            System.out.println("[ERROR] self-check failed, aborting...");
            System.exit(1);
        }
        
//        RootNode ast = Parser.parse("7 + 3*2");
//        new PrettyPrinter().print(ast);
//        Simplifier.simplify(ast);
//        new PrettyPrinter().print(ast);
//        System.out.println(ast);
        
        String input;
        while (!(input = new StringPrompt(">>>").ask()).isBlank()) {
            RootNode result = Parser.parse(input);
            if (result instanceof Expression expr) {
                new PrettyPrinter().print(result);
                Simplifier.simplify(expr);
                // new PrettyPrinter().print(expr);
                System.out.println("Result: " + Evaluator.evaluate(expr));
            }
            else if (result instanceof Equation eqn) {
                // new PrettyPrinter().print(result);
                Simplifier.simplify((Equation)result);
                // new PrettyPrinter().print(result);
                System.out.println("Simplified: " + result.toString());
                
                try {
                    double r = new LinearSolver().solve(eqn);
                    System.out.println("Solution: " + r);
                } catch (Exception e) {
                    
                }
            }
            System.out.println();
        }

    }
    
}
