package runthenumbers;

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
        boolean passed = TestSuite.run();
        if (!passed) {
            System.out.println("[ERROR] self-check failed, aborting...");
            System.exit(1);
        }
    }
    
}
