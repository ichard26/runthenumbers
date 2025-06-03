package runthenumbers.math.ast;

/**
 * TODO
 * @date May 25, 2025
 * @author Richard Si
 */
public class Operation extends Expression {
    private Expression left;
    private String operator;
    private Expression right;

    public Operation(Expression left, String operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    // Getters and setters.
    
    public Expression getLeft() {
        return left;
    }

    public void setLeft(Expression left) {
        this.left = left;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Expression getRight() {
        return right;
    }

    public void setRight(Expression right) {
        this.right = right;
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
        // Present N*x or x*N as Nx.
        if (is("*")) {
            if (left instanceof Number && right instanceof Variable)
                return left.toString() + right;
            if (left instanceof Variable && right instanceof Number)
                return right.toString() + left;
        }
        
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
