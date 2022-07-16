package algorithm;

import model.*;

import java.util.List;

public class LinKernighan {

    // har chi gain bishtar bashe behtare
    public static K_Exchange bestKExchange;

    // "l" is number of neighbors
    public static void linKernighan(Tour T, int k, int l){
        // for each starting vertex v
        for (int i=0;i<Graph.graph.getSize();i++)
            linKernighanFirst(T, k , l, Graph.graph.getVertex(i));

    }

    public static void linKernighanFirst(Tour T, int k, int l, Vertex first){
        linKernighanSecond(T, k, l, first, T.next(first));
        linKernighanSecond(T, k, l, first, T.previous(first));
    }

    public static void linKernighanSecond(Tour T, int k, int l, Vertex first, Vertex second){
        K_Exchange kExchange = new K_Exchange(Graph.graph, T,first, second);
        Vertex currentVertex = second;

        applyStep(kExchange, 0, k , l, currentVertex);
    }

    private static void applyStep(K_Exchange kExchange, int step, int k, int l, Vertex currentVertex){
        List<Edge> list = Graph.graph.nearestNeighbors(currentVertex, l);

        for (Edge e:
             list) {

            if(!kExchange.containsBlue(e)){
                K_Exchange newKExchange = kExchange.add(e);

                if(newKExchange!=null){
                    if (step+1<k) {
                        step++; // for each read and blue together step++
                        applyStep(newKExchange, step, k, l, newKExchange.t.get(newKExchange.t.size()-1));
                    }
                    else{ // reached step k

                        if(bestKExchange==null)
                            bestKExchange = newKExchange;
                        else if(newKExchange.getGain() > bestKExchange.getGain())
                            bestKExchange = newKExchange;

                        newKExchange.close();
                    }
                }
            }
        }

        if(bestKExchange==null)
            bestKExchange = kExchange;
        else if(kExchange.getGain() > bestKExchange.getGain())
            bestKExchange = kExchange;

        kExchange.close();

    }


}
