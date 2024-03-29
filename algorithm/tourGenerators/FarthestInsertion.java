package algorithm.tourGenerators;

import model.Edge;
import model.Graph;
import model.Tour;
import model.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class FarthestInsertion extends TourGenerator {
    public FarthestInsertion(Graph graph) {
        super(graph);
    }

    @Override
    public Tour generateTour(Graph graph) {
        Vertex[] rnd = graph.getRandomVertices(2);
        Vertex s = rnd[0], t = rnd[1];
        for (Vertex v : graph.getVertices())
            for (Vertex u : graph.getVertices())
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
        for (Vertex vertex : graph.getVertices())
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
        return new Tour(new ArrayList<>(edges), graph);
    }

    private double insertionCost(Edge edge, Vertex v) {
        return graph.getEdge(edge.v, v).weight + graph.getEdge(edge.u, v).weight - edge.weight;
    }
}
