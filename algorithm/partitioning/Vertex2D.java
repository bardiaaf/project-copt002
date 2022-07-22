package algorithm.partitioning;

import model.Vertex;

public class Vertex2D extends Vertex {

    public int x;
    public int y;
    public int clusterNumber;

    public Vertex2D(int id) {
        super(id);
    }

    public void setXY(int x, int y){
        this.x=x;
        this.y=y;
    }

    public void setClusterNumber(int num){
        this.clusterNumber = num;
    }






}
