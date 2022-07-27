package algorithm.partitioning;

import model.Point;

public class Centroid {

    private Point coord;

    public final int id;

    public Centroid(int id, Double x, Double y) {
        this.coord = new Point(x, y);
        this.id = id;
    }

    public Point getCoord() {
        return coord;
    }

    public void changeLocation(Double x, Double y) {
        this.coord = new Point(x, y);
    }

}
