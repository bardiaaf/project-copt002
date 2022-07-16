package model;

import java.util.ArrayList;
import java.util.List;

public class K_Exchange {
    private static Graph graph;
    public final List<Edge> blues = new ArrayList<>();
    public final List<Edge> reds = new ArrayList<>();
    public final List<Vertex> t = new ArrayList<>();
    private final Tour tour;
    private final boolean direction;
    private final int gain;

    public K_Exchange(Graph graph, Tour tour, Vertex first, Vertex second) {
        setGraph(graph);
        this.tour = tour;
        reds.add(graph.getEdge(first, second));
        t.add(first);
        t.add(second);
        gain = graph.getEdge(first,second).weight;
        direction = tour.next(first).equals(second);
    }

    private K_Exchange(Tour tour, boolean direction,
                       List<Edge> blues, List<Edge> reds, List<Vertex> t,
                       Vertex t_n_1, Vertex t_n, int gain) {
        this.tour = tour;
        this.direction = direction;
        this.blues.addAll(blues);
        this.reds.addAll(reds);
        this.t.addAll(t);
        blues.add(graph.getEdge(t.get(t.size()-1), t_n_1));
        reds.add(graph.getEdge(t_n_1, t_n));
        t.add(t_n_1);
        t.add(t_n);
        this.gain = gain;
    }

    public K_Exchange add(Edge blue) {
        Vertex t_n_1 = blue.u.equals(t.get(t.size()-1)) ? blue.v : blue.u;
        Vertex t_n = direction ? tour.next(t_n_1) : tour.previous(t_n_1);
        int deltaGain = deltaGain(t_n_1, t_n);
        if(deltaGain + gain > 0)
            return new K_Exchange(tour, direction, blues, reds, t, t_n_1, t_n, gain + deltaGain);
        return null;
    }

    private int deltaGain(Vertex t_n_1, Vertex t_n) {
        if(reds.size() == 1)
            return (-1 * graph.getEdge(t.get(1),t_n_1).weight +
                    graph.getEdge(t_n_1,t_n).weight -
                    graph.getEdge(t_n,t.get(0)).weight);
        return (graph.getEdge(t.get(0),t.get(t.size()-1)).weight +
                graph.getEdge(t_n_1,t_n).weight -
                graph.getEdge(t.get(0),t_n).weight);
    }

    public void close(){
        if(reds.size() == 1)
            reds.clear();
        else
            blues.add(graph.getEdge(t.get(t.size()-1),t.get(0)));
    }

    public boolean containsRed(Edge edge) {
        return reds.contains(edge);
    }

    public boolean containsBlue(Edge edge) {
        return blues.contains(edge);
    }

    public static void setGraph(Graph graph) {
        if(K_Exchange.graph == null)
            K_Exchange.graph = graph;
    }

    public int getGain() {
        return gain;
    }
}
