package runthenumbers.math.ast;

import runthenumbers.utils.PrettyPrintable;

/**
 * TODO
 * @author Richard Si
 */
public final class Variable extends ExprNode implements PrettyPrintable {
    private String name;

    public Variable(String name) {
        this.name = name;
    }
    
    // Getters and setters.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * TODO
     * @return 
     */
    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Variable other)
            return this.name.equals(other.name);
        
        return false;
    }

    @Override
    public String toPrettyString() {
        return "Variable(" + name + ")";
    }   
}
