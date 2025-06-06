package runthenumbers.math.ast;

/**
 *
 * @author Richard Si
 */
public sealed interface ParentNode permits Equation, Expression, Group, Operation { }
