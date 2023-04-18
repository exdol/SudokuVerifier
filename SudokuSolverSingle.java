// Single threaded implementation (DFS) solution

class SudokuSolverSingle {
    public static int solutionCount = 0;
    public static void main(String[] args) {
        GridReader reader = new GridReader();
        int[][] grid = reader.getGridFromFile(args);

        long start = System.currentTimeMillis();
        solveGrid(grid);
        long end = System.currentTimeMillis();

        if (solutionCount == 0)
            System.out.println("No solution.");
        else if (solutionCount > 1)
            System.out.println(solutionCount + " solutions found");
        else {
            System.out.println("Solution found");
        }

        System.out.println("Processing time: " + (end - start) + " ms");
    }
    
    public static boolean solveGrid(int[][] grid)
    {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (grid[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(grid, row, col, num)) {
                            grid[row][col] = num;
                            if (solveGrid(grid)) {
                                return true;
                            } else {
                                grid[row][col] = 0;
                            }
                        }
                    }
                    return false;
                }
                else if (!isValid(grid, row, col, grid[row][col]))
                {
                    printGrid(grid);
                    System.out.println("Grid logic error at row " + (row + 1) + ", column " + (col + 1));
                    System.exit(0);
                }
            }
        }
        printGrid(grid);
        solutionCount += 1;
        return false;
    }
    
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