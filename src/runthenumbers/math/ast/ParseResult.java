package runthenumbers.math.ast;

public sealed interface ParseResult permits Equation, Expression {
    @Override
    public String toString();
}
