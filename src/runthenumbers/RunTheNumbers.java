package runthenumbers;

import runthenumbers.math.Evaluator;
import runthenumbers.math.ast.Expression;
import runthenumbers.math.ast.Parser;
import runthenumbers.math.tokenize.TokenStream;
import runthenumbers.math.tokenize.Tokenizer;

/*
[TODO LIST]

- [x] Port over calculator math implementation
- [] Extend math impl. to handle variables and equations
- [] Extend tokenizer and parser with fix-ups and error handling
- [] DOCUMENTATION (class, methods) & COMMENT CHECKPOINT
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
        boolean passed = TestSuite.run();
        if (!passed) {
            System.out.println("[ERROR] self-check failed, aborting...");
            System.exit(1);
        }
        
//        TokenStream stream = new Tokenizer().tokenize("+ =");
//        Expression expr = Parser.parse(stream);
//        double result = Evaluator.evaluate(expr);
    }
    
}
