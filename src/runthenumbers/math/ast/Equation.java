package runthenumbers.math.ast;

/**
 * TODO
 * @author Richard Si
 */
public final class Equation extends RootNode implements ParentNode {
    private ExprNode left;
    private ExprNode right;

    public Equation(ExprNode left, ExprNode right) {
        this.left = left;
        this.right = right;
    }
    
    // Getters and setters.

    public ExprNode getLeft() {
        return left;
    }

    public void setLeft(ExprNode left) {
        this.left = left;
    }

    public ExprNode getRight() {
        return right;
    }

    public void setRight(ExprNode right) {
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
