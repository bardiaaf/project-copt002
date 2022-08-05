package solvers;

import algorithm.tourGenerators.FarthestInsertion;
import algorithm.tourGenerators.LinKernighan;
import algorithm.tourGenerators.NearestNeighbor;
import model.Graph;
import model.Tour;
import org.moeaframework.problem.tsplib.TSPInstance;

public class MostPromisingSolver extends Solver {
    int rounds = 100;
    int innerRounds;
    int k;
    int k_cluster;
    double PRECISION = 0.5;

    @Override
    public Tour solve(TSPInstance instance) {
        Graph graph = createGraph(instance);

        FarthestInsertion farthestInsertion = new FarthestInsertion(graph);
        LinKernighan linKernighan = new LinKernighan(graph, farthestInsertion);
        Tour farthest = farthestInsertion.generateTour(graph);
        farthest = linKernighan.generateTour(graph, farthest, 100, 5, 10);
        NearestNeighbor nearestNeighbor = new NearestNeighbor(graph);
        Tour nearest = nearestNeighbor.generateTour(graph);
        nearest = linKernighan.generateTour(graph, nearest, 100, 5, 10);
        Tour clustering = getClusteringTour(graph, instance, 100, 10, 5, 50, PRECISION);
        clustering = linKernighan.generateTour(graph, clustering, 50, 5, 10);
        Tour res;
        double clusteringWeight = clustering.tsplibFormat().distance(instance);
        double nearestWeight = nearest.tsplibFormat().distance(instance);
        double farthestWeight = farthest.tsplibFormat().distance(instance);
        if(clusteringWeight <= Math.min(nearestWeight, farthestWeight))
            res = clustering;
        else if(nearestWeight <= farthestWeight)
            res = nearest;
        else res = farthest;
        return linKernighan.generateTour(graph, res, 300, 5, 10);
    }
}
