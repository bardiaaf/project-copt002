package algorithm.tourGenerators;

import model.Edge;
import model.Graph;
import model.Tour;
import model.Vertex;

import java.util.ArrayList;
import java.util.List;

public class NearestNeighbor extends TourGenerator {
    public NearestNeighbor(Graph graph) {
        super(graph);
    }

    @Override
    public Tour generateTour(Graph graph) {
        List<Edge> edges = new ArrayList<>();
        List<Vertex> visited = new ArrayList<>();
        Vertex cur = graph.getRandomVertices(1)[0];
        visited.add(cur);
        while (edges.size() < graph.getSize()) {
            Vertex min = null;
            for (Vertex vertex : graph.getVertices())
                if (!visited.contains(vertex)) {
                    if (min == null)
                        min = vertex;
                    else if (graph.getEdge(vertex, cur).weight < graph.getEdge(min, cur).weight)
                        min = vertex;
                }
            if (min == null)
                break;
            edges.add(graph.getEdge(cur, min));
            visited.add(min);
            cur = min;
        }
        edges.add(graph.getEdge(cur, visited.get(0)));
        return new Tour(edges, graph);
    }
}
