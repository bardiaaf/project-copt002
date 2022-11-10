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

Parameters are picked by examining different values for every input to get best approximation and timing. 

**Results**

| Data | Partition Ratio | Time(s)| MostPromising Ratio | Time(s) | TourMerging Ratio | Time(s) | Best Approximation Factor |
| :-------------: | :-------------:| :-----: | :-------------: | :-----: | :-----------: | :-----: | :--: |
| xqf131 |  1.067  |  1  | 1.033  | 3 | 1.06 | 6 | 1.033 |
| xqg237 |  1.063  | 3 | 1.062  |  3  | 1.07 | 3 | 1.062 |
| pma343  |   1.048 | 2 | 1.023  | 5  | 1.065 | 9 | 1.023 |
| pka379 | 1.064  | 2 | 1.051  | 4  | 1.045 | 17 | 1.045 |
| bcl380 | 1.085  | 3 | 1.047  | 4  | 1.088 | 12 | 1.047 |
| nu3496 | 1.076  | 70 | 1.078  | 132  | 1.091 | 944 | 1.076 |
| tz6117 | 1.066 | 133 | 1.067  | 630  | 1.09 | 1400 | 1.066 |
| zi929 | 1.068 | 7 | 1.035 | 37 | 1.084 | 18 | 1.035 |
| rw1621 | 1.078 | 25 | 1.069 | 42 | 1.1 | 30 | 1.069 |
| pbm436 | 1.079 | 3 | 1.024 | 7 | 1.087 | 4 | 1.024 |
| ca4663 | 1.073 | 57 | 1.067 | 243 |1.091 | 166 | 1.067 |

We performed all our tests on a 1.4 GHz Quad-Core Intel Core i5 processor. We have limited our usage of virtual memory to 8 GBs.


**Analysis**

It can be concluded from the results above that MostPromising solver is doing better for instances that are relevantly small (i.e. less than 1000 vertices), however its running time increases dramatically for greater number of vertices. Thus, we can see that MostPromising solver is a great solver for instances with less than 1000 vertices, while Partitioning solver seems to be a better choice for larger graphs since it is noticeably faster on larger graphs.

