import model.Tour;
import org.moeaframework.problem.tsplib.TSPInstance;
import solvers.MostPromisingSolver;
import solvers.TourMergingSolver;

import java.io.*;



public class MLTest {


    private static void logging(String secondaryTourGenerator, int rounds,
                                      int innerRounds, int k_linKernighan, int l, int k_cluster,
                                      double PRECISION, double time, double tourApprox) {
        try {
            File directory = new File("./");
            final String filePath = directory.getAbsolutePath() + "TOURMERGE.log";
            File file = new File(filePath);
            if (!file.exists())
                file.createNewFile();


            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)));

            String message = "time="+ time +", approx=" + tourApprox+", secondaryTourGenerator="+secondaryTourGenerator+", rounds="+rounds+
                    ", k=" + k_linKernighan + ", l="+ l + ", k_cluster=" +k_cluster + ", PRECISION="+PRECISION+ ", innerRounds="+innerRounds;
            out.println(message);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void represent(double best ,String secondaryTourGenerator, int rounds,
                                 int innerRounds, int k_linKernighan, int l, int k_cluster,
                                 double PRECISION,Tour tour, TSPInstance instance, double startTime) {

        logging(secondaryTourGenerator, rounds, innerRounds, k_linKernighan, l,
                k_cluster, PRECISION,(System.currentTimeMillis() - startTime) / 1000L ,(tour.tsplibFormat().distance(instance) / best));
    }

    public static void main(String[] args) {

        String name = "ca4663";
        double best = 1290319;
        TSPInstance instance= null;
        String filePath = "data/tsp/"+name+".tsp";
        try {
             instance=new TSPInstance(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        long startTime;


        for (int k=4 ; k< 6;k++){
            for (int rounds = 10 ; rounds < 30 ;rounds++) {
                for (int innerRounds=40 ; innerRounds<60 ; innerRounds++) {
                    for (int k_cluster = 8; k_cluster < 20; k_cluster++) {
                        for (int l = 7; l < 10; l++) {
                            System.out.println("testing = "+k+", "+rounds+", "+innerRounds+", "+k_cluster+", "+l);
                            startTime=System.currentTimeMillis();
//                            Tour tour = new MostPromisingSolver(rounds, innerRounds, k, k_cluster, l).solve(instance);

                            Tour tour = new TourMergingSolver(rounds, innerRounds, k, k_cluster, l).solve(instance);

                            represent( best ,"", rounds,
                             innerRounds,  k,  l,  k_cluster,
                             0.5, tour,  instance,  startTime);
                        }
                        k_cluster+=2;
                    }
                    innerRounds+=5;
                }
                rounds +=5;
            }
        }
    }


}
