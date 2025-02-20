import java.util.Scanner;
import java.util.concurrent.*;

public class SupplyChainOptimization {
    static final int INF = Integer.MAX_VALUE;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of locations in the supply chain network: ");
        int n = scanner.nextInt();

        // Validate input
        if (n <= 0) 
        {
            System.out.println("Error: Number of locations must be greater than 0.");
            return;
        }

        int[][] supplyChain = new int[n][n];

        // Input the supply chain network
        System.out.println("Enter the transportation costs between locations:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf("Cost to transport from location %d to location %d (Enter 'INF' for no direct connection): ", i + 1, j + 1);
                String input = scanner.next();
                if (input.equalsIgnoreCase("INF")) {
                    supplyChain[i][j] = INF;
                } else {
                    supplyChain[i][j] = Integer.parseInt(input);
                }
            }
        }

        // Find the shortest paths between all pairs of locations
        int[][] shortestDistances = floydWarshall(supplyChain);

        // Print the shortest distances between all pairs of locations
        System.out.println("Shortest distances between all pairs of locations:");
        printGraph(shortestDistances);
        
        scanner.close();
    }

    // Optimized Floyd-Warshall algorithm for finding shortest paths between all pairs of locations
    public static int[][] floydWarshall(int[][] graph) {
        int n = graph.length;
        int[][] distances = new int[n][n];

        // Initialize distances array with the given graph
        for (int i = 0; i < n; i++) {
            System.arraycopy(graph[i], 0, distances[i], 0, n);
        }

        // Applying Floyd-Warshall algorithm with parallelization
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int k = 0; k < n; k++) {
            final int K = k;
            for (int i = 0; i < n; i++) {
                final int I = i;
                executor.submit(() -> {
                    for (int j = 0; j < n; j++) {
                        if (distances[I][K] != INF && distances[K][j] != INF &&
                            distances[I][K] + distances[K][j] < distances[I][j]) {
                            distances[I][j] = distances[I][K] + distances[K][j];
                        }
                    }
                });
            }
        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return distances;
    }

    // Utility method to print the graph represented by a 2D matrix
    public static void printGraph(int[][] graph) {
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[i].length; j++) {
                if (graph[i][j] == INF) {
                    System.out.print("INF ");
                } else {
                    System.out.printf("%-3d ", graph[i][j]);
                }
            }
            System.out.println();
        }
    }
}
