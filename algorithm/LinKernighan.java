package algorithm;

import model.*;

import java.awt.event.KeyEvent;
import java.util.List;

public class LinKernighan {

    // har chi gain bishtar bashe behtare
    public static K_Exchange bestKExchange;

    // "l" is number of neighbors
    public static void linKernighanIteration(Tour T, int k, int l){
        int bestGain=0;
        K_Exchange best = null;
        // for each starting vertex v
        for (int i=0;i<Graph.graph.getSize();i++) {
            K_Exchange stepResult =linKernighanFirst(T, k, l, Graph.graph.getVertex(i));
            if (stepResult.getGain()>bestGain){
                best = stepResult;
                bestGain = best.getGain();
            }
        }

    }

    public static K_Exchange  linKernighanFirst(Tour T, int k, int l, Vertex first){
        K_Exchange k1 = linKernighanSecond(T, k, l, first, T.next(first));
        K_Exchange k2 = linKernighanSecond(T, k, l, first, T.previous(first));

        if (k1.getGain() > k2.getGain())
            return k1;

        return k2;
    }

    public static K_Exchange  linKernighanSecond(Tour T, int k, int l, Vertex first, Vertex second){
        K_Exchange kExchange = new K_Exchange(Graph.graph, T,first, second);

        return applyStep(kExchange, 0, k , l, second);
    }

    private static K_Exchange applyStep(K_Exchange kExchange, int step, int k, int l, Vertex currentVertex){
        List<Edge> list = Graph.graph.nearestNeighbors(currentVertex, l);

        K_Exchange best = kExchange;

        for (Edge e:
             list) {

            if(!kExchange.containsBlue(e)){
                K_Exchange newKExchange = kExchange.add(e);

                if(newKExchange!=null){
                    // che vaghti ke apply step mikoni va che vaghti ke miri toye else max bayad begirim ba ye best ii ke aval darim
                    if (step+1<k) {
                        step++; // for each read and blue together step++
                        best = applyStep(newKExchange, step, k, l, newKExchange.t.get(newKExchange.t.size()-1));
                    }
                    else{ // reached step k

                        if(newKExchange.getGain() > best.getGain()){
                            best = newKExchange;
                        }
                    }
                }
            }
        }


        best.close();
        return best;

    }


}
