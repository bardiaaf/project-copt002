package model;

import java.util.ArrayList;
import java.util.List;

public class Tour {
    private static Graph graph;
    private final List<Edge> edges = new ArrayList<>();
    private final Vertex[] next;
    private final Vertex[] previous;

    public Tour(List<Edge> edges) {
        previous = new Vertex[edges.size()];
        next = new Vertex[edges.size()];
        Vertex[][] neighbors = new Vertex[edges.size()][2];
        for (Edge edge : edges) {
            neighbors[edge.v.id][neighbors[edge.v.id][0] == null ? 0 : 1] = edge.u;
            neighbors[edge.u.id][neighbors[edge.u.id][0] == null ? 0 : 1] = edge.v;
        }
        Vertex prev = new Vertex(0);
        Vertex cur = neighbors[0][0];
        next[prev.id] = cur;
        previous[cur.id] = prev;
        this.edges.add(graph.getEdge(prev,cur));
        while(this.edges.size()<edges.size()){
            Vertex tmp = cur;
            cur = neighbors[cur.id][prev.equals(neighbors[cur.id][0]) ? 1 : 0];
            prev = tmp;
            next[prev.id] = cur;
            previous[cur.id] = prev;
            this.edges.add(graph.getEdge(cur,tmp));
        }
    }

    public static void setGraph(Graph graph) {
        if(Tour.graph == null)
            Tour.graph = graph;
    }

    public Vertex next(Vertex vertex){
        return next[vertex.id];
    }

    public Vertex previous(Vertex vertex){
        return previous[vertex.id];
    }

    public Tour applyKExchange(K_Exchange exchange) {
        ArrayList<Edge> edges = new ArrayList<>();
        for(Edge edge: this.edges)
            if(!exchange.containsRed(edge))
                edges.add(edge);
        edges.addAll(exchange.blues);
        return new Tour(edges);
    }
}
