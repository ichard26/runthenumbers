package runthenumbers.math.ast;

/**
 * @date June 4, 2025
 * @author Richard Si
 */
public abstract sealed class ExprNode extends Node 
        permits Number, Variable, Operation, Group {
    private ParentNode parent;

    public ParentNode getParent() {
        return parent;
    }

    public void setParent(ParentNode parent) {
        this.parent = parent;
    }
}
