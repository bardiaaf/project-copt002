import algorithm.partitioning.Cluster;
import algorithm.partitioning.KMeans;
import algorithm.partitioning.TourJoin;
import algorithm.tourGenerators.FarthestInsertion;
import algorithm.tourGenerators.LinKernighan;
import algorithm.tourGenerators.NearestNeighbor;
import algorithm.tourGenerators.TourGenerator;
import model.*;
import model.Point;
import org.moeaframework.problem.tsplib.DistanceTable;
import org.moeaframework.problem.tsplib.NodeCoordinates;
import org.moeaframework.problem.tsplib.TSPInstance;
import org.moeaframework.problem.tsplib.TSPPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static String name;
    static String mode;
    static double best;
    static String filePath;
    static TSPInstance instance;
    static long startTime;

    public static void setArgs(String name,double best, String filePath, String mode) throws IOException {
        Main.name= name;
        Main.best = best;
        Main.filePath = filePath;
        instance = new TSPInstance(new File(filePath));
        startTime = System.currentTimeMillis();
        Main.mode = mode;
    }

    public static Tour test(String secondaryTourGenerator, int rounds, int k_linKernighan, int l) throws IOException {
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
        }
        else{
            tourGenerator = new LinKernighan(graph, new NearestNeighbor(graph));
        }
        return tourGenerator.generateTour(graph, rounds, k_linKernighan, l);

    }

    public static Tour test_withCluster(String secondaryTourGenerator, int rounds, int innerRounds, int k_linKernighan, int l, int k_cluster, double PRECISIOJN) throws IOException {
        // init problem
        TSPInstance problem = new TSPInstance(new File(filePath));
        DistanceTable distanceTable = problem.getDistanceTable();

        List<Point> points = Point.getPoints((NodeCoordinates) distanceTable);
        Graph graph = new Graph(points);
        List<Vertex2D> allPoints = new ArrayList<>();

        for (int i=0;i<points.size();i++) {
            allPoints.add(new Vertex2D(i, points.get(i).x, points.get(i).y));
        }

        // cluster
        KMeans kMeans = new KMeans(k_cluster, PRECISIOJN,allPoints);
        List<Cluster> clusters = kMeans.kmMeans();

        for (Cluster cluster:
             clusters) {
            // set tours
            SubGraph subGraph = new SubGraph(cluster, graph);
            LinKernighan tourGenerator = new LinKernighan(subGraph, new NearestNeighbor(subGraph));
            Tour tour = tourGenerator.generateTour(subGraph, innerRounds, k_linKernighan, l);
            cluster.setTour(tour);
        }
        Tour tour = TourJoin.joinClusterTours(graph, clusters);
        tour = new LinKernighan(graph, null).generateTour(graph, tour, rounds,k_linKernighan,l);
        return tour;
    }

    public static Tour solve() throws IOException {
        if(instance.getDistanceTable().getClass().equals(NodeCoordinates.class))
            if(mode.equals("Q"))
//                return test_withCluster("FarthestInsertion", 100, 40, 3, 5, 10, 0.5);
                return test("FarthestInsertion", 100, 3, 5);
            else if(mode.equals("N"))
                return test_withCluster("FarthestInsertion", 300, 50,4, 8, 20, 0.5);
            else return test_withCluster("FarthestInsertion", 500, 50, 5, 10, 20, 0.5);
        else
            if(mode.equals("Q"))
                return test("FarthestInsertion", 100,3,5);
            else if(mode.equals("N"))
                return test("FarthestInsertion", 200,4,8);
            else
                return test("FarthestInsertion", 500,5,10);
    }

    public static void represent(Tour tour) {
        System.out.println("Tour cost: " + tour.tsplibFormat().distance(instance));
        if(best!=0.0) System.out.println("approx: " + (tour.tsplibFormat().distance(instance) / best));
        System.out.println("time: " + (System.currentTimeMillis() - startTime) / 1000L);
        if (instance.getDistanceTable().getClass().equals(NodeCoordinates.class)) {
            TSPPanel panel = new TSPPanel(instance);
            panel.displayTour(tour.tsplibFormat(), Color.BLUE);
            JFrame frame = new JFrame(instance.getName());
            frame.getContentPane().setLayout(new BorderLayout());
            frame.getContentPane().add(panel, BorderLayout.CENTER);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(500, 400);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }

    }


    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter .tsp file direction: ");
        String address = sc.nextLine();
        System.out.println("Please enter the test name: ");
        String name = sc.nextLine();
        System.out.println("Enter search mode:\n(Q) Quick scan\n(N) Normal scan\n(C) Complete scan");
        String mode = sc.nextLine();
        System.out.println("Please enter the optimal tour weight: (enter 0 if you are not sure)");
        double best = sc.nextDouble();
        setArgs(name,best,address+name+".tsp", mode);
        Tour tour = solve();
        represent(tour);
//        setArgs("ym7663",238314,"./data/tsp/"+name+".tsp");
//        setFilePath("./data/tsp/"+name+".tsp");
//        setFilePath("./data/tsp/"+name+".tsp");
    }

}
