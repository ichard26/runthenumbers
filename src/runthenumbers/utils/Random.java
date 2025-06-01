package runthenumbers.utils;

import java.util.InputMismatchException;

/**
 *
 * @author Richard Si
 */
public class Random {
    /**
     * Method Name: titleCase
     * Description: Rewrite a word in lowercase but capitalize the first letter
     * @param word
     * @return the word in title case
     */
    public static String titleCase(String word) {
        if (word.contains(" "))
            throw new InputMismatchException("no spaces allowed");
        
        word = word.toLowerCase();
        return word.replaceFirst(word.substring(0, 1), word.substring(0, 1).toUpperCase());
    }
}
