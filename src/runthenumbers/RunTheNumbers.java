package runthenumbers;

import runthenumbers.utils.PrettyPrinter;
import runthenumbers.utils.prompt.StringPrompt;
import runthenumbers.math.Evaluator;
import runthenumbers.math.ast.Expression;
import runthenumbers.math.ast.ParseResult;
import runthenumbers.math.ast.Parser;
import runthenumbers.math.tokenize.TokenStream;
import runthenumbers.math.tokenize.Tokenizer;

/*
[TODO LIST]

- [x] Port over calculator math implementation
- [] Extend math impl. to handle variables and equations
- [x] Extend tokenizer and parser with fix-ups and error handling
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
        
        String input;
        while (!(input = new StringPrompt(">>>").ask()).isBlank()) {
            TokenStream stream = new Tokenizer().tokenize(input);
            ParseResult result = Parser.parse(stream);
            if (result instanceof Expression expr) {
                new PrettyPrinter().print(result);
                System.out.println("Result: " + Evaluator.evaluate(expr));
            }
            else {
                new PrettyPrinter().print(result);
            }
            System.out.println();
        }

    }
    
}
