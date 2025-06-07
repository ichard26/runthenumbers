package runthenumbers.math;

import runthenumbers.math.ast.Equation;
import runthenumbers.math.ast.Parser;
import runthenumbers.math.solve.Simplifier;
import runthenumbers.math.ast.Expression;
import runthenumbers.math.ast.RootNode;
import runthenumbers.math.solve.LinearSolver;

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
    public static boolean runSelfCheck() {
        log("testing evaluation");
        testBasicEvaluation();
        log("testing fix-ups");
        testFixups();
        log("testing simplification");
        testSimplification();
        log("testing linear solver");
        testLinearSolver();
        log("verdict: %d passed, %d failed", passingCases, failingCases);
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
        assertEvaluate("5(3)", 15);
        assertSimplify("5x", "5 * x");
        assertSimplify("x-5", "x - 5");
    }
    
    public static void testSimplification() {
        // Without variables. Constant folding will simplify these down to a number.
        assertSimplify("1-3", "-2");
        assertSimplify("7 + 3*2", "13");
        assertSimplify("(1 + 2 + 3) * (-1 - 2 - 3)", "-36");
        assertSimplify("2 + 10^3 - 3", "999");
        assertSimplify("2^2^2", "16");
        assertSimplify("1 + 2 * 3 - 4 / 5^2", "6.84");
        assertSimplify("(((((5)))))", "5");
        // With variables, with add/subtract.
        assertSimplify("x - 3", "x - 3");
        assertSimplify("5x + 2*3", "5x + 6");
        assertSimplify("x + 5(2*4)", "x + 40");
        assertSimplify("5x + 5 + 5", "5x + 10");
        // With variables, with multiply/divide.
        assertSimplify("10+20 - 5x + 3*5 = 0", "45 - 5x = 0");
//        assertSimplify("5x * 3 * 3", "45x");
//        assertSimplify("10 * x * 10", "100x");
        // With variables, with brackets.
        assertSimplify("x + (5 + 2)", "x + 7");
        assertSimplify("x + (5 + 2) * 2", "x + 14");
        assertSimplify("10 + x + (5 + 2) * 2", "24 + x");
        assertSimplify("(x +3-2) + (2*3x)", "(x + 1) + (6x)");
//        assertSimplify("10 + x + (2x)*2", "10 + 5x");
    }
    
    public static void testSimplificationEquation() {
        assertSimplify("5 + 4 = x", "9 = x");
        assertSimplify("1*2*3 = 3*2*1", "6 = 6");
        assertSimplify("1 + ((x)) + 3 = 10^2", "4 + x = 100");
    }
    
    
    public static void testLinearSolver() {
        assertSolve("x = 0", 0);
        assertSolve("x + 3 = 0", -3);
        assertSolve("x - 3 = 0", 3);
        assertSolve("x * 3 = 27", 9);
        assertSolve("x / 3 = 3", 9);
        assertSolve("x + 4 - 4 + 4 - 4 = 5", 5);
        assertSolve("5 + x = 0", -5);
        assertSolve("5 - x = 0", 5);
        assertSolve("-x + 5 = 0", 5);
        // More complex LHS.
        assertSolve("-5x = 25", -5);
        assertSolve("20*10 + x + 50*2.5 = 0", -325);
        assertSolve("5^2 + x*2/10 - 200 = 10", 925);
        assertSolve("5*4*3*x*2*1 + 40 = 100", 0.5);
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
    
    private static void assertSimplify(String input, String expected) {
        RootNode actualResult = Parser.parse(input);
        RootNode expectedResult = Parser.parse(expected);
        Simplifier.simplify(actualResult);
        if (!actualResult.equals(expectedResult)) {
            System.out.printf("""
                      [ERROR] simplification
                        - Input: %s
                        - Expected: %s
                        - Actual: %s
                      """, 
                    input, expectedResult.toString(), actualResult.toString());
            failingCases++;
        }
        else {
            passingCases++;
        }
    }
    
    private static void assertSolve(String input, double expected) {
        RootNode root = Parser.parse(input);
        if (root instanceof Expression)
            throw new Error("Expected equation, got expression!");
            
        Equation eqn = (Equation)root;
        Simplifier.simplify(eqn);
        double result = new LinearSolver().solve(eqn);
        if (result != expected) {
            System.out.printf("""
                      [ERROR] solve
                        - Input: %s
                        - Expected: %s
                        - Actual: %s
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
        RootNode result = Parser.parse(input);
        if (result instanceof Expression expr)
            return Evaluator.evaluate(expr);
        
        throw new Error("Expected expression, got equation!");
    }
    
    private static void log(String template, Object... args) {
        System.out.printf("[self-check] " + template + "\n", args);
    }
    
}
