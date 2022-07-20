//import org.moeaframework.problem.tsplib.TSPInstance;
//import org.moeaframework.problem.tsplib.Tour;
//
//import java.io.File;
//import java.io.IOException;
//
//public class Test {
//    public static void main(String[] args) throws IOException {
//        TSPInstance problem = new TSPInstance(new File("./data/tsp/pcb442.tsp"));
//        problem.addTour(new File("./data/tsp/pcb442.opt.tour"));
//
//        for (Tour tour : problem.getTours()) {
//            System.out.println(tour.distance(problem));
//        }
//    }
//
//}
