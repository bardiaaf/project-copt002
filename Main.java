import algorithm.partitioning.Cluster;
import algorithm.partitioning.KMeans;
import algorithm.partitioning.TourJoin;
import algorithm.tourGenerators.FarthestInsertion;
import algorithm.tourGenerators.LinKernighan;
import algorithm.tourGenerators.NearestNeighbor;
import algorithm.tourGenerators.TourGenerator;
import model.*;
import org.moeaframework.problem.tsplib.DistanceTable;
import org.moeaframework.problem.tsplib.NodeCoordinates;
import org.moeaframework.problem.tsplib.TSPInstance;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    static String name;
    static double best;
    static String filePath;

    public static void setArgs(String name,double best, String filePath){
        Main.name= name;
        Main.best = best;
        Main.filePath = filePath;
    }


    public static void test(String secondaryTourGenerator, int rounds, int k_linKernighan, int l) throws IOException {
        // init problem
        TSPInstance problem = new TSPInstance(new File(filePath));
        DistanceTable distanceTable = problem.getDistanceTable();
        int n = distanceTable.listNodes().length;

        long startTime = System.currentTimeMillis();

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

        Graph graph = new Graph(a);

        LinKernighan tourGenerator;
        Tour pre;

        // lin kernighan
        if (secondaryTourGenerator.equals("FarthestInsertion")) {
            tourGenerator = new LinKernighan(graph, new FarthestInsertion(graph));
            pre = new FarthestInsertion(graph).generateTour(graph);

        }
        else{
            tourGenerator = new LinKernighan(graph, new NearestNeighbor(graph));
            pre = new NearestNeighbor(graph).generateTour(graph);
        }

        System.out.println(pre.tsplibFormat().distance(problem));

        Tour tour = tourGenerator.generateTour(graph, rounds, k_linKernighan, l);
        System.out.println(tour.tsplibFormat().distance(problem));
        System.out.println("name: " + name);
        System.out.println("approx: " + (tour.tsplibFormat().distance(problem) / best));
        System.out.println("time: " + (System.currentTimeMillis() - startTime) / 1000L);

    }


    public void test_withCluster(String secondaryTourGenerator, int rounds, int k_linKernighan, int l, int k_cluster, double PRECISIOJN) throws IOException {
        // init problem
        TSPInstance problem = new TSPInstance(new File(filePath));
        DistanceTable distanceTable = problem.getDistanceTable();
        int n = distanceTable.listNodes().length;

        long startTime = System.currentTimeMillis();

        List<Point> points = Point.getPoints((NodeCoordinates) distanceTable);
        Graph graph = new Graph(points);
        List<Vertex2D> allpoints = new ArrayList<>();

        for (int i=0;i<points.size();i++) {
            allpoints.add(new Vertex2D(i, points.get(i).x, points.get(i).y));
        }

        // cluster
        KMeans kMeans = new KMeans(k_cluster, PRECISIOJN,allpoints);
        List<Cluster> clusters = kMeans.kmMeans();

        for (Cluster cluster:
             clusters) {
            // set tours
            SubGraph subGraph = new SubGraph(cluster, graph);
            LinKernighan tourGenerator = new LinKernighan(subGraph, new NearestNeighbor(subGraph));
            Tour tour = tourGenerator.generateTour(subGraph, rounds, k_linKernighan, l);
            cluster.setTour(tour);
        }


        Tour tour = TourJoin.joinClusterTours(graph, clusters);
        System.out.println(tour.tsplibFormat().distance(problem));
        System.out.println("name: " + name);
        System.out.println("approx: " + (tour.tsplibFormat().distance(problem) / best));
        System.out.println("time: " + (System.currentTimeMillis() - startTime) / 1000L);
    }


    public static void main(String[] args) {

    }

}
