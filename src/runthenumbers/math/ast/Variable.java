package runthenumbers.math.ast;

import runthenumbers.utils.PrettyPrintable;

/**
 * Class Name: Variable
 * Description: A variable AST node.
 * Programmer: Richard Si
 * Date: May 26, 2025
 */
public final class Variable extends ExprNode implements PrettyPrintable {
    private String name;

    public Variable(String name) {
        this.name = name;
    }

    // Getters and setters.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method Name: deepcopy
     * Description: Create a new instance of this node (and recursively for
     *               any children nodes).
     * @return An identical but separate copy of this node.
     */
    @Override
    public Variable deepcopy() {
        return new Variable(name);
    }

    /**
     * Method Name: toString
     * @return The node's human-readable string representation.
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Method Name: equals
     * @param obj The other object to compare to.
     * @return True if the two objects represent the same parse tree.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Variable other)
            return this.name.equals(other.name);

        return false;
    }

    /**
     * Method Name: toPrettyString
     * Description: Format the node for display via the PrettyPrinter utility class.
     * @return The pretty string representation.
     */
    @Override
    public String toPrettyString() {
        return "Variable(" + name + ")";
    }
}
