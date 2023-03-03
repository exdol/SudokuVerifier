class SudokuSolver {
    public static int solutionCount = 0;
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
        long start = System.currentTimeMillis();
        solveGrid(grid);
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