package runthenumbers;

import runthenumbers.math.Evaluator;
import runthenumbers.math.ast.Expression;
import runthenumbers.math.ast.Parser;
import runthenumbers.math.tokenize.TokenStream;
import runthenumbers.math.tokenize.Tokenizer;

/**
 * TODO
 * @author Richard Si
 */
public class TestSuite {
    private static int passingCases;
    private static int failingCases;
    
    /**
     * TODO
     * @return 
     */
    public static boolean run() {
        testBasicEvaluation();
        testFixups();
        System.out.printf("[self-check] %d passed, %d failed\n", passingCases, failingCases);
        return failingCases == 0;
    }
    
    /**
     * TODO
     */
    public static void testBasicEvaluation() {
        // Check basic operations.
        assertEvaluate("5 + 2", 7);
        assertEvaluate("5 - 2", 3);
        assertEvaluate("5 * 4", 20);
        assertEvaluate("5 / 2", 2.5);
        assertEvaluate("5^2", 25);
        // Check mixed math.
        assertEvaluate("7 + 3*2", 13);
        assertEvaluate("(1 + 2 + 3) * (-1 - 2 - 3)", -36);
        assertEvaluate("2 + 10^3 - 3", 999);
        assertEvaluate("2^2^2", 16);
        assertEvaluate("1 + 2 * 3 - 4 / 5^2", 6.84);
    }
    
    public static void testFixups() {
        assertEvaluate("1-3", -2);
        assertEvaluate("5-1-1-1-1", 1);
        assertEvaluate("(5)(2)", 10);
        // TODO test Nx
    }
    
    /**
     * TODO
     * @param input
     * @param expected 
     */
    private static void assertEvaluate(String input, double expected) {
        double result = -1000000;
        Throwable error = null;
        try {
            result = evaluate(input);
        } catch (Error | RuntimeException e) {
            error = e;
        }
        if (error != null) {
            // An error was raised == automatic fail.
            System.out.printf("""
                              [ERROR] evaluation
                                - Input: %s
                              """, 
                    input);
            error.printStackTrace(System.out);
            failingCases++;
        }
        else if (result != expected) {
            // No error was raised, but the result is wrong.
            System.out.printf("""
                              [ERROR] evaluation
                                - Input: %s
                                - Expected: %s, Actual: %s
                              """, 
                    input, Double.toString(expected), Double.toString(result));
            failingCases++;
        }
        else {
            passingCases++;
        }
    }
    
    /**
     * TODO
     * @param input
     * @return 
     */
    private static double evaluate(String input) {
        TokenStream stream = new Tokenizer().tokenize(input);
        Expression expr = Parser.parse(stream);
        return Evaluator.evaluate(expr);
    }
}
