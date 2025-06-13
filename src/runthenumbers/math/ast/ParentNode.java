package runthenumbers.math.ast;

/**
 * Class Name: ParentNode
 * Description: Union of node types that can contain other AST nodes.
 * Programmer: Richard Si
 * Date: June 6, 2025
 */
public sealed interface ParentNode permits Equation, Expression, Group, Operation { }
