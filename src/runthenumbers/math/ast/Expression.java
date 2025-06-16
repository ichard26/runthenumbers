package runthenumbers.math.ast;

/**
 * Class Name: Equation
 * Description: Parsed math expression.
 * Programmer: Richard Si
 * Date: May 30, 2025
 */
public final class Expression extends RootNode implements ParentNode {
    private ExprNode body;

    public Expression(ExprNode body) {
        setBody(body);
    }

    // Getters and setters.

    public ExprNode getBody() {
        return body;
    }

    public void setBody(ExprNode body) {
        body.setParent(this);
        this.body = body;
    }

    /**
     * Method Name: deepcopy
     * Description: Create a new instance of this node (and recursively for
     *               any children nodes).
     * @return An identical but separate copy of this node.
     */
    @Override
    public Expression deepcopy() {
        return new Expression(body.deepcopy());
    }

    /**
     * Method Name: toString
     * @return The node's human-readable string representation.
     */
    @Override
    public String toString() {
        return body.toString();
    }

    /**
     * Method Name: equals
     * @param obj The other object to compare to.
     * @return True if the two objects represent the same parse tree.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Expression other)
            return this.body.equals(other.body);

        return false;
    }

}
