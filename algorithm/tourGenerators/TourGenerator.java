package algorithm.tourGenerators;

import model.Edge;
import model.Graph;
import model.Tour;

import java.util.ArrayList;
import java.util.List;

public abstract class TourGenerator {
    protected final Graph graph;

    public TourGenerator() {
        this.graph = Graph.getInstance();
    }

    public abstract Tour generateTour(Graph graph);
}
