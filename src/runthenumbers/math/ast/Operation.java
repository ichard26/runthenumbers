package runthenumbers.math.ast;

/**
 * Class Name: Operation
 * Description: A math operation AST node, containing two operands.
 * Programmer: Richard Si
 * Date: May 26, 2025
 */
public final class Operation extends ExprNode implements ParentNode {
    private ExprNode left;
    private String operator;
    private ExprNode right;

    public Operation(ExprNode left, String operator, ExprNode right) {
        setLeft(left);
        this.operator = operator;
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public ExprNode getRight() {
        right.setParent(this);
        return right;
    }

    public void setRight(ExprNode right) {
        this.right = right;
    }

    /**
     * Method Name: replaceOperand
     * Description: Replace the left or right operand with a new expression.
     * @param operand The original operand node to replace.
     * @param replacement The replacement expression node.
     */
    public void replaceOperand(ExprNode operand, ExprNode replacement) {
        assert operand == left || operand == right : operand.toString() + "is not an operand";
        if (operand == left)
            setLeft(replacement);
        else
            setRight(replacement);
    }

    /**
     * Method Name: is
     * Description: Check if this operation uses specific operators.
     * @param operators The desired operators.
     * @return True if the operation uses a given operator.
     */
    public boolean is(String... operators) {
        for (String op : operators)
            if (op.equals(this.operator))
                return true;
        return false;
    }

    /**
     * Method Name: deepcopy
     * Description: Create a new instance of this node (and recursively for
     *               any children nodes).
     * @return An identical but separate copy of this node.
     */
    @Override
    public Operation deepcopy() {
        return new Operation(left.deepcopy(), operator, right.deepcopy());
    }

    /**
     * Method Name: toString
     * @return The node's human-readable string representation.
     */
    @Override
    public String toString() {
        if (is("*")) {
            // Present N*x or x*N as Nx.
            if (left instanceof Number && right instanceof Variable)
                return left.toString() + right;
            if (left instanceof Variable && right instanceof Number)
                return right.toString() + left;
        }
        else if (is("^") && left instanceof Variable)
            // Don't add spaces in a power with a variable base.
            return left.toString() + "^" + right;

        return left.toString() + " " + operator + " " + right;
    }

    /**
     * Method Name: equals
     * @param obj The other object to compare to.
     * @return True if the two objects represent the same parse tree.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Operation other)
            return this.left.equals(other.left)
                    && this.operator.equals(other.operator)
                    && this.right.equals(other.right);

        return this == obj;
    }
}
