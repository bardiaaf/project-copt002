package model;

public class OneTree {
    public final Edge e1, e2;
    private final Tree tree;

    public final Vertex one;

    public OneTree(Edge e1, Edge e2, Vertex one, Tree tree) {
        this.e1 = e1;
        this.e2 = e2;
        this.tree = tree;
        this.one = one;
    }

    public Tree getTree() {
        return tree;
    }
}
