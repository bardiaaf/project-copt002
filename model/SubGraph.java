package model;

import algorithm.partitioning.Cluster;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class SubGraph extends Graph{
    private final Vertex[] remainingVertices;
    private final Graph superGraph;

    public SubGraph(Vertex[] remainingVertices, Graph graph) {
        this.remainingVertices = remainingVertices;
        this.superGraph = graph;
        adj = new TreeMap<>();
        for (Vertex v : remainingVertices) {
            adj.put(v, new ArrayList<>());
            for (Vertex u : remainingVertices)
                if (hasEdge(v, u))
                    adj.get(v).add(getEdge(v, u));
            adj.get(v).sort(Edge::compareTo);
        }
    }

    public SubGraph(Cluster cluster, Graph graph) {
        this(cluster.getPoints().toArray(new Vertex2D[0]), graph);
    }

    @Override
    public Vertex[] getVertices() {
        return remainingVertices;
    }

    @Override
    public int getSize() {
        return remainingVertices.length;
    }

    @Override
    public List<Edge> nearestNeighbors(Vertex v, int l) {
        return super.nearestNeighbors(v, l);
    }

    @Override
    public Edge getEdge(Vertex v, Vertex u) {
        return superGraph.getEdge(v, u);
    }

    @Override
    public boolean hasEdge(Vertex v, Vertex u) {
        return superGraph.hasEdge(v, u);
    }

    @Override
    public Vertex getVertex(int id) {
        return superGraph.getVertex(id);
    }
}
