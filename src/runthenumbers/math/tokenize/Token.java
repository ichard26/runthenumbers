package runthenumbers.math.tokenize;

/**
 * @date May 25, 2025
 * @author Richard Si
 */
public class Token {
    private final String type;
    private String value;
    private final Span position;

    public Token(String kind, String value, Span position) {
        this.type = kind;
        this.value = value;
        this.position = position;
    }
    
    // Getters and setters (only for value, though).

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Span getPosition() {
        return position;
    }
    
    /**
     * Method Name: is
     * Description: Check if the token is of type X, Y, or Z.
     * @param types The acceptable types.
     * @return True if it is the one of the desired types, false otherwise.
     */
    public boolean is(String... types) {
        for (String acceptableType : types) {
            if (this.type.equals(acceptableType))
                return true;
        }
        return false;
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
