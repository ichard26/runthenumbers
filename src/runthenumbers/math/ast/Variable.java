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

    @Override
    public Variable deepcopy() {
        return new Variable(name);
    }

    // Getters and setters.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * TODO
     * @return
     */
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Variable other)
            return this.name.equals(other.name);

        return false;
    }

    @Override
    public String toPrettyString() {
        return "Variable(" + name + ")";
    }
}
