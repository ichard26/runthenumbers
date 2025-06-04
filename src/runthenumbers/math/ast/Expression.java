package runthenumbers.math.ast;

/**
 *
 * @author Richard Si
 */
public final class Expression extends RootNode {
    private ExprNode body;

    public Expression(ExprNode body) {
        this.body = body;
    }

    public ExprNode getBody() {
        return body;
    }

    public void setBody(ExprNode body) {
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
