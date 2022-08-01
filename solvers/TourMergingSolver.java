package solvers;

import algorithm.tourGenerators.FarthestInsertion;
import algorithm.tourGenerators.LinKernighan;
import algorithm.tourGenerators.NearestNeighbor;
import model.Graph;
import model.Tour;
import org.moeaframework.problem.tsplib.TSPInstance;

public class TourMergingSolver extends Solver {
    @Override
    public Tour solve(TSPInstance instance) {
        Graph graph = createGraph(instance);
        FarthestInsertion farthestInsertion = new FarthestInsertion(graph);
        Tour farthest = farthestInsertion.generateTour(graph);
        NearestNeighbor nearestNeighbor = new NearestNeighbor(graph);
        Tour nearest = nearestNeighbor.generateTour(graph);
        Tour clustering = getClusteringTour(instance, 50, 10, 5, 50, 0.5);
        LinKernighan linKernighan = new LinKernighan(graph, farthestInsertion);
        Graph H = new Graph(farthest);
        H.add(nearest);
        H.add(clustering);
        for(int i=0;i<10;i++){
            farthest = linKernighan.generateTour(graph, farthest, 10, 5, 5);
            nearest = linKernighan.generateTour(graph, nearest, 10, 5, 5);
            clustering = linKernighan.generateTour(graph, clustering, 10, 5, 5);
            H.add(farthest);
            H.add(nearest);
            H.add(clustering);
        }
        return linKernighan.generateTour(H, farthest, 300, 5, 10);
    }
}
