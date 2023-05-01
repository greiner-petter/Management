package de.ostfalia.aud.s23ss.base;

import java.util.Arrays;
import java.util.Comparator;

/**
 * A binary tree with int values.
 */
public class Tree {

    /**
     * The value of this node in the tree.
     */
    private IEmployee node;

    /**
     * The left subtree.
     */
    private Tree lhs;

    /**
     * The right subtree.
     */
    private Tree rhs;

    private Tree mid;

    private Comparator<IEmployee> comparator;

    private TreeIterator iterator;

    /**
     * Creates a new tree with the value node.
     * @param node The value of this node in the tree.
     */
    public Tree(IEmployee node, Comparator<IEmployee> comparator) {
        this.node = node;
        this.lhs = null;
        this.rhs = null;
        this.mid = null;
        this.comparator = comparator;
    }

    /**
     * Method to add a node to the tree. Duplicates will be refused.
     *
     * @param insert the new number
     */
    public void add(IEmployee insert) {
        if ((comparator.compare(insert, node) < 0) && (lhs == null)) {
            lhs = new Tree(insert, comparator);
        } else if ((comparator.compare(insert, node) < 0) && (lhs != null)) {
            lhs.add(insert);
        }
        if ((comparator.compare(insert, node) > 0) && (rhs == null)) {
            rhs = new Tree(insert, comparator);
        } else if ((comparator.compare(insert, node) > 0) && (rhs != null)) {
            rhs.add(insert);
        }
        if ((comparator.compare(insert, node) == 0) && (mid == null)) {
            mid = new Tree(insert, comparator);
        } else if ((comparator.compare(insert, node) == 0) && (mid != null)) {
            mid.add(insert);
        }
    }

    /**
     * Method to calculate the depth of a tree. It is defined by the maximal
     * number of edges to a leaf.
     *
     * @return the depth of the tree
     */
    public int depth() {
        int depth = 0;
        int left = 0;
        int right = 0;
        if (lhs == null && rhs == null) {
            return 0;
        }
        if (lhs != null) {
            left = lhs.depth();
        }
        if (rhs != null) {
            right = rhs.depth();
        }
        if (left > right) {
            depth = left + 1;
        } else {
            depth = right + 1;
        }
        return depth;
    }

    public int size() {
        return size(this);
    }
    private int size(Tree node) {
        if (node == null) {
            return 0;
        }
        else {
            return (size(node.lhs) + 1 + size(node.mid) + size(node.rhs));
        }
    }

    public IEmployee search(int key){
        IEmployee wanted = null;
        if (key == node.getKey()) {
            return node;
        } else if ((key < node.getKey()) && (lhs != null)) {
            wanted = lhs.search(key);
        } else if ((key > node.getKey()) && (rhs != null)) {
            wanted = rhs.search(key);
        }
        return wanted;
    }

    public Tree search(String name, String firstName) {
        Tree wantedTree = null;
        if (name.equals(node.getName()) && firstName.equals(node.getFirstName())) {
            trim();
            return this;
        } else if ((name.compareTo(node.getName()) < 0) && (lhs != null)) {
            wantedTree = lhs.search(name, firstName);
        } else if ((name.compareTo(node.getName()) > 0) && (rhs != null)) {
            wantedTree = rhs.search(name, firstName);
        }
        if (wantedTree != null) {
            wantedTree.trim();
        }
        return wantedTree;
    }

    public Tree search(Department department) {
        Tree wantedTree = null;
        if (department.equals(node.getDepartment())) {
            trim();
            return this;
        } else if ((department.compareTo(node.getDepartment()) < 0) && (lhs != null)) {
            wantedTree = lhs.search(department);
        } else if ((department.compareTo(node.getDepartment()) > 0) && (rhs != null)) {
            wantedTree = rhs.search(department);
        }
        if (wantedTree != null) {
            wantedTree.trim();
        }
        return wantedTree;
    }

    private void trim() {
        lhs = null;
        rhs = null;
    }

    public Tree getLeft() {
        return lhs;
    }

    public Tree getMid() {
        return mid;
    }

    public Tree getRight() {
        return rhs;
    }

    public IEmployee getNode() {
        return node;
    }

    public IEmployee[] toArray(Tree tree) {
        int i = 0;
        iterator = new TreeIterator(tree);

        IEmployee[] result = new IEmployee[0];
        while (iterator.hasNext()) {
            if (i == result.length) {
                result = Arrays.copyOf(result, result.length + 1);
            }
            result[i] = iterator.next();
            i++;
        }
        return result;
    }


    /**
     * Method that formats the tree into a readable string.
     *
     * @return the formatted string
     */
    public String toString() {
        StringBuilder visualTree = new StringBuilder();
        if (lhs == null && rhs == null) {
            visualTree.append(node);
            return visualTree.toString();
        }
        if (lhs != null) {
            visualTree.append("(").append(lhs.toString()).append(")");
            if (rhs == null) {
                visualTree.append(node);
            }
            if (lhs != null && rhs != null) {
                visualTree.append(node);
            }
        }
        if (rhs != null) {
            if (lhs == null) {
                visualTree.append(node);
            }
            visualTree.append("(").append(rhs.toString()).append(")");
        }
        return visualTree.toString();
    }
}