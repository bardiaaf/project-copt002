import model.*;
import org.moeaframework.problem.tsplib.NodeCoordinates;
import org.moeaframework.problem.tsplib.TSPInstance;
import org.moeaframework.problem.tsplib.TSPPanel;
import solvers.MostPromisingSolver;
import solvers.PartitioningSolver;
import solvers.TourMergingSolver;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    static String name;
    static String mode;
    static double best;
    static String filePath;
    static TSPInstance instance;
    static long startTime;

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

    public static void getInput() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter .tsp file direction: ");
        String address = sc.nextLine();
        System.out.println("Please enter the test name: ");
        Main.name = sc.nextLine();
        System.out.println("Enter search mode:\n(Q) Quick scan\n(N) Normal scan\n(C) Complete scan");
        Main.mode = sc.nextLine();
        System.out.println("Please enter the optimal tour weight: (enter 0 if you are not sure)");
        Main.best = sc.nextDouble();
        Main.filePath = address+name+".tsp";
        Main.instance = new TSPInstance(new File(filePath));
        Main.startTime = System.currentTimeMillis();
    }
    public static void main(String[] args) throws IOException {
        getInput();
//        Tour tour = new TourMergingSolver().solve(instance);
//        Tour tour = new MostPromisingSolver().solve(instance);
        Tour tour = new PartitioningSolver(PartitioningSolver.Mode.fromString(mode)).solve(instance);
        represent(tour);
//        setArgs("ym7663",238314,"./data/tsp/"+name+".tsp");
//        setFilePath("./data/tsp/"+name+".tsp");
//        setFilePath("./data/tsp/"+name+".tsp");
    }

}
