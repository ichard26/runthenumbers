package runthenumbers.math.ast;

/**
 * TODO
 * @date May 25, 2025
 * @author Richard Si
 */
public class Number extends Expression {
    private double value;

    public Number(double value) {
        this.value = value;
    }

    // Getters and setters.
    
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
    
    /**
     * TODO
     * @return 
     */
    @Override
    public String toString() {
        return Double.toString(value);
    }
}
