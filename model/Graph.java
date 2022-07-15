package model;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    public final Vertex[] vertices;
    public final Edge[][] a;
    public final List<Edge>[] adj;

    public Graph(Edge[][] a) {
        vertices = new Vertex[a.length];
        for (int i = 0; i < a.length; i++)
            vertices[i] = new Vertex(i);
        this.a = a;
        this.adj = new List[vertices.length];
        for (Vertex v: vertices) {
            adj[v.id] = new ArrayList<>();
            for(Vertex u: vertices)
                if(hasEdge(v,u))
                    adj[v.id].add(a[v.id][u.id]);
            adj[v.id].sort(Edge::compareTo);
        }
    }

    public Edge getEdge(Vertex v, Vertex u) {
        return a[v.id][u.id];
    }

    public boolean hasEdge(Vertex v, Vertex u) {
        return a[v.id][u.id].weight != Integer.MAX_VALUE;
    }
}
