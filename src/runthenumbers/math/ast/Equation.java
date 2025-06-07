package runthenumbers.math.ast;

/**
 * TODO
 * @author Richard Si
 */
public final class Equation extends RootNode implements ParentNode {
    private ExprNode left;
    private ExprNode right;

    public Equation(ExprNode left, ExprNode right) {
        setLeft(left);
        setRight(right);
    }
    
    @Override
    public Equation deepcopy() {
        return new Equation(left.deepcopy(), right.deepcopy());
    }
    
    // Getters and setters.

    public ExprNode getLeft() {
        return left;
    }

    public void setLeft(ExprNode left) {
        left.setParent(this);
        this.left = left;
    }

    public ExprNode getRight() {
        return right;
    }

    public void setRight(ExprNode right) {
        right.setParent(this);
        this.right = right;
    }
    
    public void replaceSide(ExprNode side, ExprNode replacement) {
        assert side == left || side == right : side.toString() + "is not left or right side";
        if (side == left)
            setLeft(replacement);
        else
            setRight(replacement);
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
