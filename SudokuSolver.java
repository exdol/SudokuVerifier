import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

class SudokuSolver {
    public static int solutionCount = 0;

    // Stores the puzzle and the row and column of the current position
    public static HashMap<String, ArrayList<Integer>> puzzleStates;

    // Store the non-visited puzzle states for temporary use
    public static HashMap<String, ArrayList<Integer>> nonVisitedPuzzleStates;

    // Stores all the solutions hashed
    public static HashSet<String> solutionHashes;

    public static void main(String[] args) {
        //One Solution
        int[][] grid = 
        {{5, 3, 0, 0, 7, 0, 0, 0, 0}
        ,{6, 0, 0, 1, 9, 5, 0, 0, 0}
        ,{0, 9, 8, 0, 0, 0, 0, 6, 0}
        ,{8, 0, 0, 0, 6, 0, 0, 0, 3}
        ,{4, 0, 0, 8, 0, 3, 0, 0, 1}
        ,{7, 0, 0, 0, 2, 0, 0, 0, 6}
        ,{0, 6, 0, 0, 0, 0, 2, 8, 0}
        ,{0, 0, 0, 4, 1, 9, 0, 0, 5}
        ,{0, 0, 0, 0, 8, 0, 0, 7, 9}};

        //Two Solutions
        // int[][] grid = 
        // {{2, 9, 5, 7, 4, 3, 8, 6, 1}
        // ,{4, 3, 1, 8, 6, 5, 9, 0, 0}
        // ,{8, 7, 6, 1, 9, 2, 5, 4, 3}
        // ,{3, 8, 7, 4, 5, 9, 2, 1, 6}
        // ,{6, 1, 2, 3, 8, 7, 4, 9, 5}
        // ,{5, 4, 9, 2, 1, 6, 7, 3, 8}
        // ,{7, 6, 3, 5, 2, 4, 1, 8, 9}
        // ,{9, 2, 8, 6, 7, 1, 3, 5, 4}
        // ,{1, 5, 4, 9, 3, 8, 6, 0, 0}};

        //Invalid grid
        // int[][] grid = 
        // {{5, 3, 0, 0, 7, 0, 0, 0, 0}
        // ,{6, 0, 0, 1, 9, 5, 0, 0, 0}
        // ,{0, 9, 8, 0, 0, 0, 0, 6, 0}
        // ,{8, 0, 0, 4, 6, 0, 0, 0, 3}
        // ,{4, 0, 0, 8, 0, 3, 0, 0, 1}
        // ,{7, 0, 0, 0, 2, 0, 0, 0, 6}
        // ,{0, 6, 0, 0, 0, 0, 2, 8, 0}
        // ,{0, 0, 0, 4, 1, 9, 0, 0, 5}
        // ,{0, 0, 0, 0, 8, 0, 0, 7, 9}};

        //"Unsolvable" grid
        // int[][] grid = 
        // {{0, 9, 0, 0, 0, 3, 7, 0, 0}
        // ,{0, 0, 0, 0, 5, 0, 0, 0, 4}
        // ,{0, 0, 1, 2, 0, 0, 0, 6, 0}
        // ,{0, 4, 5, 0, 6, 0, 0, 0, 0}
        // ,{0, 3, 0, 0, 0, 4, 0, 0, 0}
        // ,{2, 0, 0, 7, 0, 0, 0, 0, 0}
        // ,{0, 0, 0, 0, 0, 9, 3, 0, 0}
        // ,{0, 0, 6, 0, 0, 0, 0, 1, 0}
        // ,{7, 0, 0, 0, 8, 0, 0, 0, 2}};

        // Initializations
        solutionHashes = new HashSet<String>();
        puzzleStates = new HashMap<String, ArrayList<Integer>>();
        nonVisitedPuzzleStates = new HashMap<String, ArrayList<Integer>>();

        // Create hash and corresponding position for the current grid
        String startGrid = createHash(grid);
        ArrayList<Integer> startPosition = new ArrayList<>();
        startPosition.add(0);
        startPosition.add(0);

        puzzleStates.put(startGrid, startPosition);

        long start = System.currentTimeMillis();
        // solveGrid(grid);
        solve(startGrid);
        long end = System.currentTimeMillis();

        if (solutionHashes.size() == 0)
            System.out.println("No solution.");
        else if (solutionHashes.size() > 1)
            System.out.println(solutionHashes.size() + " solutions found");
        else {
            System.out.println("Solution found");
        }

        System.out.println("Processing time: " + (end - start) + " ms");
    }

    // Returns a string representing the puzzle/grid's state (for memoization)
    public static String createHash(int[][] grid) {
        String hash = "";

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                hash += grid[i][j];
            }
        }

        return hash;
    }

    // Returns a functional grid from a memoized puzzle/grid state
    public static int[][] unHash(String hash) {
        int[][] newGrid = new int[9][9];

        int index = 0;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                newGrid[i][j] = hash.charAt(index);
                index++;
            }
        }

        return newGrid;
    }

    // Gets the next logical step in any given puzzle in the form of a hash
    public static ArrayList<String> getNext(String currentPuzzleHash) {
        int[][] grid = unHash(currentPuzzleHash);
        ArrayList<Integer> currentPuzzlePosition = puzzleStates.get(currentPuzzleHash);
        ArrayList<String> ret = new ArrayList<String>();
        
        if (currentPuzzlePosition.get(0) >= 8 && currentPuzzlePosition.get(1) >= 8) {
            // First, check if the grid is finished, add nothing to the arraylist that is returned
            // Instead, add the hashed grid to the solutionHashes hashset
            solutionHashes.add(currentPuzzleHash);
            return ret;
            
        } else if (grid[currentPuzzlePosition.get(0)][currentPuzzlePosition.get(1)] != 0) {
            // If the current position is already filled, increment the position to the next col (or row if applicable)
            if (currentPuzzlePosition.get(1) >= 8) {
                // If at the end of the column, start a new row
                currentPuzzlePosition.set(0, currentPuzzlePosition.get(0)+1);
                currentPuzzlePosition.set(1, 0);
            } else {
                // Increment the column only since its not at the end yet
                currentPuzzlePosition.set(1, currentPuzzlePosition.get(1)+1);
            }
            // Then add that currentPuzzleHash to the arraylist that is returned
            ret.add(currentPuzzleHash);

            // Set the new position
            puzzleStates.put(currentPuzzleHash, currentPuzzlePosition);

        } else {
            int row = currentPuzzlePosition.get(0);
            int col = currentPuzzlePosition.get(1);
            // Iterates through all 9 possible values at this current position
            for (int i = 1; i <= 9; i++) {
                if (isValid(grid, row, col, i)) {
                    // If valid, add each state's hash to the arraylist that is returned and store the position
                    grid[row][col] = i;
                    String tempHash = createHash(grid);
                    ret.add(tempHash);
                    nonVisitedPuzzleStates.put(tempHash, currentPuzzlePosition);
                }
            }
        }

        return ret;
    }

    // Solves with an iterative/DP approach
    public static void solve(String startGridHash) {
        LinkedList<String> queue = new LinkedList<>();
        queue.offer(startGridHash);

        while (queue.size() != 0) {
            String currentPuzzleHash = queue.poll();

            // MAKE A getNext(String hash) method that gets the next 9 possible values in current position
            // Then it will increment the current position + 1 (moving us rightward then downward)
            ArrayList<String> adjacentPuzzles = getNext(currentPuzzleHash);

            for (int i = 0; i < adjacentPuzzles.size(); i++) {
                String currentAdjacentPuzzleHash = adjacentPuzzles.get(i);
                // Add it to the queue for further breath first searches
                queue.offer(currentAdjacentPuzzleHash);
            }
        }
    }
    
    // Verifies that a given number is valid at the current position in a puzzle at its current state
    public static boolean isValid(int[][] grid, int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if ((grid[row][i] == num && i != col) || (grid[i][col] == num && i != row)) {
                return false;
            }
        }
        int boxRow = row - row % 3;
        int boxCol = col - col % 3;
        for (int i = boxRow; i < boxRow + 3; i++) {
            for (int j = boxCol; j < boxCol + 3; j++) {
                if (grid[i][j] == num && !(i == row && j == col)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static void printGrid(int[][] grid) {
        int column = 0;
        int row = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(grid[i][j] + " ");
                column += 1;
                if(column % 3 == 0 && column % 9 != 0)
                    System.out.print(" | ");
            }
            row += 1;
            if(row % 3 == 0 && row % 9 != 0)
                System.out.print("\n-----------------------");
            System.out.println();

        }
        System.out.println();
        System.out.println();
    }
}