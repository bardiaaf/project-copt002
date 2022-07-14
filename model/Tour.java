package model;

import java.util.ArrayList;
import java.util.List;

public class Tour {
    private final List<Edge> edges = new ArrayList<>();
    private Edge[][] neighbors;

    public Tour(List<Edge> edges) {
        this.edges.addAll(edges);
        neighbors = new Edge[edges.size()][2];
        for (Edge edge : edges) {
            neighbors[edge.v.id][neighbors[edge.v.id][0] == null ? 0 : 1] = edge;
            neighbors[edge.u.id][neighbors[edge.u.id][0] == null ? 0 : 1] = edge;
        }
    }
}
