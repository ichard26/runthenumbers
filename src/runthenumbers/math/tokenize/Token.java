package runthenumbers.math.tokenize;

import java.awt.Point;

/**
 * @date May 25, 2025
 * @author Richard Si
 */
public class Token {
    private final String type;
    private final String value;
    private final Point position;

    public Token(String kind, String value, Point position) {
        this.type = kind;
        this.value = value;
        this.position = position;
    }
    
    // Getters (no setters as this class is intended to be immutable).

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public Point getPosition() {
        return position;
    }
    
    /**
     * Method Name: is
     * Description: Check if the token is of type X.
     * @param type The desired type.
     * @return True if it is the desired type, false otherwise.
     */
    public boolean is(String type) {
        return this.type.equals(type);
    }

    /**
     * TODO
     * @return 
     */
    @Override
    public String toString() {
        return "Token{" + "kind=" + type + ", value=" + value + ", position=" + position + '}';
    }
    
}
