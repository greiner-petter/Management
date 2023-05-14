package de.ostfalia.aud.s23ss.base;

import java.util.Stack;

public class TreeIterator {
    private Stack<Tree> stack;

    public TreeIterator(Tree root) {
        stack = new Stack<>();
        pushLeftNodes(root);
    }

    /**
     *  @return whether we have a next smallest number
     *  */
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    /**
     * @return the next smallest number
     * */
    public IEmployee next() {
        Tree node = stack.pop();
        pushLeftNodes(node.getRight());
        pushLeftNodes(node.getMid());
        return node.getNode();
    }

    private void pushLeftNodes(Tree node) {
        while (node != null) {
            stack.push(node);
            node = node.getLeft();
        }
    }
}