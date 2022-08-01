package model;

import org.moeaframework.problem.tsplib.NodeCoordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Graph {
    private Vertex[] vertices;
    private Edge[][] a;
    protected TreeMap<Vertex, List<Edge>> adj;
    private int size;

    public Graph() {
    }

    public Graph(Edge[][] a) {
        vertices = new Vertex[a.length];
        size = a.length;
        for (int i = 0; i < a.length; i++)
            vertices[i] = new Vertex(i);
        this.a = a;
        this.adj = new TreeMap<>();
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
        this.adj = new TreeMap<>();
        createAdj();
    }

    public Graph(NodeCoordinates nodeCoordinates) {
        this(Point.getPoints(nodeCoordinates));
    }

    public Graph(Tour tour) {
        this.vertices = tour.getGraph().getVertices();
        int size = vertices.length;
        adj = new TreeMap<>();
        for (Vertex v : vertices)
            adj.put(v, new ArrayList<>());
        this.a = new Edge[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                a[i][j] = new Edge(vertices[i], vertices[j]);
        for (Edge edge: tour.edges){
            a[edge.v.id][edge.u.id] = edge;
            a[edge.u.id][edge.v.id] = edge;
            adj.get(edge.u).add(edge);
            adj.get(edge.v).add(edge);
        }
        for (Vertex v : vertices)
            adj.get(v).sort(Edge::compareTo);
    }

    public void add(Tour tour) {
        for (Edge edge: tour.edges)
            addEdge(edge);
    }

    private void addEdge(Edge edge) {
        if(a[edge.u.id][edge.v.id].equals(edge))
            return;
        a[edge.u.id][edge.v.id] = edge;
        a[edge.v.id][edge.u.id] = edge;
        for (int i = 0; i < adj.get(edge.v).size(); i++) {
            Edge e = adj.get(edge.v).get(i);
            if(e.weight > edge.weight) {
                adj.get(edge.v).add(i, edge);
                break;
            }
            else if(i == adj.get(edge.v).size()-1) {
                adj.get(edge.v).add(edge);
                break;
            }
        }
        for (int i = 0; i < adj.get(edge.u).size(); i++) {
            Edge e = adj.get(edge.u).get(i);
            if(e.weight > edge.weight) {
                adj.get(edge.u).add(i, edge);
                break;
            }
            else if(i == adj.get(edge.u).size()-1) {
                adj.get(edge.u).add(edge);
                break;
            }
        }
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    private void createAdj() {
        for (Vertex v : vertices) {
            adj.put(v, new ArrayList<>());
            for (Vertex u : vertices)
                if (hasEdge(v, u))
                    adj.get(v).add(a[v.id][u.id]);
            adj.get(v).sort(Edge::compareTo);
        }
    }

    public int getSize() {
        return size;
    }

    public List<Edge> nearestNeighbors(Vertex v, int l) {
        List<Edge> result = new ArrayList<>();
        List<Edge> tmp = adj.get(v);
        if (tmp.size() <= l) {
            result.addAll(tmp);
            return result;
        }

        for (int i = 0; i < l; i++)
            result.add(tmp.get(i));
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

    public Vertex getVertex(int id) {
        return vertices[id];
    }
}
