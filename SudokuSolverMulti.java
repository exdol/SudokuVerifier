
class SolverThread extends Thread {
    int section;
    int[][] grid;
    boolean[][][] eliminatedNumbers;
    boolean unsolved = true;
    int attempts = 0;
    public void run()
    {
        while(unsolved && attempts < 100)
        {
           
            unsolved = false; //returns to true if any of this thread's given grid is zero
            attempts++;
            System.out.println("Thread " + this.threadId() + ", attempt " + attempts); //if removing this, make sure to add sleep() or threads will work too fast
            
            //Eliminate possible values
            for(int i = 0; i < 9; i++)
            {
                // ROW
                if(grid[section][i] != 0)
                    for(int j = 0; j < 9; j++)
                    {
                        eliminatedNumbers[section][j][grid[section][i]] = true;
                        eliminatedNumbers[section][i][j + 1] = true;
                    }
                // COLUMN
                if(grid[i][section] != 0)
                    for(int j = 0; j < 9; j++)
                    {
                        eliminatedNumbers[j][section][grid[i][section]] = true;
                        eliminatedNumbers[i][section][j + 1] = true;
                    }
                // BOX
                int boxrow = (i % 3) + (section % 3) * 3;
                int boxcol = (i / 3) + (section / 3) * 3;
                if(grid[boxrow][boxcol] != 0)
                    for(int j = 0; j < 9; j++)
                    {
                        int jboxrow = (j % 3) + (section % 3) * 3;
                        int jboxcol = (j / 3) + (section / 3) * 3;
                        eliminatedNumbers[jboxrow][jboxcol][grid[boxrow][boxcol]] = true;
                        eliminatedNumbers[boxrow][boxcol][j + 1] = true;
                    }
            }
            
            int possibleValues;
            int currentValue;
            //Find grid spaces with only one possible value
            for(int i = 0; i < 9; i++)
            {
                // ROW
                if(grid[section][i] == 0)
                {
                    unsolved = true;
                    possibleValues = 0;
                    currentValue = 0;
                    for(int j = 1; j < 10; j++)
                    {
                        if(!eliminatedNumbers[section][i][j])
                        {
                            possibleValues++;
                            currentValue = j;
                        }
                    }
                    if(possibleValues == 1)
                        grid[section][i] = currentValue;
                }
                // COLUMN
                if(grid[i][section] == 0)
                {
                    unsolved = true;
                    possibleValues = 0;
                    currentValue = 0;
                    for(int j = 1; j < 10; j++)
                    {
                        if(!eliminatedNumbers[i][section][j])
                        {
                            possibleValues++;
                            currentValue = j;
                        }
                    }
                    if(possibleValues == 1)
                        grid[i][section] = currentValue;
                }
                // BOX
                int boxrow = (i % 3) + (section % 3) * 3;
                int boxcol = (i / 3) + (section / 3) * 3;
                if(grid[boxrow][boxcol] == 0)
                {
                    unsolved = true;
                    possibleValues = 0;
                    currentValue = 0;
                    for(int j = 1; j < 10; j++)
                    {
                        if(!eliminatedNumbers[boxrow][boxcol][j])
                        {
                            possibleValues++;
                            currentValue = j;
                        }
                    }
                    if(possibleValues == 1)
                        grid[boxrow][boxcol] = currentValue;
                }
            }

            //Find grid spaces where all other spaces are invalid
            for(int i = 0; i < 9; i++)
            {
                // ROW
                if(grid[section][i] == 0)
                {
                    for(int j = 1; j < 10; j++)
                    {
                        if(!eliminatedNumbers[section][i][j])
                        {
                            boolean alone = true; //will become false if other spaces share a possible value
                            for(int k = 0; k < 9; k++)
                            {
                                if(!eliminatedNumbers[section][k][j] && i != k)
                                    alone = false;
                            }
                            if(alone)
                                grid[section][i] = j;
                        }
                    }
                }
                // COLUMN
                if(grid[i][section] == 0)
                {
                    for(int j = 1; j < 10; j++)
                    {
                        if(!eliminatedNumbers[i][section][j])
                        {
                            boolean alone = true; //will become false if other spaces share a possible value
                            for(int k = 0; k < 9; k++)
                            {
                                if(!eliminatedNumbers[k][section][j] && i != k)
                                    alone = false;
                            }
                            if(alone)
                                grid[i][section] = j;
                        }
                    }
                }
                // BOX
                int boxrow = (i % 3) + (section % 3) * 3;
                int boxcol = (i / 3) + (section / 3) * 3;
                if(grid[boxrow][boxcol] == 0)
                {
                    for(int j = 1; j < 10; j++)
                    {
                        if(!eliminatedNumbers[boxrow][boxcol][j])
                        {
                            boolean alone = true; //will become false if other spaces share a possible value
                            for(int k = 0; k < 9; k++)
                            {
                                int kboxrow = (k % 3) + (section % 3) * 3;
                                int kboxcol = (k / 3) + (section / 3) * 3;
                                if(!eliminatedNumbers[kboxrow][kboxcol][j] && i != k)
                                    alone = false;
                            }
                            if(alone)
                                grid[boxrow][boxcol] = j;
                        }
                    }
                }
            }
        }
    }
}

class SudokuSolverMulti {
    public static int solutionCount = 0;
    //Text color stuff
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[44m";

    public static void main(String[] args) {
        //One Solution
        // int[][] grid = 
        // {{5, 3, 0, 0, 7, 0, 0, 0, 0}
        // ,{6, 0, 0, 1, 9, 5, 0, 0, 0}
        // ,{0, 9, 8, 0, 0, 0, 0, 6, 0}
        // ,{8, 0, 0, 0, 6, 0, 0, 0, 3}
        // ,{4, 0, 0, 8, 0, 3, 0, 0, 1}
        // ,{7, 0, 0, 0, 2, 0, 0, 0, 6}
        // ,{0, 6, 0, 0, 0, 0, 2, 8, 0}
        // ,{0, 0, 0, 4, 1, 9, 0, 0, 5}
        // ,{0, 0, 0, 0, 8, 0, 0, 7, 9}};

        //One Solution
        int[][] grid = 
        {{0, 0, 6, 3, 0, 7, 0, 0, 0}
        ,{0, 0, 4, 0, 0, 0, 0, 0, 5}
        ,{1, 0, 0, 0, 0, 6, 0, 8, 2}
        ,{2, 0, 5, 0, 3, 0, 1, 0, 6}
        ,{0, 0, 0, 2, 0, 0, 3, 0, 0}
        ,{9, 0, 0, 0, 7, 0, 0, 0, 4}
        ,{0, 5, 0, 0, 0, 0, 0, 0, 0}
        ,{0, 1, 0, 0, 0, 0, 0, 0, 0}
        ,{0, 0, 8, 1, 0, 9, 0, 4, 0}};

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
            System.out.println("No solution.");
        else if (solutionCount > 1)
            System.out.println(solutionCount + " solutions found");
        else {
            System.out.println("Solution found");
        }

        System.out.println("Processing time: " + (end - start) + " ms");
    }

    public static void solveGrid(int[][] grid)
    {
        SolverThread threads[] = new SolverThread[9];
        boolean[][][] eliminatedNumbers = new boolean[9][9][10];
        for(int i = 0; i < 9; i++)
        {
            threads[i] = new SolverThread();
            threads[i].section = i;
            threads[i].grid = grid;
            threads[i].eliminatedNumbers = eliminatedNumbers;
            threads[i].start();
        }

        //wait for each thread to finish
        for (int i = 0; i < 9; i++) { 
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        printGrid(grid);
        printEliminatedGrid(eliminatedNumbers, grid, 0);
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
    public static void printEliminatedGrid(boolean[][][] grid, int[][]solvedGrid, int boxNum) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 9; k++) {
                    for (int l = 0; l < 3; l++) {
                        if(!grid[i][k][j * 3 + l + 1])
                            System.out.print((j * 3 + l + 1) + " ");
                        else if(solvedGrid[i][k] != 0)
                        {
                            if(j * 3 + l + 1 == 5)
                                System.out.print(ANSI_BLUE + solvedGrid[i][k] + " " + ANSI_RESET);
                            else
                            System.out.print(ANSI_BLUE + "  " + ANSI_RESET);
                        }
                        else
                            System.out.print("  ");
                    }
                    System.out.print("| ");
                }
                System.out.println();
            }
            System.out.println("---------------------------------------------------------------------");
        }
        System.out.println();
        System.out.println();
    }
}