package runthenumbers;

import java.awt.FlowLayout;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JPanel;
import runthenumbers.gui.CalculationHistoryGUI;
import runthenumbers.gui.CalculatorGUI;
import runthenumbers.math.TestSuite;
import runthenumbers.utils.PrettyPrinter;
import runthenumbers.math.Evaluator;
import runthenumbers.math.ast.Equation;
import runthenumbers.math.ast.Parser;
import runthenumbers.math.solve.Simplifier;
import runthenumbers.math.ast.Expression;
import runthenumbers.math.ast.RootNode;
import runthenumbers.math.solve.InverseSolver;

/*
[TODO LIST]

- [x] Port over calculator math implementation
- [x] Extend math impl. to handle variables and equations
- [x] Extend tokenizer and parser with fix-ups and error handling
- [x] DOCUMENTATION (class, methods) & COMMENT CHECKPOINT
- [x] Complete simplification barebones
- [] Extend simplifier to support distribution, multiply folding, and collection
     of variable terms
- [x] Implement basic linear solver
- [x] Sketch out GUI
- [] Extend unit tests
- [] Add logging to math package
- [] Fix infix minus precedence with powers
*/

/**
 * Class Name: RunTheNumbers
 * Description: Program entrypoint.
 * Programmer: Richard Si
 * Date: May 29, 2025.
 */
public class RunTheNumbers {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Run test suite before starting the application.
        TestSuite.runSelfCheck();
        runApp();

        // This is debug code that allows quick interaction with the math
        // portion of the project. I'm not commenting this.
        Scanner scanS = new Scanner(System.in);
        while (true) {
            System.out.print(">>> ");
            String input = scanS.nextLine();
            if (input.isBlank())
                break;

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
                    double r = new InverseSolver().solve(eqn).getFirst();
                    System.out.println("Solution: " + r);
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }
            }
            System.out.println();
        }
    }

    /**
     * Method Name: runApp
     * Description: Calculator GUI entrypoint.
     */
    public static void runApp() {
        JFrame frame = new JFrame("RunTheNumbers");
        frame.setLayout(new FlowLayout());

        // Construct the calculator panel.
        JPanel calculatorPanel = CalculatorGUI.constructPanel();
        frame.add(calculatorPanel);

        // Construct the history panel and link the calculator GUI to it.
        CalculationHistoryGUI history = new CalculationHistoryGUI();
        CalculatorGUI.setOnCalculation((e) -> {
            history.addEntry(e);
            frame.pack();
            calculatorPanel.requestFocusInWindow();
        });
        frame.add(history.constructPanel());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocation(200, 200);
        frame.setVisible(true);
        calculatorPanel.requestFocusInWindow();
    }
}
