package algorithm.tourGenerators;

import model.Edge;
import model.Graph;
import model.Tour;

import java.util.ArrayList;
import java.util.List;

public abstract class TourGenerator {

    public abstract Tour generateTour(Graph graph);
}
