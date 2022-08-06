package solvers;

import algorithm.tourGenerators.FarthestInsertion;
import algorithm.tourGenerators.LinKernighan;
import algorithm.tourGenerators.NearestNeighbor;
import model.Graph;
import model.Tour;
import org.moeaframework.problem.tsplib.TSPInstance;

import java.awt.event.PaintEvent;

public class MostPromisingSolver extends Solver {
    int rounds = 50;
    int innerRounds = 100;
    int k = 5;
    int l = 10;
    int k_cluster=16;
    double PRECISION = 0.5;
    int rounds2 = 100;

    public MostPromisingSolver() {
        new MostPromisingSolver(rounds, innerRounds, k, k_cluster, l);
    }

    public MostPromisingSolver(int rounds, int innerRounds, int k, int k_cluster, int l){
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
        LinKernighan linKernighan = new LinKernighan(graph, farthestInsertion);

        final Tour[] farthest = {null};
        Thread t1 = new Thread(() -> {
            farthest[0] = farthestInsertion.generateTour(graph);
            farthest[0] = linKernighan.generateTour(graph, farthest[0], rounds, k, l);
        });
        t1.start();


        final Tour[] nearest = {null};
        Thread t2 = new Thread(() -> {
            NearestNeighbor nearestNeighbor = new NearestNeighbor(graph);
            nearest[0] = nearestNeighbor.generateTour(graph);
            nearest[0] = linKernighan.generateTour(graph, nearest[0], rounds, k, l);
        });
        t2.start();


        final Tour[] clustering = {null};
        Thread t3 = new Thread(() -> {
            clustering[0] = getClusteringTour(graph, instance, innerRounds, k+2, l, k_cluster, PRECISION);
            clustering[0] = linKernighan.generateTour(graph, clustering[0], rounds, k, l);
        });
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Tour res;
        double clusteringWeight = clustering[0].tsplibFormat().distance(instance);
        double nearestWeight = nearest[0].tsplibFormat().distance(instance);
        double farthestWeight = farthest[0].tsplibFormat().distance(instance);
        if(clusteringWeight <= Math.min(nearestWeight, farthestWeight))
            res = clustering[0];
        else if(nearestWeight <= farthestWeight)
            res = nearest[0];
        else res = farthest[0];

        return linKernighan.generateTour(graph, res, 300, 5, 10);
    }
}
