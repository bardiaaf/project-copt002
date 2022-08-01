package solvers;

import model.Tour;
import org.moeaframework.problem.tsplib.NodeCoordinates;
import org.moeaframework.problem.tsplib.TSPInstance;

public class PartitioningSolver extends Solver {
    public final Mode mode;

    public PartitioningSolver(Mode mode) {
        this.mode = mode;
    }

    public Tour solve(TSPInstance instance) {
        if (instance.getDistanceTable().getClass().equals(NodeCoordinates.class))
            if (mode.equals(Mode.QUICK))
                return solveLinKernighan(instance, "FarthestInsertion", 100, 3, 5);
            else if (mode.equals(Mode.NORMAL))
                return solveWithClustering(instance, "FarthestInsertion",
                        300, 50, 4, 8, 20, 0.5);
            else return solveWithClustering(instance, "FarthestInsertion",
                        500, 50, 5, 10, 20, 0.5);
        else if (mode.equals(Mode.QUICK))
            return solveLinKernighan(instance, "FarthestInsertion", 100, 3, 5);
        else if (mode.equals(Mode.NORMAL))
            return solveLinKernighan(instance, "FarthestInsertion", 200, 4, 8);
        else
            return solveLinKernighan(instance, "FarthestInsertion", 500, 5, 10);
    }

    public enum Mode{
        QUICK, NORMAL, COMPLETE;

        public static Mode fromString(String str) {
            if(str.equals("Q"))
                return QUICK;
            if(str.equals("N"))
                return NORMAL;
            return COMPLETE;
        }
    }
}
