package model;

public class Edge implements Comparable<Edge> {
    public final Vertex v, u;
    public final int weight;

    public Edge(Vertex v, Vertex u, int weight) {
        this.v = v;
        this.u = u;
        this.weight = weight;
    }

    //For simulating non-edges
    public Edge(Vertex v, Vertex u) {
        this.v = v;
        this.u = u;
        this.weight = Integer.MAX_VALUE;
    }

    public Edge swap(){
        return new Edge(u,v,weight);
    }

    @Override
    public int compareTo(Edge e) {
        if(this.weight == e.weight) {
            if (Math.min(this.u.id, this.v.id) == Math.min(e.u.id, e.v.id))
                return Math.max(this.u.id, this.v.id) - Math.max(e.u.id, e.v.id);
            return Math.min(this.u.id, this.v.id) - Math.min(e.u.id, e.v.id);
        }
        return this.weight - e.weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return weight == edge.weight &&
                v.equals(edge.v) &&
                u.equals(edge.u);
    }
}
