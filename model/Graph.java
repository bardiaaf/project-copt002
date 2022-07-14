package model;

import java.util.List;

public class Graph {
    public final Vertex[] vertices;
    public final Edge[][] a;

    public Graph(Edge[][] a) {
        vertices = new Vertex[a.length];
        for (int i = 0; i < a.length; i++)
            vertices[i] = new Vertex(i);
        this.a = a;
    }

    public Edge getEdge(Vertex v, Vertex u) {
        return a[v.id][u.id];
    }

    public boolean hasEdge(Vertex v, Vertex u) {
        return a[v.id][u.id].weight != Integer.MAX_VALUE;
    }
}
