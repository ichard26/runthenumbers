package runthenumbers.math.ast;

/**
 * Class Name: Equation
 * Description: Math equation with a left and a right side.
 * Programmer: Richard Si
 * Date: May 30, 2025
 */
public final class Equation extends RootNode implements ParentNode {
    private ExprNode left;
    private ExprNode right;

    public Equation(ExprNode left, ExprNode right) {
        setLeft(left);
        setRight(right);
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

    /**
     * Method Name: replaceSide
     * Description: Replace one side of the equation with a new expression node.
     * @param side The "root" expression node of the side to replace.
     * @param replacement The replacement expression node.
     */
    public void replaceSide(ExprNode side, ExprNode replacement) {
        assert side == left || side == right : side.toString() + "is not left or right side";
        if (side == left)
            setLeft(replacement);
        else
            setRight(replacement);
    }

    /**
     * Method Name: deepcopy
     * Description: Create a new instance of this node (and recursively for
     *               any children nodes).
     * @return An identical but separate copy of this node.
     */
    @Override
    public Equation deepcopy() {
        return new Equation(left.deepcopy(), right.deepcopy());
    }

    /**
     * Method Name: toString
     * @return The node's human-readable string representation.
     */
    @Override
    public String toString() {
        return left.toString() + " = " + right.toString();
    }

    /**
     * Method Name: equals
     * @param obj The other object to compare to.
     * @return True if the two objects represent the same parse tree.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Equation other)
            return this.left.equals(other.left) && this.right.equals(other.right);

        return false;
    }

}
