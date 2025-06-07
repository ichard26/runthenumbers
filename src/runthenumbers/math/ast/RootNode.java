package runthenumbers.math.ast;

/**
 *
 * @author Richard Si
 */
public abstract sealed class RootNode extends Node
        permits Expression, Equation { }
