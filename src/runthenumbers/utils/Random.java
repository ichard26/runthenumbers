package runthenumbers.utils;

import java.util.InputMismatchException;

/**
 * Class Name: Random
 * Description: Collection of utility functions.
 * Programmer: Richard Si
 * Date: June 2, 2025.
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

    /**
     * Method Name: colorPrintf
     * Description: Wrapper over System.out.printf which accepts a color tag.
     * @param template The printf template.
     * @param parameters The remaining parameters to pass printf.
     */
    public static void colorPrintf(String template, Object... parameters) {
        int colorTagEnd;
        String ansiCode = null;

        // Look for a [$color] prefix and replace it with the right ANSI code.
        if (template.startsWith("[")) {
            colorTagEnd = template.indexOf("]");
            switch (template.substring(1, colorTagEnd)) {
                case "red" -> ansiCode = ANSI.RED;
                case "yellow" -> ansiCode = ANSI.YELLOW;
                case "green" -> ansiCode = ANSI.GREEN;
                case "cyan" -> ansiCode = ANSI.CYAN;
                case "magenta" -> ansiCode = ANSI.MAGENTA;
                case "black" -> ansiCode = ANSI.BLACK;
                case "white" -> ansiCode = ANSI.WHITE;
            }
            // Don't do anything if an unknown tag was given.
            if (ansiCode != null) {
                template = template.substring(colorTagEnd + 1);
                template = ansiCode + template + ANSI.RESET;
            }
        }
        System.out.printf(template, parameters);
    }

    public static String formatNumber(double number) {
        // The double is also an integer, so don't show the .0
        if (number % 1 == 0)
            return Integer.toString(Double.valueOf(number).intValue());
        return Double.toString(number);
    }

}
