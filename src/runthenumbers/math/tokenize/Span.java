package runthenumbers.math.tokenize;

/**
 * TODO
 * @author Richard Si
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
    
}
