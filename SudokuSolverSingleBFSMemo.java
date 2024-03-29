import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

// Single BFS solution

class SudokuSolverSingleBFSMemo extends Thread {
    public static int solutionCount = 0;

    // Stores the puzzle and the row and column coordinates of the current position
    public static HashMap<String, ArrayList<Integer>> puzzleStates = new HashMap<String, ArrayList<Integer>>();

    // Stores all the solutions hashed
    public static HashSet<String> solutionHashes = new HashSet<String>();

    public static void main(String[] args) {
        GridReader reader = new GridReader();
        int[][] grid = reader.getGridFromFile(args);

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

        for (String setElement : solutionHashes) {
            System.out.println(setElement);
            printGrid(unHash(setElement));
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
                newGrid[i][j] = Character.getNumericValue(hash.charAt(index));
                index++;
            }
        }

        return newGrid;
    }

    // Method that gets the next 9 possible values in current position 
    //      and increments the current position (moving us rightward then downward)
    public static ArrayList<String> getNext(String currentPuzzleHash) {
        int[][] grid = unHash(currentPuzzleHash);
        ArrayList<Integer> currentPuzzlePositionOriginal = puzzleStates.get(currentPuzzleHash);
        ArrayList<Integer> currentPuzzlePosition = new ArrayList<Integer>();
        currentPuzzlePosition.add(currentPuzzlePositionOriginal.get(0));
        currentPuzzlePosition.add(currentPuzzlePositionOriginal.get(1));
        ArrayList<String> ret = new ArrayList<String>();
        
        // Meaning that we can try all valid numbers on this position and increment its coordinates
        if (grid[currentPuzzlePosition.get(0)][currentPuzzlePosition.get(1)] == 0) {
            int row = currentPuzzlePosition.get(0);
            int col = currentPuzzlePosition.get(1);

            // Increment next position so we can easily memo the next states'
            if (currentPuzzlePosition.get(1) >= 8) {
                // If at the end of the column, start a new row
                currentPuzzlePosition.set(0, currentPuzzlePosition.get(0)+1);
                currentPuzzlePosition.set(1, 0);

            } else {
                // Increment the column only since its not at the end yet
                currentPuzzlePosition.set(1, currentPuzzlePosition.get(1)+1);
            }

            // Iterates through all 9 possible values at this current position
            for (int i = 1; i <= 9; i++) {
                if (isValid(grid, row, col, i)) {
                    // If valid, add each state's hash to the arraylist that is returned and store the position
                    grid[row][col] = i;
                    String tempHash = createHash(grid);
                    puzzleStates.put(tempHash, currentPuzzlePosition);
                    ret.add(tempHash);
                }
            }

        } else {
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
        }

        return ret;
    }

    // Solves with an iterative/DP approach for brute force
    public static void solve(String startGridHash) {
        LinkedList<String> queue = new LinkedList<>();
        queue.offer(startGridHash);
         while (queue.size() != 0) {
            String currentPuzzleHash = queue.poll();
            ArrayList<String> adjacentPuzzles = getNext(currentPuzzleHash);

            for (int i = 0; i < adjacentPuzzles.size(); i++) {
                String currentAdjacentPuzzleHash = adjacentPuzzles.get(i);

                // Check if the puzzle hasn't been completed yet
                if (puzzleStates.get(currentAdjacentPuzzleHash).get(0) <= 8) {
                    // Add it to the queue for further breath first searches if not completed
                    queue.offer(currentAdjacentPuzzleHash);

                } else {
                    // If it has been completed, check if there are no 0s
                    if (isComplete(currentAdjacentPuzzleHash)) {
                        solutionHashes.add(currentPuzzleHash);
                        solutionCount++;
                    }
                }
            }
        }
    }

    // Verifies that a puzzle has been completed (no empty or 0 spots)
    public static boolean isComplete(String hash) {
        for (int i = 0; i < hash.length(); i++) {
            if (hash.charAt(i) == '0') {
                return false;
            }
        }

        return true;
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