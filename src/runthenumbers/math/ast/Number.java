package runthenumbers.math.ast;

import runthenumbers.utils.PrettyPrintable;
import static runthenumbers.utils.Random.formatNumber;

/**
 * Class Name: Number
 * Description: A constant number AST node.
 * Programmer: Richard Si
 * Date: May 26, 2025
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
        return formatNumber(value);
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
