package algorithm.partitioning;

import java.util.*;

public class KMeans {

    private List<Vertex2D> allPoints;
    private Double PRECISION;
    private int k;

    public KMeans(int k, Double PRECISION){
        this.PRECISION =PRECISION;
        this.k = k;
    }


    // distance
    public Double distance(Vertex2D v, Centroid c){
         return Math.pow(v.x -c.x,2)+Math.pow(v.y -c.y, 2);
    }


    // centroids
    public List<Centroid> randomCentroids() {
        List<Centroid> centroids = new ArrayList<>();
        Random rand = new Random(allPoints.size());

        for(int i=1; i< k ; i++){
            Vertex2D v = allPoints.get(rand.nextInt());
            Centroid centroid = new Centroid(i, v.x+0.0, v.y+0.0);
            centroids.add(centroid);
        }
        return centroids;
    }

    public void setNewClusterNumbers(List<Centroid> centroids){

        for (int i=0;i<allPoints.size();i++){
            Double minDist = Double.MAX_VALUE;
            int index = -1;

            for (int j=0;j<centroids.size();j++) {
                Double dist =distance(allPoints.get(i), centroids.get(j));

                if (dist<minDist){
                    minDist = dist;
                    index = j;
                }
            }

            allPoints.get(i).setClusterNumber(index);
        }
    }

    public Double renewCenters(List<Centroid> centroids){
        // init clusters
        List<ArrayList<Vertex2D>> clusters = new ArrayList<>();
        for (Vertex2D v:
             allPoints) {
            clusters.get(v.clusterNumber).add(v);
        }


        // compute center
        for (int i=0; i<k; i++){
            Double X = 0.0 ;
            Double Y = 0.0;
            int n =  clusters.get(i).size();

            for (Vertex2D v:
                 clusters.get(i)) {
                X += v.x;
                Y += v.y;
            }
            centroids.get(i).changeLocation(X/n, Y/n);
        }

        ArrayList<Double> values = new ArrayList<>();
        ArrayList<Double> means2 = new ArrayList<>();

        int n=0;
        Double ALL = 0.0;

        for (int i=0;i<k;i++){
            Double val = 0.0;
            for (Vertex2D v:
                    clusters.get(i)) {
                val += distance(v, centroids.get(i));
            }
            ALL +=val;
            n++;

            values.add(val);
        }
        Double mean = ALL/n;


        Double SSE=0.0;
        for (Double val:
             values) {
            SSE += Math.pow(val-mean, 2);
        }
        return SSE;
    }



    // kMeans algorithm
    public void kmMeans(){
        // Select K initial centroids
        List<Centroid> centroids = randomCentroids();

        // Initialize Sum of Squared Errors to max, we'll lower it at each iteration
        Double SSE = Double.MAX_VALUE;

        while (true) {
            // Assign observations to centroids
            setNewClusterNumbers(centroids);

            // Recompute centroids according to new cluster assignments
            Double newSSE = renewCenters(centroids);

            // Exit condition, SSE changed less than PRECISION parameter
            if(SSE-newSSE <= PRECISION){
                break;
            }
            SSE = newSSE;
        }
    }



}
