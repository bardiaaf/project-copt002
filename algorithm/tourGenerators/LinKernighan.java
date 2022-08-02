package algorithm.tourGenerators;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class LinKernighan extends TourGenerator {
    private final TourGenerator secondaryTourGenerator;
    private static final int defaultRounds = 100, defaultK = 5, defaultL = 5;

    public LinKernighan(Graph graph, TourGenerator secondaryTourGenerator) {
        super(graph);
        this.secondaryTourGenerator = secondaryTourGenerator;
    }

    @Override
    public Tour generateTour(Graph graph) {
        return generateTour(graph, defaultRounds, defaultK, defaultL);
    }

    public Tour generateTour(Graph graph, int rounds, int k, int l) {
        return this.generateTour(graph, secondaryTourGenerator.generateTour(graph), rounds, k, l);
    }

    public Tour generateTour(Graph graph, Tour pre, int rounds, int k, int l) {
        Tour res = pre;
        for (int i = 0; i < rounds; i++) {
            K_Exchange best = linKernighanIteration(res, k, l);
            if(best == null)
                break;
            res = res.applyKExchange(best);
        }
        return res;
    }

    public K_Exchange parallelTourGeneration(int i0, int step, Tour T, int k, int l) {
        double bestGain = 0.0;
        K_Exchange best = null;

        // for each starting vertex v
        for (int i = i0; i < graph.getVertices().length; i += step) {
            Vertex vertex = graph.getVertices()[i];
            K_Exchange stepResult = linKernighanFirst(T, k, l, vertex);
            if (stepResult.getGain() > bestGain) {
                best = stepResult;
                bestGain = best.getGain();
            }
        }
        return best;
    }

    public K_Exchange linKernighanIteration(Tour T, int k, int l) {
        int cores = 4;
        double bestGain = 0.0;
        K_Exchange best = null;
        K_Exchange[] res = new K_Exchange[cores];
        List<Thread> threads = new ArrayList<>();
        for(int i=0;i<cores;i++){
            final int i0 = i;
            threads.add(new Thread(() -> res[i0] = parallelTourGeneration(i0, cores, T, k, l)));
            threads.get(i).start();
        }
        for(int i=0;i<cores;i++) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for(int i=0;i<cores;i++) {
            if(res[i]==null)
                continue;
            if (res[i].getGain() > bestGain) {
                best = res[i];
                bestGain = best.getGain();
            }
        }
        return best;
    }

    // "l" is number of neighbors
//    public K_Exchange linKernighanIteration(Tour T, int k, int l) {
//        double bestGain = 0.0;
//        K_Exchange best = null;
//
//        // for each starting vertex v
//        for (Vertex vertex : graph.getVertices()) {
//            K_Exchange stepResult = linKernighanFirst(T, k, l, vertex);
//            if (stepResult.getGain() > bestGain) {
//                best = stepResult;
//                bestGain = best.getGain();
//            }
//        }
//        return best;
//    }

    public K_Exchange linKernighanFirst(Tour T, int k, int l, Vertex first) {
        K_Exchange k1 = linKernighanSecond(T, k, l, first, T.next(first));
        K_Exchange k2 = linKernighanSecond(T, k, l, first, T.previous(first));

        if (k1.getGain() > k2.getGain())
            return k1;

        return k2;
    }

    public K_Exchange linKernighanSecond(Tour T, int k, int l, Vertex first, Vertex second) {
        K_Exchange kExchange = new K_Exchange(graph, T, first, second);

        return applyStep(kExchange, 1, k, l, second);
    }

    private K_Exchange applyStep(K_Exchange kExchange, int step, int k, int l, Vertex currentVertex) {
        List<Edge> list = graph.nearestNeighbors(currentVertex, l);

        K_Exchange best = kExchange;

        for (Edge e :
                list) {

            if (!kExchange.containsBlue(e)) {
                K_Exchange newKExchange = kExchange.add(e);

                if (newKExchange != null) {
                    // che vaghti ke apply step mikoni va che vaghti ke miri toye else max bayad begirim ba ye best ii ke aval darim
                    if (step + 1 < k) {
                        K_Exchange stepExchange = applyStep(newKExchange, step+1, k, l,
                                newKExchange.t.get(newKExchange.t.size() - 1));
                        if (stepExchange.getGain() > best.getGain())
                            best = stepExchange;

                    } else { // reached step k
                        if (newKExchange.getGain() > best.getGain()) {
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
