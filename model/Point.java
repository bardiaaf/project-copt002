package model;

import org.moeaframework.problem.tsplib.NodeCoordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Point {
    public final double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distance(Point point) {
        return Math.sqrt((x - point.x) * (x - point.x) + (y - point.y) * (y - point.y));
    }

    public static List<Point> getPoints(NodeCoordinates nodeCoordinates) {
        int[] idList = nodeCoordinates.listNodes();
        List<Point> points = new ArrayList<>();
        for(int id: idList)
            points.add(new Point(nodeCoordinates.get(id).getPosition()[0], nodeCoordinates.get(id).getPosition()[0]));
        return points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0;
    }
}
