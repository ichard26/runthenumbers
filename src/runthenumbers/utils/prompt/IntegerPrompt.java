package runthenumbers.utils.prompt;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Richard Si
 */
public class IntegerPrompt extends Prompt<Integer> {
    public IntegerPrompt(String prompt) {
        super(prompt);
    }
    
    public IntegerPrompt gt(int min) {
        return greaterThan(min);
    }
    
    public IntegerPrompt greaterThan(int min) {
        String failMsg = String.format("Please enter a whole number greater than %d.", min);
        restrict(n -> n > min, failMsg);
        return this;
    }
    
    public IntegerPrompt range(int min, int max) {
        String failMsg = String.format("Please enter a whole number from %d to %d.", min, max);
        restrict(n -> n >= min && n <= max, failMsg);
        return this;
    }
    
    @Override
    protected Integer askOnce() {
        Scanner scanN = new Scanner(System.in);
        
        try {
            System.out.printf("%s ", prompt);
            return scanN.nextInt(); 
        } catch (InputMismatchException e) {
           scanN.nextLine(); // Skip pass the invalid input.
           System.out.print("That's not a number! ");
           return null;
        }
    }
    
}
