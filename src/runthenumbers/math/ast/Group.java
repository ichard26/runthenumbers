package runthenumbers.math.ast;

/**
 * TODO
 * @date May 25, 2025
 * @author Richard Si
 */
public class Group extends Expression {
    private Expression body;

    public Group(Expression body) {
        this.body = body;
    }

    // Getters and setters.
    
    public Expression getBody() {
        return body;
    }

    public void setBody(Expression body) {
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
}
