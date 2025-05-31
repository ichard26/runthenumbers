package runthenumbers.math.ast;

/**
 * TODO
 * @author Richard Si
 */
public class Variable extends Expression {
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
}
