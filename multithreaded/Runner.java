package multithreaded;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Runner {
    public static int solutionCount = 0;
    
    public static void main(String[] args) { 
        File inputFile = null;
        Scanner in = null;

        System.out.println("Stargint multithreaded approach");

        if (args.length == 0) {
            System.out.println("No file specified");
            System.exit(1);
        }

        inputFile = new File(args[0]);
        try {
            in = new Scanner (inputFile);
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found");
            e.printStackTrace();
            System.exit(1);
        }
        
        // A standard sudoku puzzle is 9x9 (81 boxes total)
        int[][] grid = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int currentElement = in.nextInt();
                grid[i][j] = currentElement;
            }
        }

        long start = System.currentTimeMillis();
        // Algorithm
        long end = System.currentTimeMillis();

        if (solutionCount == 0) {
            System.out.print("No solution.");
        } else if (solutionCount > 1) {
            System.out.print(solutionCount + " solutions found");
        } else {
            System.out.print("Solution found");
        }

        System.out.println("Processing time: " + (end - start) + " ms");
    }
}
