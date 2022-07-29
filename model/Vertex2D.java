package model;

public class Vertex2D extends Vertex {

    public final Point coord;
    public int clusterNumber;

    public Vertex2D(int id, double x, double y) {
        super(id);
        this.coord = new Point(x, y);

    }

    public void setClusterNumber(int num) {
        this.clusterNumber = num;
    }
}
