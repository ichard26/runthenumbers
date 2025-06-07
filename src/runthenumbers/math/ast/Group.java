package runthenumbers.math.ast;

/**
 * TODO
 * @date May 25, 2025
 * @author Richard Si
 */
public final class Group extends ExprNode implements ParentNode {
    private ExprNode body;

    public Group(ExprNode body) {
        setBody(body);
    }

    @Override
    public Group deepcopy() {
        return new Group(body.deepcopy());
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
     * TODO
     * @return
     */
    @Override
    public String toString() {
        return "(" + body + ')';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Group other)
            return this.body.equals(other.body);

        return false;
    }
}
