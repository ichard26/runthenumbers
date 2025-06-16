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

    // Getters and setters.

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Method Name: deepcopy
     * Description: Create a new instance of this node (and recursively for
     *               any children nodes).
     * @return An identical but separate copy of this node.
     */
    @Override
    public Number deepcopy() {
        return new Number(value);
    }

    /**
     * Method Name: toString
     * @return The node's human-readable string representation.
     */
    @Override
    public String toString() {
        return formatNumber(value);
    }

    /**
     * Method Name: toPrettyString
     * Description: Format the node for display via the PrettyPrinter utility class.
     * @return The pretty string representation.
     */
    @Override
    public String toPrettyString() {
        return "Number(" + toString() + ")";
    }

    /**
     * Method Name: equals
     * @param obj The other object to compare to.
     * @return True if the two objects represent the same parse tree.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Number other)
            return this.value == other.value;

        return false;
    }
}
