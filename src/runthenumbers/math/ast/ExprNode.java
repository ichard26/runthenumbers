package runthenumbers.math.ast;

/**
 * @date June 4, 2025
 * @author Richard Si
 */
public abstract sealed class ExprNode extends Node permits Number, Variable, Operation, Group {
    private Node parent;

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}
