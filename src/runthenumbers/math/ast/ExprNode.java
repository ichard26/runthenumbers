package runthenumbers.math.ast;

/**
 * Class Name: ExprNode
 * Description: Union of node types that can make up any math expression.
 * Programmer: Richard Si
 * Date: June 6, 2025
 */
public abstract sealed class ExprNode extends Node
        permits Number, Variable, Operation, Group {
    private ParentNode parent;

    // Common getters and setters.

    public ParentNode getParent() {
        return parent;
    }

    public void setParent(ParentNode parent) {
        this.parent = parent;
    }

    /**
     * Method Name: deepcopy
     * Description: Create a new instance of this node (and recursively for
     *               any children nodes).
     * @return An identical but separate copy of this node.
     */
    @Override
    abstract public ExprNode deepcopy();
}
