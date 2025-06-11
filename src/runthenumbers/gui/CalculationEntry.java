package runthenumbers.gui;

/**
 * Class Name: HistoryEntry
 * Description: Stores the input and result of one calculation.
 * Programmer: Richard Si
 * Date: June 9, 2025
 */
public class CalculationEntry {
    private String mode;
    private String input;
    private Double answer;

    public CalculationEntry(String inputType, String input, Double answer) {
        this.mode = inputType;
        this.input = input;
        this.answer = answer;
    }

    // Accessors and mutators.

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public Double getAnswer() {
        return answer;
    }

    public void setAnswer(Double answer) {
        this.answer = answer;
    }

    /**
     * Method Name: fromLine
     * Description: Construct a new HistoryEntry from a profile file.
     * @param line A line from the history file.
     * @return the new HistoryEntry instance.
     */
    public CalculationEntry fromLine(String line) {
        String[] tokens = line.split(" | ");
        if (tokens.length != 3)
            throw new Error("too many fields");

        return new CalculationEntry(tokens[0], tokens[1], Double.valueOf(tokens[2]));
    }

    /**
     * Method Name: toLine
     * Description: Format the history entry as a string for persistent storage.
     * @return The string representation (to be parsed later).
     */
    public String toLine() {
        return mode + " | " + input + " | " + answer;
    }
}
