import algorithm.tourGenerators.FarthestInsertion;
import algorithm.tourGenerators.LinKernighan;
import model.Edge;
import model.Graph;
import model.Tour;
import model.Vertex;
import org.moeaframework.problem.tsplib.DistanceTable;
import org.moeaframework.problem.tsplib.TSPInstance;
import org.moeaframework.problem.tsplib.TSPPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
//        TSPInstance problem = new TSPInstance(new File("./data/tsp/pcb442.tsp"));
//        problem.addTour(new File("./data/tsp/pcb442.opt.tour"));
        TSPInstance problem = new TSPInstance(new File("./data/tsp/ym7663.tsp"));
//        problem.addTour(new File("./data/tsp/eil101.opt.tour"));
//        System.err.println(problem.getTours().get(0).distance(problem));
        DistanceTable distanceTable = problem.getDistanceTable();
        int n = distanceTable.listNodes().length;
        System.err.println(n);
//        System.err.println(problem.getTours().get(0).distance(problem));
        Edge[][] a = new Edge[n][n];
        for (int i=0;i<n;i++) {
            for (int j = i + 1; j < n; j++) {
                int w = (int) distanceTable.getDistanceBetween(i + 1, j + 1);
                a[i][j] = new Edge(new Vertex(i), new Vertex(j), w);
                a[j][i] = a[i][j];
            }
            a[i][i] = new Edge(new Vertex(i), new Vertex(i));
        }
        Graph.setInstance(a);
        LinKernighan tourGenerator = new LinKernighan(new FarthestInsertion());
        Tour pre = new FarthestInsertion().generateTour(Graph.getInstance());
        System.out.println(pre.tsplibFormat().distance(problem));
        Tour tour = tourGenerator.generateTour(Graph.getInstance(), 200, 6, 6);
        System.out.println(tour.tsplibFormat().distance(problem));
        TSPPanel panel = new TSPPanel(problem);
        panel.displayTour(tour.tsplibFormat(), Color.BLUE);
        JFrame frame = new JFrame(problem.getName());
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
