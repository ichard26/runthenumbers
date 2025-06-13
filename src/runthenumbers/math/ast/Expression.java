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

    @Override
    public Expression deepcopy() {
        return new Expression(body.deepcopy());
    }

    public ExprNode getBody() {
        return body;
    }

    public void setBody(ExprNode body) {
        body.setParent(this);
        this.body = body;
    }

    @Override
    public String toString() {
        return body.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Expression other)
            return this.body.equals(other.body);

        return false;
    }

}
