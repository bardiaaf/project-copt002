package model;

import org.moeaframework.problem.tsplib.NodeCoordinates;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private Vertex[] vertices;
    private Edge[][] a;
    protected List<Edge>[] adj;
    private int size;

    public Graph() {
    }

    public Graph(Edge[][] a) {
        vertices = new Vertex[a.length];
        size = a.length;
        for (int i = 0; i < a.length; i++)
            vertices[i] = new Vertex(i);
        this.a = a;
        this.adj = new List[vertices.length];
        createAdj();
    }

    public Graph(List<Point> points) {
        size = points.size();
        vertices = new Vertex2D[size];
        for (int i = 0; i < size; i++)
            vertices[i] = new Vertex2D(i, points.get(i).x, points.get(i).y);
        this.a = new Edge[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (i == j)
                    a[i][j] = new Edge(vertices[i], vertices[j]);
                else
                    a[i][j] = new Edge(vertices[i], vertices[j], points.get(i).distance(points.get(j)));
        this.adj = new List[size];
        createAdj();
    }

    public Graph(NodeCoordinates nodeCoordinates) {
        this(Point.getPoints(nodeCoordinates));
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    private void createAdj() {
        for (Vertex v : vertices) {
            adj[v.id] = new ArrayList<>();
            for (Vertex u : vertices)
                if (hasEdge(v, u))
                    adj[v.id].add(a[v.id][u.id]);
            adj[v.id].sort(Edge::compareTo);
        }
    }

    public int getSize() {
        return size;
    }

    public List<Edge> nearestNeighbors(Vertex v, int l) {
        List<Edge> result = new ArrayList<>();
        if (adj[v.id].size() <= l) {
            result.addAll(adj[v.id]);
            return result;
        }

        for (int i = 0; i < l; i++)
            result.add(adj[v.id].get(i));
        return result;
    }

    public Edge getEdge(Vertex v, Vertex u) {
        return a[v.id][u.id];
    }

    public boolean hasEdge(Vertex v, Vertex u) {
        return a[v.id][u.id].weight != Edge.MAX;
    }

    public Vertex[] getRandomVertices(int l) {
        Vertex[] res = new Vertex[l];
        for (int i = 0; i < l; i++) {
            res[i] = this.getVertices()[i];
        }
        return res;
    }
}
