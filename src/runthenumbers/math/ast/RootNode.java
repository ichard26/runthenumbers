package runthenumbers.math.ast;

/**
 * Class Name: RootNode
 * Description: Union of node types that represent the root of any math AST
 *              (AKA, it can be returned by the parser).
 * Programmer: Richard Si
 * Date: June 6, 2025
 */
public abstract sealed class RootNode extends Node
        permits Expression, Equation { }
