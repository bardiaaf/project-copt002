package model;

public class Vertex implements Comparable<Vertex>{
    public final int id;

    public Vertex(int id) {
        this.id = id;
    }

    @Override
    public int compareTo(Vertex o) {
        return id-o.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return id == vertex.id;
    }
}
