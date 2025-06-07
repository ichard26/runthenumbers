package runthenumbers.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import static runthenumbers.utils.Random.titleCase;

class Sentinel {}

/**
 *
 * @author Richard Si
 */
public class PrettyPrinter {
    private int indentLevel;
    private boolean ignoreIndentOnce;
    private final String[] CLASS_COLOURS = { ANSI.MAGENTA, ANSI.BLUE };
    private int colorIndex;
    private final Sentinel UNKNOWN = new Sentinel();

    public void print(Object obj) {
        if (obj == null) {
            System.out.println(obj);
            return;
        }

        Class cls = obj.getClass();

        String color = nextColor();
        printf("%s%s(%s\n", color, cls.getSimpleName(), ANSI.RESET);
        indentLevel++;

        for (Field field : cls.getDeclaredFields()) {
            if (!Character.isLetterOrDigit(field.getName().charAt(0)))
                continue;

            Method getter = null;
            Object value = UNKNOWN;
            try {
                getter = cls.getMethod("get" + titleCase(field.getName()));
            } catch (NoSuchMethodException e) { }
            if (getter == null)
                continue;

            try {
                value = getter.invoke(obj);
            } catch (IllegalAccessException | InvocationTargetException ex) { /* Ignore */ }
            if (UNKNOWN.equals(value))
                continue;

            if (value instanceof PrettyPrintable printable) {
                printf("%s=%s,\n", field.getName(), printable.toPrettyString());
            }
            else if (value instanceof String string) {
                printf("%s='%s',\n", field.getName(), string);
            }
            else {
                if (value instanceof Integer
                        || value instanceof Double
                        || value instanceof Boolean) {
                    printf("%s=%s,\n", field.getName(), value.toString());
                }
                else {
                    printf("%s=", field.getName());
                    ignoreIndentOnce = true;
                    print(value);
                }
            }
        }

        indentLevel--;
        printf("%s)%s\n", color, ANSI.RESET);
    }

    private void printf(String template, Object... args) {
        if (!ignoreIndentOnce)
            template = " ".repeat(indentLevel*3) + template;
        System.out.printf(template, args);
        ignoreIndentOnce = false;
    }

    private String nextColor() {
        colorIndex = (colorIndex + 1) % CLASS_COLOURS.length;
        return CLASS_COLOURS[colorIndex];
    }

}
