package solvers;

import algorithm.partitioning.Cluster;
import algorithm.partitioning.KMeans;
import algorithm.partitioning.TourJoin;
import algorithm.tourGenerators.FarthestInsertion;
import algorithm.tourGenerators.LinKernighan;
import algorithm.tourGenerators.NearestNeighbor;
import model.*;
import org.moeaframework.problem.tsplib.DistanceTable;
import org.moeaframework.problem.tsplib.NodeCoordinates;
import org.moeaframework.problem.tsplib.TSPInstance;

import java.util.ArrayList;
import java.util.List;

public abstract class Solver {
    public abstract Tour solve(TSPInstance instance);

    public static Tour getClusteringTour(TSPInstance problem, int innerRounds,
                                         int k_linKernighan, int l, int k_cluster, double PRECISION) {
        // init problem
        DistanceTable distanceTable = problem.getDistanceTable();

        List<Point> points = Point.getPoints((NodeCoordinates) distanceTable);
        Graph graph = new Graph(points);
        List<Vertex2D> allPoints = new ArrayList<>();

        for (int i=0;i<points.size();i++) {
            allPoints.add(new Vertex2D(i, points.get(i).x, points.get(i).y));
        }

        // cluster
        KMeans kMeans = new KMeans(k_cluster, PRECISION,allPoints);
        List<Cluster> clusters = kMeans.kmMeans();

        for (Cluster cluster:
                clusters) {
            // set tours
            SubGraph subGraph = new SubGraph(cluster, graph);
            //TODO: maybe farthest
            LinKernighan tourGenerator = new LinKernighan(subGraph, new NearestNeighbor(subGraph));
            Tour tour = tourGenerator.generateTour(subGraph, innerRounds, k_linKernighan, l);
            cluster.setTour(tour);
        }
        return TourJoin.joinClusterTours(graph, clusters);
    }

    public static Tour solveWithClustering(TSPInstance problem, String secondaryTourGenerator, int rounds,
                                        int innerRounds, int k_linKernighan, int l, int k_cluster, double PRECISION) {
        DistanceTable distanceTable = problem.getDistanceTable();

        List<Point> points = Point.getPoints((NodeCoordinates) distanceTable);
        Graph graph = new Graph(points);
        Tour tour = getClusteringTour(problem, innerRounds, k_linKernighan, l, k_cluster, PRECISION);
        tour = new LinKernighan(graph, null).generateTour(graph, tour, rounds,k_linKernighan,l);
        return tour;
    }

    public static Tour solveLinKernighan(TSPInstance problem, String secondaryTourGenerator,
                                         int rounds, int k_linKernighan, int l) {
        Graph graph = createGraph(problem);

        LinKernighan tourGenerator;
        Tour pre;

        // lin kernighan
        if (secondaryTourGenerator.equals("FarthestInsertion")) {
            tourGenerator = new LinKernighan(graph, new FarthestInsertion(graph));
        }
        else{
            tourGenerator = new LinKernighan(graph, new NearestNeighbor(graph));
        }
        return tourGenerator.generateTour(graph, rounds, k_linKernighan, l);
    }

    public static Graph createGraph(TSPInstance problem) {
        // init problem
        DistanceTable distanceTable = problem.getDistanceTable();
        int n = distanceTable.listNodes().length;

        // create graph
        Edge[][] a = new Edge[n][n];
        for (int i=0;i<n;i++) {
            for (int j = i + 1; j < n; j++) {
                int w = (int) distanceTable.getDistanceBetween(i + 1, j + 1);
                a[i][j] = new Edge(new Vertex(i), new Vertex(j), w);
                a[j][i] = a[i][j];
            }
            a[i][i] = new Edge(new Vertex(i), new Vertex(i));
        }

        return new Graph(a);
    }
}
