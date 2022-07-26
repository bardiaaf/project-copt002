package algorithm.partitioning;

public class Centroid {

    public Double x;
    public Double y;

    public int id;


    public static Double centroidDistance(Centroid c1, Centroid c2){
        return Math.pow(c1.x -c2.x,2)+Math.pow(c1.y -c2.y, 2);
    }

    public static Double distance(Vertex2D v, Centroid c){
        return Math.pow(v.x -c.x,2)+Math.pow(v.y -c.y, 2);
    }




    public Centroid(int id, Double x, Double y){
        this.x= x;
        this.y = y;
        this.id = id;
    }


    public void changeLocation(Double x, Double y){
        this.x = x;
        this.y=y;
    }

}
