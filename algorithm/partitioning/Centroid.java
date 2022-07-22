package algorithm.partitioning;

public class Centroid {

    public Double x;
    public Double y;

    public int id;


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
