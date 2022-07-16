package model;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    public static Graph graph = null;

    public final Vertex[] vertices;
    public final Edge[][] a;
    public final List<Edge>[] adj;
    public final int size;

    public Graph(Edge[][] a) {
        //singletone
        this.graph = this;

        vertices = new Vertex[a.length];
        size = a.length;
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


    public int getSize(){
        return size;
    }
    public Vertex getVertex(int i){
        return vertices[i];
    }

    public List<Edge> nearestNeighbors(Vertex v){
        List<Edge> result= new ArrayList<>();
        if (adj[v.id].size()<=5){
            result.addAll(adj[v.id]);
            return result;
        }

        for (int i=0;i<5;i++)
            result.add(adj[v.id].get(i));
        return result;
    }

    public Edge getEdge(Vertex v, Vertex u) {
        return a[v.id][u.id];
    }

    public boolean hasEdge(Vertex v, Vertex u) {
        return a[v.id][u.id].weight != Integer.MAX_VALUE;
    }
}
