package model;

import org.moeaframework.problem.tsplib.NodeCoordinates;

import java.util.*;

public class Graph {
    private Vertex[] vertices;
    private Edge[][] a;
    protected TreeMap<Vertex, List<Edge>> adj;
    protected TreeMap<Vertex, List<Edge>> alphaNearness;
    private int size;
    protected double[][] alpha;
    private boolean isAlphaComputed = false;

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
        if(isAlphaComputed)
            return nearestAlphaNeighbors(v,l);
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

    public OneTree minOneTree() {
        return minOneTree(this.getRandomVertices(1)[0]);
    }
    public OneTree minOneTree(Vertex one) {
        Tree tree = getMSTWithoutOne(one);
        Edge e1 = null, e2 = null;
        for(Vertex v: this.getVertices())
            if(!v.equals(one)) {
                Edge edge = getEdge(v, one);
                if(e1 == null)
                    e1 = edge;
                else if(e1.weight > edge.weight) {
                    e2 = e1;
                    e1 = edge;
                }
                else if (e2 == null)
                    e2 = edge;
                else if (e2.weight > edge.weight)
                    e2 = edge;
            }
        return new OneTree(e1, e2, one, tree);
    }

    protected Tree getMSTWithoutOne(Vertex one) {
        TreeMap<Edge, Vertex> remaining = new TreeMap<>();
        Vertex[] vs = this.getRandomVertices(2);
        Vertex root = vs[0].equals(one)?vs[1]:vs[0];
        Tree tree = new Tree(root);
        for(Vertex vertex:getVertices())
            if(!vertex.equals(root) && !vertex.equals(one))
                remaining.put(getEdge(vertex, root), vertex);
        List<Vertex> updatedVertices = new ArrayList<>();
        List<Edge> updatedEdges = new ArrayList<>();
        while (remaining.size() > 0) {
            Vertex first = remaining.firstEntry().getValue();
            Edge edge = remaining.firstEntry().getKey();
            tree.add(edge.u.equals(first) ? edge.v : edge.u, first);
            remaining.remove(remaining.firstEntry().getKey());
            for (Map.Entry<Edge, Vertex> entry: remaining.entrySet()) {
                if(entry.getKey().weight > getEdge(first, entry.getValue()).weight) {
                    updatedVertices.add(entry.getValue());
                    updatedEdges.add(entry.getKey());
                }
            }
            for (int i = 0; i < updatedVertices.size(); i++) {
                remaining.remove(updatedEdges.get(i));
                remaining.put(getEdge(updatedVertices.get(i), first), updatedVertices.get(i));
            }
        }
        return tree;
    }

    protected void calculateAlphaNearness() {
        isAlphaComputed = true;
        OneTree oneTree = minOneTree();
        alpha = new double[vertices.length][vertices.length];
        for (int i = 0; i < vertices.length; i++)
            for (int j = 0; j < vertices.length; j++)
                alpha[i][j] = 0;
        for(Edge edge: adj.get(oneTree.one))
            if(!edge.equals(oneTree.e1) && !edge.equals(oneTree.e2)) {
                alpha[edge.v.id][edge.u.id] = edge.weight - oneTree.e2.weight;
                alpha[edge.u.id][edge.v.id] = edge.weight - oneTree.e2.weight;
            }
        List<Vertex> seq = oneTree.getTree().getTopoSort();
        for (int i = 0; i < seq.size(); i++) {
            Vertex vi = seq.get(i);
            alpha[vi.id][vi.id] = Edge.MAX;
            for (int j = 0; j < i+1; j++) {
                Vertex vj = seq.get(j);
                Vertex parvj = oneTree.getTree().getPar(vj);
                alpha[vi.id][vj.id] = getEdge(vi, vj).weight -
                        Math.max(getEdge(vj, parvj).weight, getEdge(vj, parvj).weight-alpha[vi.id][parvj.id]);
            }
        }
        for(Vertex vertex: vertices) {
            alphaNearness.put(vertex, new ArrayList<>(adj.get(vertex)));
            alphaNearness.get(vertex).sort((e1, e2) -> {
                if(alpha[e1.v.id][e1.u.id]==alpha[e2.v.id][e2.u.id])
                    return 0;
                return (alpha[e1.v.id][e1.u.id]<alpha[e2.v.id][e2.u.id] ? -1: 1);
            });
        }
    }

    public List<Edge> nearestAlphaNeighbors(Vertex v, int l) {
        List<Edge> result = new ArrayList<>();
        List<Edge> tmp = alphaNearness.get(v);
        if (tmp.size() <= l) {
            result.addAll(tmp);
            return result;
        }

        for (int i = 0; i < l; i++)
            result.add(tmp.get(i));
        return result;
    }
}
