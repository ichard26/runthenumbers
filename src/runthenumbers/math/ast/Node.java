package runthenumbers.math.ast;

public sealed abstract class Node permits RootNode, ExprNode {
    @Override
    abstract public String toString();
    @Override
    abstract public boolean equals(Object other);
    abstract public Node deepcopy();
}
