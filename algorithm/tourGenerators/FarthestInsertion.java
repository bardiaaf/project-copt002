package algorithm.tourGenerators;

import model.Edge;
import model.Graph;
import model.Tour;
import model.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class FarthestInsertion extends TourGenerator {
    @Override
    public Tour generateTour(Graph graph) {
        Vertex s = new Vertex(0), t = new Vertex(1);
        for (Vertex v : graph.vertices)
            for (Vertex u : graph.vertices)
                if (!v.equals(u) && graph.getEdge(v, u).weight < graph.getEdge(s, t).weight) {
                    s = v;
                    t = u;
                }
        TreeSet<Edge> edges = new TreeSet<>();
        edges.add(graph.getEdge(s, t));
        TreeSet<Vertex> selectedVertices = new TreeSet<>();
        selectedVertices.add(s);
        selectedVertices.add(t);
        TreeSet<Edge> remaining = new TreeSet<>();
        for (Vertex vertex : graph.vertices)
            if (!vertex.equals(s) && !vertex.equals(t)) {
                Edge es = graph.getEdge(s, vertex), et = graph.getEdge(t, vertex);
                remaining.add(es.weight < et.weight ? es : et);
            }
        while (!remaining.isEmpty()) {
            Edge e = remaining.last();
            Vertex v = selectedVertices.contains(e.v) ? e.u : e.v;
            selectedVertices.add(v);
            List<Edge> deletions = new ArrayList<>();
            List<Edge> additions = new ArrayList<>();
            remaining.remove(e);
            for (Edge edge : remaining) {
                Vertex u = selectedVertices.contains(edge.v) ? edge.u : edge.v;
                Edge newEdge = graph.getEdge(u, v);
                if (edge.weight > newEdge.weight) {
                    deletions.add(edge);
                    additions.add(newEdge);
                }
            }
            deletions.forEach(remaining::remove);
            remaining.addAll(additions);
            Edge min = edges.first();
            for (Edge edge : edges)
                if (insertionCost(edge, v) < insertionCost(min, v))
                    min = edge;
            if (edges.size() > 1)
                edges.remove(min);
            edges.add(graph.getEdge(min.v, v));
            edges.add(graph.getEdge(min.u, v));
        }
        return new Tour(new ArrayList<>(edges));
    }

    private double insertionCost(Edge edge, Vertex v) {
        return graph.getEdge(edge.v, v).weight + graph.getEdge(edge.u, v).weight - edge.weight;
    }
}
