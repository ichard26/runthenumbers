package runthenumbers.math.ast;

import runthenumbers.utils.PrettyPrintable;

/**
 * TODO
 * @date May 25, 2025
 * @author Richard Si
 */
public final class Number extends ExprNode implements PrettyPrintable {
    private double value;

    public Number(double value) {
        this.value = value;
    }

    @Override
    public Number deepcopy() {
        return new Number(value);
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
        // The double is also an integer, so don't show the .0
        if (value % 1 == 0)
            return Integer.toString(Double.valueOf(value).intValue());
        return Double.toString(value);
    }

    @Override
    public String toPrettyString() {
        return "Number(" + toString() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Number other)
            return this.value == other.value;

        return false;
    }
}
