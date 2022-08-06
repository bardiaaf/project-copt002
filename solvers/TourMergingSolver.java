package solvers;

import algorithm.tourGenerators.FarthestInsertion;
import algorithm.tourGenerators.LinKernighan;
import algorithm.tourGenerators.NearestNeighbor;
import model.Graph;
import model.Tour;
import org.moeaframework.problem.tsplib.TSPInstance;

public class TourMergingSolver extends Solver {
    int rounds = 50;
    int innerRounds = 50;
    int k = 5;
    int l = 10;
    int k_cluster;
    double PRECISION = 0.5;
    int rounds2 = 100;

    public TourMergingSolver() {
        new TourMergingSolver(10, 50, 6, 15, 10);
    }

    public TourMergingSolver(int rounds, int innerRounds, int k, int k_cluster, int l){
        this.rounds = rounds;
        this.innerRounds = innerRounds;
        this.k = k;
        this.l = l;
        this.k_cluster = k_cluster;
//        this.PRECISION = PRECISION;
        rounds2 = rounds *2;
    }

    @Override
    public Tour solve(TSPInstance instance) {
        Graph graph = createGraph(instance);
        FarthestInsertion farthestInsertion = new FarthestInsertion(graph);
        Tour farthest = farthestInsertion.generateTour(graph);
        NearestNeighbor nearestNeighbor = new NearestNeighbor(graph);
        Tour nearest = nearestNeighbor.generateTour(graph);
        Tour clustering = getClusteringTour(graph, instance, innerRounds, k, l, k_cluster, 0.5);
        LinKernighan linKernighan = new LinKernighan(graph, farthestInsertion);
        Graph H = new Graph(farthest);
        H.add(nearest);
        H.add(clustering);
        for(int i=0;i<10;i++){
            farthest = linKernighan.generateTour(graph, farthest, rounds, l, l);
            nearest = linKernighan.generateTour(graph, nearest, rounds, k, l);
            clustering = linKernighan.generateTour(graph, clustering, rounds, k, l);
            H.add(farthest);
            H.add(nearest);
            H.add(clustering);
        }
        return linKernighan.generateTour(H, farthest, rounds*30, k, l);
    }
}
