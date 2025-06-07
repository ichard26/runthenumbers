package runthenumbers.math.ast;

/**
 * TODO
 * @date May 25, 2025
 * @author Richard Si
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
    
    public void replaceOperand(ExprNode operand, ExprNode replacement) {
        assert operand == left || operand == right : operand.toString() + "is not an operand";
        if (operand == left)
            setLeft(replacement);
        else
            setRight(replacement);
    }
    
    public boolean is(String... operators) {
        for (String op : operators)
            if (op.equals(this.operator))
                return true;
        return false;
    }
    
    /**
     * TODO
     * @return 
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
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Operation other)
            return this.left.equals(other.left) 
                    && this.operator.equals(other.operator)
                    && this.right.equals(other.right);
        
        return this == obj;
    }
}
