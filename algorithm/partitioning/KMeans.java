package algorithm.partitioning;

import algorithm.tourGenerators.TourGenerator;
import model.Edge;
import model.Point;
import model.Vertex2D;

import java.util.*;

public class KMeans {

    private final List<Vertex2D> allPoints = new ArrayList<>();
    private final double PRECISION;
    private final int k;

    public KMeans(int k, double PRECISION, List<Vertex2D> points) {
        this.PRECISION = PRECISION;
        this.k = k;
        this.allPoints.addAll(points);
    }

    // centroids
    public List<Centroid> randomCentroids() {
        List<Centroid> centroids = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < k; i++) {
            Vertex2D v = allPoints.get(rand.nextInt(allPoints.size()));
            Centroid centroid = new Centroid(i, v.coord.x + 0.0, v.coord.y + 0.0);
            centroids.add(centroid);
        }
        return centroids;
    }

    public void setNewClusterNumbers(List<Centroid> centroids) {

        for (Vertex2D point : allPoints) {
            double minDist = Double.MAX_VALUE;
            int index = -1;

            for (int j = 0; j < centroids.size(); j++) {
                double dist = point.coord.distance(centroids.get(j).getCoord());

                if (dist < minDist) {
                    minDist = dist;
                    index = j;
                }
            }

            point.setClusterNumber(index);
        }

    }

    public double renewCenters(List<Centroid> centroids) {
        // init clusters
        List<ArrayList<Vertex2D>> clusters = new ArrayList<>();

        for (int i = 0; i < k; i++)
            clusters.add(new ArrayList<>());

        for (Vertex2D v : allPoints)
            clusters.get(v.clusterNumber).add(v);


        // compute center
        for (int i = 0; i < k; i++) {
            double X = 0.0;
            double Y = 0.0;
            double n = clusters.get(i).size() * 1.0;

            for (Vertex2D v : clusters.get(i)) {
                X += v.coord.x;
                Y += v.coord.y;
            }
            centroids.get(i).changeLocation(X / n, Y / n);
        }

        ArrayList<Double> values = new ArrayList<>();

        double n = 0.0;
        double ALL = 0.0;

        for (int i = 0; i < k; i++) {
            double val = 0.0;
            for (Vertex2D v : clusters.get(i))
                val += v.coord.distance(centroids.get(i).getCoord());
            ALL += val;
            n = n + 1.0;

            values.add(val);
        }
        double mean = ALL / n;


        double SSE = 0.0;
        for (Double val : values)
            SSE += Math.pow(val - mean, 2);
        return SSE;
    }

    public List<Cluster> deleteSmallClusters(List<Cluster> clusters){

        for (int i=0;i<clusters.size();i++){
            if (clusters.get(i).getSize() <3){
                joinClusters(clusters.get(i), clusters);
                i=0;
            }
        }

        return clusters;
    }

    public void joinClusters(Cluster small, List<Cluster> clusters){
        double minDst = Edge.MAX;
        Point centroid = small.getCentroid();
        Cluster target = null;

        for (Cluster c:
             clusters) {
            double dst = centroid.distance(c.getCentroid());
            if (!c.equals(small) && dst<minDst){
                target = c;
            }
        }

        clusters.remove(small);
        target.addPoint(small.getPoints());

    }

    public List<Cluster> getClusters(List<Centroid> centroids){
        List<ArrayList<Vertex2D>> list = new ArrayList<>();
        for (int i=0;i<k;i++){
            list.add(new ArrayList<>());
        }

        for (Vertex2D v:
             allPoints) {
            list.get(v.clusterNumber).add(v);
        }


        List<Cluster> clusters = new ArrayList<>();
        for (int i=0;i<k;i++){
           clusters.add(new Cluster(i, centroids.get(i).getCoord(), list.get(i)));
        }


        return deleteSmallClusters(clusters);
    }


    // kMeans algorithm
    public List<Cluster> kmMeans() {
        // Select K initial centroids
        List<Centroid> centroids = randomCentroids();

        // Initialize Sum of Squared Errors to max
        double SSE = Double.MAX_VALUE;

        while (true) {
            // Assign observations to centroids
            setNewClusterNumbers(centroids);

            // Recompute centroids according to new cluster assignments
            double newSSE = renewCenters(centroids);

            // Exit condition, SSE changed less than PRECISION parameter
            if (SSE - newSSE <= PRECISION)
                break;
            SSE = newSSE;
        }

        return getClusters(centroids);

    }


}
