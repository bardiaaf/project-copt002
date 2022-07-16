package algorithm;

import model.Edge;
import model.Tour;

import java.util.ArrayList;
import java.util.List;

public class TourGenerator {

    public static Tour NNTour(){
        List<Edge> edges = new ArrayList<>();

        // with nn algorithm add n-1 edges to this list and make  a tour

        Tour tour = new Tour(edges);
        return tour;
    }
}
