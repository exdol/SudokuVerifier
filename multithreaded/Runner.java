package multithreaded;

public class Runner {
    public static int solutionCount = 0;
    
    public static void main(String[] args) { 

        System.out.println("Stargint multithreaded approach");

        if (args.length == 0) {
            System.out.println("No file specified");
            System.exit(1);
        }

        // PArse txt file around here
        int[][] grid = {{}};

        long start = System.currentTimeMillis();
        // algorithm
        long end = System.currentTimeMillis();

        if (solutionCount == 0)
            System.out.print("No solution.");
        else if (solutionCount > 1)
            System.out.print(solutionCount + " solutions found");
        else {
            System.out.print("Solution found");
        }

        System.out.println("Processing time: " + (end - start) + " ms");
    }
}
