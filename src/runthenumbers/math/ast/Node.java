package runthenumbers.math.ast;

/**
 * Class Name: Node
 * Description: Represents any possible node in a parsed math AST.
 * Programmer: Richard Si
 * Date: June 6, 2025
 */
public sealed abstract class Node permits RootNode, ExprNode {
    /**
     * Method Name: toString
     * @return The node's human-readable string representation.
     */
    @Override
    abstract public String toString();

    /**
     * Method Name: equals
     * @param other The other object to compare to.
     * @return True if the two objects represent the same parse tree.
     */
    @Override
    abstract public boolean equals(Object other);

    /**
     * Method Name: deepcopy
     * Description: Create a new instance of this node (and recursively for
     *               any children nodes).
     * @return An identical but separate copy of this node.
     */
    abstract public Node deepcopy();
}
