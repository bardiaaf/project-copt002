package model;

import java.util.ArrayList;
import java.util.List;

public class SubGraph extends Graph{
    private Vertex[] remainingVertices;
    private Graph superGraph;

    public SubGraph(Vertex[] remainingVertices, Graph graph) {
        this.remainingVertices = remainingVertices;
        this.superGraph = graph;
        adj = new List[remainingVertices.length];
        for (Vertex v : remainingVertices) {
            adj[v.id] = new ArrayList<>();
            for (Vertex u : remainingVertices)
                if (hasEdge(v, u))
                    adj[v.id].add(getEdge(v, u));
            adj[v.id].sort(Edge::compareTo);
        }
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
