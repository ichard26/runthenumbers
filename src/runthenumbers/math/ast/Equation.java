package runthenumbers.math.ast;

import java.util.Objects;

/**
 * TODO
 * @author Richard Si
 */
public final class Equation implements ParseResult {
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
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Equation other)
            return this.left.equals(other.left) && this.right.equals(other.right);
        
        return false;
    }
    
}
