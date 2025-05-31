package runthenumbers.math.tokenize;

import java.awt.Point;

/**
 * @date May 25, 2025
 * @author Richard Si
 */
public class Token {
    public final String kind;
    public final String value;
    public final Point position;

    public Token(String kind, String value, Point position) {
        this.kind = kind;
        this.value = value;
        this.position = position;
    }
    
    public boolean is(String kind) {
        return this.kind.equals(kind);
    }

    @Override
    public String toString() {
        return "Token{" + "kind=" + kind + ", value=" + value + ", position=" + position + '}';
    }
    
}
