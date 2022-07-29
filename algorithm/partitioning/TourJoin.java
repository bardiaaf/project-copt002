package algorithm.partitioning;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class TourJoin {
    public static Cluster findClosest(Cluster cluster, List<Cluster> clusters) {
        double minDST = Double.MAX_VALUE;
        Cluster second = null;

        for (Cluster c : clusters) {
            if (c.id == cluster.id)
                continue;
            double dst = cluster.getCentroid().distance(c.getCentroid());
            if (dst < minDST) {
                minDST = dst;
                second = c;
            }
        }

        return second;
    }

    private static Vertex getEndNode(Graph graph, Tour T, Vertex2D v) {
        Vertex v1 = T.next(v);
        Vertex v2 = T.previous(v);
        if (graph.getEdge(v, v1).weight > graph.getEdge(v, v2).weight) {
            return graph.getVertex(v1.id);
        } else {
            return graph.getVertex(v2.id);
        }
    }

    private static Tour joinTours(Graph graph, Tour T1, Vertex start1, Vertex end1, Tour T2, Vertex start2, Vertex end2) {
        List<Edge> edges = new ArrayList<>();

        Edge e1 = graph.getEdge(start1, end1);
        for (Edge e : T1.edges)
            if (!e.equals(e1))
                edges.add(e);

        Edge e2 = graph.getEdge(start2, end2);
        for (Edge e : T2.edges)
            if (!e.equals(e2))
                edges.add(e);

        edges.add(graph.getEdge(start1, start2));
        edges.add(graph.getEdge(end1, end2));
        System.err.println(">>>>>"+T1.edges.size()+"+"+T2.edges.size()+"="+edges.size());

        System.err.println(start1.id+" "+end1.id);
        System.err.println(start2.id+" "+end2.id);
        return new Tour(edges, graph);
    }

    private static Cluster join(Graph graph, Cluster c1, Cluster c2) {
        Vertex2D v1 = c1.closestPoint(c2.getCentroid());
        Vertex2D v2 = c2.closestPoint(c1.getCentroid());

        Vertex end1 = getEndNode(graph, c1.getTour(), v1);
        Vertex end2 = getEndNode(graph, c2.getTour(), v2);

        List<Vertex2D> points = new ArrayList<>(c1.getPoints());
        points.addAll(c2.getPoints());
        Tour tour = joinTours(/*new SubGraph(points.toArray(new Vertex2D[0]), graph)*/ graph, c1.getTour(), v1, end1, c2.getTour(), v2, end2);


        Cluster cluster = new Cluster(c1.id, c1.getCentroid(), tour, points);
        cluster.updateCentroid();

        return cluster;
    }

    public static Tour joinClusterTours(Graph graph, List<Cluster> clusters) {
        while (clusters.size() > 1) {
            Cluster c1 = clusters.get(0);
            Cluster c2 = findClosest(c1, clusters);
            clusters.remove(c1);
            clusters.remove(c2);
            clusters.add(join(graph, c1, c2));
        }

        return clusters.get(0).getTour();
    }
}
