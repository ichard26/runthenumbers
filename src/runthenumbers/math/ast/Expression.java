package runthenumbers.math.ast;

/**
 * @date May 25, 2025
 * @author Richard Si
 */
public non-sealed abstract class Expression implements ParseResult {
    private ParseResult parent;

    public ParseResult getParent() {
        return parent;
    }

    public void setParent(ParseResult parent) {
        this.parent = parent;
    }
}
