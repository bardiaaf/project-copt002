package model;

public class Edge {
    public final Vertex v, u;
    public final int weight;

    public Edge(Vertex v, Vertex u, int weight) {
        this.v = v;
        this.u = u;
        this.weight = weight;
    }

    public Edge(Vertex v, Vertex u) {
        this.v = v;
        this.u = u;
        this.weight = Integer.MAX_VALUE;
    }
}
