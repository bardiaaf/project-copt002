package model;

import org.moeaframework.problem.misc.Lis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Tree {
    private Map<Vertex, Vertex> par;
    private Vertex root;

    private final List<Vertex> topoSort = new ArrayList<>();

    public Tree(Vertex root) {
        par = new TreeMap<>();
        this.root = root;
        topoSort.add(root);
    }

    public void add(Vertex in, Vertex out) {
        par.put(out, in);
        topoSort.add(out);
    }

    public Vertex getPar(Vertex v) {
        return par.get(v);
    }

    public Vertex getRoot() {
        return root;
    }

    public List<Vertex> getTopoSort(){
        return topoSort;
    }
}
