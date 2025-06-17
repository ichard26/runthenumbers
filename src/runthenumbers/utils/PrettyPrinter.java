package runthenumbers.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import static runthenumbers.utils.Random.titleCase;

// NOTE: I threw this together quickly. This is a nonessential portion of the project
// thus the code quality is not the best.

// Used as a MISSING object. null can't be used as it can appear
// as a field value in legitimate scenarios.
class Sentinel {}

/**
 * Class Name: PrettyPrinter
 * Description: Print objects in a pretty multi-line way. Used for logging.
 * Programmer: Richard Si
 * Date: June 5, 2025.
 */
public class PrettyPrinter {
    private int indentLevel;
    private boolean ignoreIndentOnce;
    private final String[] CLASS_COLOURS = { ANSI.MAGENTA, ANSI.BLUE };
    private int colorIndex;
    private final Sentinel UNKNOWN = new Sentinel();

    // No getters and setters as these fields should never be accessed by
    // outside code. A pretty printer instance is short-lived and can only be
    // used once.

    /**
     * Method Name: print
     * Description: Pretty print an object.
     * @param obj The object to pretty print.
     */
    public void print(Object obj) {
        if (obj == null) {
            System.out.println(obj);
            return;
        }

        Class cls = obj.getClass();

        // Use alternating colors to make bracket pairs easier to differentiate.
        colorIndex = (colorIndex + 1) % CLASS_COLOURS.length;
        String color = CLASS_COLOURS[colorIndex];

        printf("%s%s(%s\n", color, cls.getSimpleName(), ANSI.RESET);
        indentLevel++;

        for (Field field : cls.getDeclaredFields()) {
            // This field is not name conventionally, ignore it.
            if (!Character.isLetterOrDigit(field.getName().charAt(0)))
                continue;

            // Find the field's getter (since the field itself may be private).
            Method getter = null;
            Object value = UNKNOWN;
            try {
                getter = cls.getMethod("get" + titleCase(field.getName()));
            } catch (NoSuchMethodException e) { }
            if (getter == null)
                continue;

            // Attempt to invoke the getter.
            try {
                value = getter.invoke(obj);
            } catch (IllegalAccessException | InvocationTargetException ex) { /* Ignore */ }
            if (UNKNOWN.equals(value))
                continue;

            // If the field's value implements the PrettyPrintable interface,
            // then display the string returned by its toPrettyString().
            if (value instanceof PrettyPrintable printable) {
                printf("%s=%s,\n", field.getName(), printable.toPrettyString());
            }
            // If it's a primitive, simply display field and its value in a simple format.
            else if (value instanceof String string) {
                printf("%s='%s',\n", field.getName(), string);
            }
            else if (value instanceof Integer
                    || value instanceof Double
                    || value instanceof Boolean) {
                printf("%s=%s,\n", field.getName(), value.toString());
            }
            // Otherwise, recurse and print the fields of the original field object.
            else {
                printf("%s=", field.getName());
                ignoreIndentOnce = true;
                print(value);
            }
        }

        indentLevel--;
        printf("%s)%s\n", color, ANSI.RESET);
    }

    /**
     * Method Name: printf
     * Description: A wrapper over printf that respects indentation.
     * @param template The printf template.
     * @param args The printf arguments.
     */
    private void printf(String template, Object... args) {
        if (!ignoreIndentOnce)
            template = " ".repeat(indentLevel*3) + template;
        System.out.printf(template, args);
        ignoreIndentOnce = false;
    }

}
