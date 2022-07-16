package algorithm;

import model.*;

import java.util.List;

public class LinKernighan {


    public static void linKernighan(Tour T, int k){
        // for each starting vertex v
        for (int i=0;i<Graph.graph.getSize();i++)
            linKernighanFirst(T, k , Graph.graph.getVertex(i));

    }

    public static void linKernighanFirst(Tour T, int k, Vertex first){
        linKernighanSecond(T, k, first, T.next(first));
        linKernighanSecond(T, k, first, T.previous(first));
    }

    public static void linKernighanSecond(Tour T, int k, Vertex first, Vertex second){
        K_Exchange kExchange = new K_Exchange(T,first, second);
        Vertex currentVertex = second;

        for (int i=1;i<k;i++){
            applyStep(kExchange,i, k , currentVertex);
        }

    }

    private static void applyStep(K_Exchange kExchange, int step, int k, Vertex currentVertex){
        List<Edge> list = Graph.graph.nearestNeighbors(currentVertex);

        for (Edge e:
             list) {

            K_Exchange newKExchange = kExchange.copy();
            Vertex next;
            if (e.v.equals(currentVertex))
                next = e.u;
            else
                next = e.v;

            Edge newEdge = new Edge(currentVertex, next, e.weight);
            if(!newKExchange.containsBlue(newEdge)){
                if(newKExchange.add(newEdge)){
                    if (step+1<k) {
                        // detecting next unique red edge handled in K_Exchange class
                        step++;
                        applyStep(newKExchange, step+1, k, newKExchange.t.get(newKExchange.t.size()-1));
                    }
                    else{
                        newKExchange.close();
                    }
                }
            }
        }


    }


}
