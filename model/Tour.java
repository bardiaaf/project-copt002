package model;

import java.util.ArrayList;
import java.util.List;

public class Tour {
    private final Graph graph;
    public final List<Edge> edges = new ArrayList<>();
    private final Vertex[] next;
    private final Vertex[] previous;

    private final int[] rank;

    public Tour(List<Edge> edges, Graph graph) {
        this.graph = graph;
        previous = new Vertex[edges.size()];
        next = new Vertex[edges.size()];
        Vertex[][] neighbors = new Vertex[edges.size()][2];
        for (Edge edge : edges) {
            neighbors[edge.v.id][(neighbors[edge.v.id][0] == null) ? 0 : 1] = edge.u;
            neighbors[edge.u.id][(neighbors[edge.u.id][0] == null) ? 0 : 1] = edge.v;
        }
        Vertex prev = new Vertex(0);
        Vertex cur = neighbors[0][0];
        next[prev.id] = cur;
        previous[cur.id] = prev;
        this.edges.add(graph.getEdge(prev, cur));
        while (this.edges.size() < edges.size()) {
            Vertex tmp = cur;
            cur = neighbors[cur.id][prev.equals(neighbors[cur.id][0]) ? 1 : 0];
            prev = tmp;
            next[prev.id] = cur;
            previous[cur.id] = prev;
            this.edges.add(graph.getEdge(cur, tmp));
        }
        int[] nodes = getNodes();
        rank = new int[edges.size()];
        for (int i = 0; i < edges.size(); i++)
            rank[nodes[i]] = i;
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
        int tmp = 0;
        int index = 0;
        do {
            res[index] = tmp;
            index++;
            tmp = next[tmp].id;
        } while (tmp != 0);
        return res;
    }

    public int getIndex(Vertex vertex) {
        return rank[vertex.id];
    }

    public Vertex next(Vertex vertex) {
        return next[vertex.id];
    }

    public Vertex previous(Vertex vertex) {
        return previous[vertex.id];
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
}
