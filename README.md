In this project we used Lin-Kernighan algorithm and multiple methods of improvements introduced by LKH to solve TSP.The goal was to find a good approximation of the optimal tour for instances in efficient ways.

The project contains multiple improvements of the main algorithm like:
- Simple Lin-Kernighan: which is the main algorithm itself which takes an initial tour constructed with simple methods like Farthest insertion and Nearest Neighbors, and using neighbors to find the next best edge to improve tour in every step.
- Partitioning and Tour Join: In this method the problem is divided into clusters using K-Means clustering method and by finding tour for every cluster with simple lin-kernighan, then we will join these tours
- Tour Merge: In this method we use multiple methods to produce some tours for the problem, using these tours we’ll construct a new graph and solve the TSP problem with lin-kernighan on this new graph.
- Using  1-tree to find neighbors :  another improvement that is designed in this project is the method of finding neighbors using 1-tree definition to improve tour in every step and replace edges in lin-kernighan steps.

How to implement this project:
The Solver class and its children are designed to implement written algorithms.

- PartitioningSolver : In this class we have 3 modes for solving TSP Quick, Normal, and Complete.Which are different in input parameters of lin-kernighan and clustering.
- MostPromisingSolver: This solver uses 3 ways to construct an initial tour “farthest insertion” , “nearest neighbors”, and “clustering”  until a limited step and then use the best of these tour to apply lin-kernighan for improvement.
- TourMergingSolver: This class uses the Tour Merge method explained above and then use lin-kernighan.

Parameters are picked by examining different values for every input to get best approximation and timing.
