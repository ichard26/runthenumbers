package runthenumbers.utils.prompt;

import java.util.ArrayList;
import java.util.function.Predicate;

/**
 *
 * @author Richard Si
 * @param <T>
 */
public abstract class Prompt<T> {
    String prompt;
    ArrayList<Predicate<T>> restrictions;
    ArrayList<String> restrictionReasons;

    public Prompt(String prompt) {
        this.prompt = prompt;
        this.restrictions = new ArrayList<>();
        this.restrictionReasons = new ArrayList<>();
    }

    public static Prompt<String> String(String prompt) {
        return new StringPrompt(prompt);
    }

    public static IntegerPrompt Integer(String prompt) {
        return new IntegerPrompt(prompt);
    }

    public static DoublePrompt Double(String prompt) {
        return new DoublePrompt(prompt);
    }

    public Prompt<T> restrict(Predicate<T> predicate) {
        return restrict(predicate, "");
    }

    public Prompt<T> restrict(Predicate<T> predicate, String explanation) {
        restrictions.add(predicate);
        restrictionReasons.add(explanation);
        return this;
    }

    public String checkRestrictions(T value) {
        for (int i = 0; i < restrictions.size(); i++) {
            if (!restrictions.get(i).test(value))
                return restrictionReasons.get(i);
        }
        return null;
    }

    protected abstract T askOnce();
    public T ask() {
        T value;
        String restrictedReason;

        while (true) {
            value = askOnce();
            if (value != null) {
                restrictedReason = checkRestrictions(value);
                if (restrictedReason == null)
                     return value;
                else
                    System.out.println(restrictedReason);
            }
        }
    }
}
