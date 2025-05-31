package runthenumbers.math.ast;

/**
 * TODO
 * @author Richard Si
 */
public class Equation {
    private Expression left;
    private Expression right;

    public Equation(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }
    
    // Getters and setters.

    public Expression getLeft() {
        return left;
    }

    public void setLeft(Expression left) {
        this.left = left;
    }

    public Expression getRight() {
        return right;
    }

    public void setRight(Expression right) {
        this.right = right;
    }
    
    /**
     * TODO
     * @return 
     */
    @Override
    public String toString() {
        return left.toString() + " = " + right.toString();
    }
    
}
