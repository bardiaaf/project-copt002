package model;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

public class K_Exchange {
    private final Graph graph;
    public final List<Edge> blues = new ArrayList<>();
    public final List<Edge> reds = new ArrayList<>();
    public final List<Vertex> t = new ArrayList<>();
    private final Tour tour;
    private boolean isClosed;
    private final double gain;

    public K_Exchange(Graph graph, Tour tour, Vertex first, Vertex second) {
        this.graph = graph;
        this.tour = tour;
        reds.add(graph.getEdge(first, second));
        t.add(first);
        t.add(second);
        gain = 0;
        isClosed = false;
    }

    private K_Exchange(Graph graph, Tour tour, List<Edge> blues, List<Edge> reds, List<Vertex> t, Vertex t_n_1, Vertex t_n) {
        this.graph = graph;
        this.tour = tour;
        this.blues.addAll(blues);
        this.reds.addAll(reds);
        this.t.addAll(t);
        blues.add(graph.getEdge(t.get(t.size() - 1), t_n_1));
        reds.add(graph.getEdge(t_n_1, t_n));
        t.add(t_n_1);
        t.add(t_n);
        double tmp = 0;
        for (Edge red : reds)
            tmp += red.weight;
        for (Edge blue : blues)
            tmp -= blue.weight;
        this.gain = tmp;
        this.isClosed = false;
    }

    public K_Exchange add(Edge blue) {
        if (isClosed)
            return null;
        Vertex t_n_1 = blue.u.equals(t.get(t.size() - 1)) ? blue.v : blue.u;
        Vertex t_n = findNext(t_n_1);
        if (t_n == null)
            return null;
        if (containsRed(graph.getEdge(t_n, t_n_1)) || containsBlue(graph.getEdge(t.get(t.size() - 1), t_n_1)))
            return null;
        double deltaGain = deltaGain(t_n_1, t_n);
        if (deltaGain + gain > 0)
            return new K_Exchange(graph, tour, blues, reds, t, t_n_1, t_n);
        return null;
    }

    private double deltaGain(Vertex t_n_1, Vertex t_n) {
        if (reds.size() == 1)
            return (graph.getEdge(t.get(0), t.get(1)).weight -
                    graph.getEdge(t.get(1), t_n_1).weight +
                    graph.getEdge(t_n_1, t_n).weight -
                    graph.getEdge(t_n, t.get(0)).weight);
        return (graph.getEdge(t.get(0), t.get(t.size() - 1)).weight +
                graph.getEdge(t_n_1, t_n).weight -
                graph.getEdge(t.get(0), t_n).weight);
    }

    private Vertex findNext(Vertex t_n_1) {
        if (isConnected(t_n_1, tour.next(t_n_1)))
            return tour.next(t_n_1);
        if (isConnected(t_n_1, tour.previous(t_n_1)))
            return tour.previous(t_n_1);
        return null;
    }

    private boolean isConnected(Vertex t_n_1, Vertex t_n) {
        List<Vertex> points = new ArrayList<>(t);
        points.add(t_n_1);
        points.add(t_n);
        List<Edge> tmpblues = new ArrayList<>(blues);
        tmpblues.add(graph.getEdge(t_n, t.get(0)));
        tmpblues.add(graph.getEdge(t_n_1, t.get(t.size() - 1)));
        List<Edge> tmpreds = new ArrayList<>(reds);
        tmpreds.add(graph.getEdge(t_n_1, t_n));
        points.sort((Vertex v, Vertex u) -> (tour.getIndex(v) - tour.getIndex(u)));
        TreeMap<Vertex, List<Vertex>> adj = new TreeMap<>();
        int k = points.size();
        for (int i = 0; i < k; i++) {
            List<Vertex> nei = new ArrayList<>();
            Vertex vertex = points.get(i);
            Vertex prev = points.get((i - 1 + k) % k);
            Vertex next = points.get((i + 1) % k);
            if (!tmpreds.contains(graph.getEdge(vertex, prev)))
                nei.add(prev);
            if (!tmpreds.contains(graph.getEdge(vertex, next)))
                nei.add(next);
            for (Edge edge : tmpblues) {
                if (edge.v.equals(vertex))
                    nei.add(edge.u);
                if (edge.u.equals(vertex))
                    nei.add(edge.v);
            }
            adj.put(vertex, nei);
        }
        for (Vertex vertex : points)
            if (adj.get(vertex).size() != 2)
                return false;
        TreeSet<Vertex> visited = new TreeSet<>();
        Vertex cur = points.get(0);
        while (!visited.contains(cur)) {
            visited.add(cur);
            for (Vertex vertex : adj.get(cur))
                if (!visited.contains(vertex))
                    cur = vertex;
        }
        return (visited.size() == points.size());
    }

    public void close() {
        if (isClosed)
            return;
        isClosed = true;
        if (reds.size() == 1)
            reds.clear();
        else
            blues.add(graph.getEdge(t.get(t.size() - 1), t.get(0)));
    }

    public boolean containsRed(Edge edge) {
        for (Edge edge1 : reds)
            if (edge1.equals(edge))
                return true;
        return false;
    }

    public boolean containsBlue(Edge edge) {
        for (Edge edge1 : blues)
            if (edge1.equals(edge))
                return true;
        return false;
    }

    public double getGain() {
        if (reds.size() <= 1)
            return 0.0;
        double tmp = 0;
        for (Edge red : reds)
            tmp += red.weight;
        for (Edge blue : blues)
            tmp -= blue.weight;
        if (!isClosed)
            tmp -= graph.getEdge(t.get(0), t.get(t.size() - 1)).weight;
        return tmp;
    }

    public void print() {
        System.out.println("t:");
        for (Vertex v : t)
            System.out.print(v.id + "(" + tour.getIndex(v) + "), ");
        System.out.println("_________\nblues: ");
        for (Edge edge : blues)
            System.out.print("(" + edge.u.id + "," + edge.v.id + ") ");
        System.out.println("\nreds: ");
        for (Edge edge : reds)
            System.out.print("(" + edge.u.id + "," + edge.v.id + ") ");
        System.out.println("_________");
    }

    public boolean isClosed() {
        return isClosed;
    }
}
