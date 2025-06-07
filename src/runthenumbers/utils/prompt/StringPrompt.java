package runthenumbers.utils.prompt;

import java.util.Collection;
import java.util.Scanner;

/**
 *
 * @author Richard Si
 */
public class StringPrompt extends Prompt<String> {
    public StringPrompt(String prompt) {
        super(prompt);
    }

    public StringPrompt choices(Collection<String> options, String... extraOptions) {
        restrict(s -> {
            for (String choice : options) {
                if (s.equals(choice))
                    return true;
            }
            for (String choice : extraOptions) {
                if (s.equals(choice))
                    return true;
            }
            return false;
        }, "Invalid choice.");
        return this;
    }

    @Override
    protected String askOnce() {
        Scanner scanS = new Scanner(System.in);
        System.out.printf("%s ", prompt);
        return scanS.nextLine();
    }

}
