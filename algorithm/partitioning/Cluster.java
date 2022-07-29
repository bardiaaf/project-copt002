package algorithm.partitioning;

import model.Point;
import model.Tour;
import model.Vertex2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cluster {
    private final List<Vertex2D> points = new ArrayList<>();
    public final int id;
    private Point centroid;
    private Tour tour;

    public Cluster(int id, Point centroid, Tour tour, List<Vertex2D> points) {
        this.id = id;
        this.centroid = centroid;
        this.tour = tour;
        this.points.addAll(points);
    }

    public Cluster(int id, Point centroid, List<Vertex2D> points){
        this.id = id;
        this.centroid = centroid;
        this.points.addAll(points);
    }

    public void setTour(Tour tour){
        this.tour = tour;
    }

    public int getSize(){
        return points.size();
    }

    public void addPoint(List<Vertex2D> newPoints){
        points.addAll(newPoints);
    }


    public List<Vertex2D> getPoints() {
        return points;
    }

    public Point getCentroid() {
        return centroid;
    }

    public Tour getTour() {
        return tour;
    }

    public void setCentroid(double x, double y) {
        setCentroid(new Point(x, y));
    }

    public void setCentroid(Point point) {
        this.centroid = point;
    }

    public Vertex2D closestPoint(Point c) {
        double minDst = Double.MAX_VALUE;
        Vertex2D vertex2D = null;
        for (Vertex2D v : points) {
            double dst = v.coord.distance(c);
            if (dst < minDst) {
                minDst = dst;
                vertex2D = v;
            }
        }
        return vertex2D;
    }

    public void updateCentroid(){

        double X = 0.0;
        double Y = 0.0;
        double n = points.size();

        for (Vertex2D v : points) {
                X += v.coord.x;
                Y += v.coord.y;
        }

        centroid = new Point(X/n, Y/n);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cluster cluster = (Cluster) o;
        return id == cluster.id;
    }
}
