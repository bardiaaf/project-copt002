package algorithm.partitioning;

import model.Edge;
import model.Graph;
import model.Tour;
import model.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TourJoin {

    private List<Vertex2D> allPoints;
    private int k;
    private List<Centroid> centroids;

    private ArrayList<Integer> labels;


    public TourJoin(int k,List<Centroid> centroids, List<Vertex2D> allPoints){
        this.k=k;
        this.centroids= centroids;
        this.allPoints = allPoints;
        this.labels = new ArrayList<>();

        for (int i=0;i<k;i++)
            labels.add(i);
    }

    public Centroid findClosest(Centroid centroid){
        Double minDST = Double.MAX_VALUE;
        Centroid second = null;

        for (Centroid c:
             centroids) {

            if (c.id==centroid.id)
                continue;

            if (!labels.contains(c.id))
                continue;

            Double dst = Centroid.centroidDistance(centroid, c);

            if (dst < minDST){
                minDST = dst;
                second = c;
            }
        }

        return second;
    }

    public Vertex2D closestPoint(Centroid C1, int label2){
        Double minDst = Double.MAX_VALUE;
        Vertex2D vertex2D = null;


        for (Vertex2D v:
             allPoints) {
            if (v.clusterNumber==label2){
                Double dst = Centroid.distance(v, C1);
                if (dst<minDst){
                    minDst = dst;
                    vertex2D = v;
                }
            }
        }

        return vertex2D;
    }

    private Vertex2D getEndNode(Tour T, Vertex2D v){
        Vertex v1 = T.next(v);
        Vertex v2 = T.previous(v);
        if (Graph.getInstance().getEdge(v, v1).weight > Graph.getInstance().getEdge(v, v2).weight ){
            return new Vertex2D(v1.id);
        }
        else{
            return new Vertex2D(v2.id);
        }
    }

    private Tour joinTours(Tour T1, Vertex start1, Vertex end1, Tour T2, Vertex start2, Vertex end2){
        List<Edge> edges = new ArrayList<>();

        Edge e1 = new Edge(start1, end1);
        for (Edge e:
             T1.edges) {
            if (e.equals(e1))
                continue;
            edges.add(e);
        }

        Edge e2 = new Edge(start2, end2);
        for (Edge e:
                T2.edges) {
            if (e.equals(e2))
                continue;
            edges.add(e);
        }

        edges.add(new Edge(start1, start2));
        edges.add(new Edge(end1, end2));

        return new Tour(edges);
    }

    private void updateLabels(int oldIndex, int newIndex){
        for (Vertex2D v:
             allPoints) {
            if (v.clusterNumber==oldIndex)
                v.clusterNumber=newIndex;
        }
    }

    public Tour join(Tour T1,  Centroid C1,Tour T2, Centroid C2){
        Vertex2D v1 = closestPoint(C2, C1.id);
        Vertex2D v2 = closestPoint(C1, C2.id);

        Vertex2D end1 = getEndNode(T1, v1);
        Vertex2D end2 = getEndNode(T2, v2);

        // update lists
        labels.remove(C2.id);
        updateLabels(C2.id, C1.id);

        return joinTours(T1, v1, end1, T2, v2, end2);
    }


    public void joinIteration(HashMap<Integer, Tour> tours){

        while (labels.size()>1){
            int i = labels.get(0);

            Centroid C1 = centroids.get(i);
            Centroid C2 = findClosest(C1);
            Tour tour = join(tours.get(C1.id), C1,tours.get(C2.id), C2);

            tours.remove(C1.id);
            tours.remove(C2.id);
            tours.put(C1.id, tour);

        }
    }


}
