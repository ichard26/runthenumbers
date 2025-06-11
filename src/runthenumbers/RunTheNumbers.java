package runthenumbers;

import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import runthenumbers.gui.CalculatorGUI;
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
- [x] Complete simplification barebones
- [] Extend simplifier to support distribution, multiply folding, and collection
     of variable terms
- [x] Implement basic linear solver
- [] Sketch out GUI
- [] Extend unit tests
- [] Add logging to math package
- [] Fix infix minus precedence with powers
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
        TestSuite.runSelfCheck();
        runApp();

        String input;
        while (!(input = new StringPrompt(">>>").ask()).isBlank()) {
            RootNode result = Parser.parse(input);
            if (result instanceof Expression expr) {
                new PrettyPrinter().print(result);
                System.out.println("Result: " + Evaluator.evaluate(expr));
            }
            else if (result instanceof Equation eqn) {
                Equation original = eqn.deepcopy();
                new PrettyPrinter().print(result);
                Simplifier.simplify(eqn);
                new PrettyPrinter().print(result);
                if (!original.equals(eqn))
                    System.out.println("Simplified: " + result.toString());

                try {
                    double r = new LinearSolver().solve(eqn);
                    System.out.println("Solution: " + r);
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }
            }
            System.out.println();
        }
    }

    public static void runApp() {
        JFrame frame = new JFrame("RunTheNumbers");
        frame.setLayout(new FlowLayout());
        JPanel calculatorPanel = CalculatorGUI.constructPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(calculatorPanel);
        frame.pack();
        frame.setLocation(200, 200);
        frame.setVisible(true);
        calculatorPanel.requestFocusInWindow();
    }
}
