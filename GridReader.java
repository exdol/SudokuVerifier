

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class GridReader {
    public int[][] getGridFromFile(String[] args) { 
        File inputFile = null;
        Scanner in = null;

        if (args.length == 0) {
            System.out.println("No file specified");
            System.exit(1);
        }

        inputFile = new File("./boards/" + args[0]);
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
        return grid;
    }
}
