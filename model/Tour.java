package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Tour {
    private final Graph graph;
    public final List<Edge> edges = new ArrayList<>();
    private final Map<Vertex,Vertex> next;
    private final Map<Vertex,Vertex> previous;

    private final Map<Vertex,Integer> rank;

    public Tour(List<Edge> edges, Graph graph) {
        this.graph = graph;
        previous = new TreeMap<>();
        next = new TreeMap<>();
        Map<Vertex, Vertex[]> neighbors = new TreeMap<>();
        for(Edge edge: edges) {
            neighbors.put(edge.v, new Vertex[2]);
            neighbors.put(edge.u, new Vertex[2]);
        }
        for (Edge edge : edges) {
            neighbors.get(edge.v)[(neighbors.get(edge.v)[0] == null) ? 0 : 1] = edge.u;
            neighbors.get(edge.u)[(neighbors.get(edge.u)[0] == null) ? 0 : 1] = edge.v;
        }
        Vertex prev = edges.get(0).v;
        Vertex cur = neighbors.get(prev)[0];
        next.put(prev, cur);
        previous.put(cur, prev);
        this.edges.add(graph.getEdge(prev, cur));
        while (this.edges.size() < edges.size()) {
            Vertex tmp = cur;
            cur = neighbors.get(cur)[prev.equals(neighbors.get(cur)[0]) ? 1 : 0];
            prev = tmp;
            next.put(prev, cur);
            previous.put(cur, prev);
            this.edges.add(graph.getEdge(cur, tmp));
        }
        int[] nodes = getNodes();
        rank = new TreeMap<>();
        for (int i = 0; i < edges.size(); i++)
            rank.put(graph.getVertex(nodes[i]), i);
    }

    public org.moeaframework.problem.tsplib.Tour tsplibFormat() {
        org.moeaframework.problem.tsplib.Tour tour = new org.moeaframework.problem.tsplib.Tour();
        int[] res = this.getNodes();
        for (int i = 0; i < res.length; i++)
            res[i]++;
        tour.fromArray(res);
        return tour;
    }

    public int[] getNodes() {
        int[] res = new int[edges.size()];
        int tmp = edges.get(0).v.id;
        int init = tmp;
        int index = 0;
        do {
            res[index] = tmp;
            index++;
            tmp = next.get(graph.getVertex(tmp)).id;
        } while (tmp != init);
        return res;
    }

    public int getIndex(Vertex vertex) {
        return rank.get(vertex);
    }

    public Vertex next(Vertex vertex) {
        return next.get(vertex);
    }

    public Vertex previous(Vertex vertex) {
        return previous.get(vertex);
    }

    public Tour applyKExchange(K_Exchange exchange) {
        ArrayList<Edge> tmp = new ArrayList<>();
        for (Edge edge : this.edges) {
            if (!exchange.containsRed(edge))
                tmp.add(edge);
        }
        tmp.addAll(exchange.blues);
        return new Tour(tmp, this.graph);
    }

    public Graph getGraph() {
        return graph;
    }
}
