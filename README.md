In this project we used Lin-Kernighan algorithm and a variaty of its improvements to solve TSP. The goal was to find a good approximation of the optimal tour for instances in efficient ways.

The project contains multiple improvements of the main algorithm like:
- **Simple Lin-Kernighan:** Which is the main algorithm itself, which takes an initial tour constructed with simple methods like farthest insertion and nearest neighbors, to find the next best edge to improve tour in every step.
- **Partitioning and Tour Join:** In this method the problem is divided into clusters using K-Means clustering method and by finding tour for every cluster with simple Lin-Kernighan, then we will join these tours
- **Tour Merge:** In this method we use multiple methods to produce some tours for the problem, using these tours we will construct a new graph and solve the TSP problem with Lin-Kernighan on this new sparse graph.
- **Using  alpha-nearness to find neighbors:**  Another improvement that is implemented in this project is the method of finding neighbors using alpha-nearness introduced by Heslgaun to choose the considered edges for replacement in the K-exchanges.

**Instructions**

On running the program, you will be asked to enter the address and then the name of your .tsp instance of the problem. After that, you will be asked to choose between the following 3 solvers for the problem. In order to pick your choice, you must enter its first letter (the letter that is inside of the parantheses). 
- **(P) PartitioningSolver:** In this case we solve the initial tour is built by clustering the points using k-means, then running Lin-Kernighan on each one of them and then joining them. Here we have 3 modes: Quick, Normal, and Complete. The most important difference between these modes is in the running time and accuracy that you are looking for. (Quick mode is not using partitioning in order to answer faster.) 
- **(M) MostPromisingSolver:** This solver uses 3 ways to construct an initial tour “farthest insertion” , “nearest neighbors”, and “clustering”  for a limited number of steps and then uses the best of these tours to apply Lin-Kernighan for improvement.
- **(T) TourMergingSolver:** This class uses the Tour Merge method explained above and then use Lin-Kernighan.

Parameters are picked by examining different values for every input to get best approximation and timing. We have concluded from our own experiments that PartitioningSolver is doing better than the other solvers.

**RESULTS**

| Data | Partition Ratio | Time| MostPromising Ratio | Time| TourMerging Ratio | Time|
| :-------------: | :-------------:| :-----: | :-------------: | :-----: | :-----------: | :-----: |
| xqf131 |  1.067  |  1  | 1.033  | 3 | 1.06 | 6 |
| xqg237 |  1.063  | 3 | 1.062  |  3  | 1.07 | 3 |
| pma343  |   1.048 | 2 | 1.023  | 5  | 1.065 | 9 |
| pka379 | 1.064  | 2 | 1.051  | 4  | 1.045 | 17 |
| bcl380 | 1.085  | 3 | 1.047  | 4  | 1.088 | 12 |
| nu3496 | 1.076  | 70 | Content Cell  | Content Cell  |
| pka379 | 1.064  | 2 | Content Cell  | Content Cell  |
| ca4663 |  1.075  | 61 | 1.067  | 248  | 1.091 | 166 |


