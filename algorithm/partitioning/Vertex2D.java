package algorithm.partitioning;

import model.Vertex;

public class Vertex2D extends Vertex {

    public final int x;
    public final int y;
    public int clusterNumber;

//    public Vertex2D(int id) {
//        super(id);
//    }

    public Vertex2D(int id, int x, int y){
        super(id);
        this.x = x;
        this.y = y;

    }


    public void setClusterNumber(int num){
        this.clusterNumber = num;
    }



}
