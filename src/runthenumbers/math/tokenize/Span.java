package runthenumbers.math.tokenize;

/**
 * Class Name: Span
 * Description: Simple 2-item tuple for storing start and end indices.
 * Programmer: Richard Si
 * Date: May 31, 2025
 */
public class Span {
    private final int start;
    private final int end;

    public Span(int start, int end) {
        this.start = start;
        this.end = end;
    }

    // Getters (no setters as this class is immutable).
    
    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
    
    /**
     * Method Name: toString
     * Description: Format span in a human friendly way.
     * @return The human-friendly string presentation of the span.
     */
    @Override
    public String toString() {
        return "(" + start + ", " + end + ")";
    }
    
}
