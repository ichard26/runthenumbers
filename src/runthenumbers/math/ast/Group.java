package runthenumbers.math.ast;

/**
 * Class Name: Group
 * Description: AST node for storing a parenthesized sub-expression.
 * Programmer: Richard Si
 * Date: May 26, 2025
 */
public final class Group extends ExprNode implements ParentNode {
    private ExprNode body;

    public Group(ExprNode body) {
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
    public Group deepcopy() {
        return new Group(body.deepcopy());
    }

    /**
     * Method Name: toString
     * @return The node's human-readable string representation.
     */
    @Override
    public String toString() {
        return "(" + body + ')';
    }

    /**
     * Method Name: equals
     * @param obj The other object to compare to.
     * @return True if the two objects represent the same parse tree.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Group other)
            return this.body.equals(other.body);

        return false;
    }
}
