package runthenumbers.math.ast;

/**
 * Class Name: Node
 * Description: Represents any possible node in a parsed math AST.
 * Programmer: Richard Si
 * Date: June 6, 2025
 */
public sealed abstract class Node permits RootNode, ExprNode {
    @Override
    abstract public String toString();
    @Override
    abstract public boolean equals(Object other);
    abstract public Node deepcopy();
}
