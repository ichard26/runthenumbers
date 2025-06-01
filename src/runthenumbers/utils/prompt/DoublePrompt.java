package runthenumbers.utils.prompt;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Richard Si
 */
public class DoublePrompt extends Prompt<Double> {
    public DoublePrompt(String prompt) {
        super(prompt);
    }
    
    public DoublePrompt gt(int min) {
        return greaterThan(min);
    }
    
    public DoublePrompt greaterThan(int min) {
        String failMsg = String.format("Please enter a number greater than " + min + ".");
        restrict(n -> n > min, failMsg);
        return this;
    }
    
    public DoublePrompt range(double min, double max) {
        // TODO: better decimals
        String failMsg = String.format("Please enter a number from %.1f to %.1f.", min, max);
        restrict(n -> n >= min && n <= max, failMsg);
        return this;
    }
    
    @Override
    protected Double askOnce() {
        Scanner scanN = new Scanner(System.in);
        
        try {
            System.out.printf("%s ", prompt);
            return scanN.nextDouble(); 
        } catch (InputMismatchException e) {
           scanN.nextLine(); // Skip pass the invalid input.
           System.out.print("That's not a number! ");
           return null;
        }
    }
    
}
