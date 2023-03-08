
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

            boolean first = true;
            boolean second = true;
            boolean third = true;
            int boxrow = 0;
            int boxcol = 0;
            //Find values in boxes that can only exist in specific rows or columns
            for(int i = 1; i < 10; i++)
            {
                //become false if value can exist in that box
                first = true;
                second = true;
                third = true;
                boxrow = 0;
                boxcol = 0;
                
                // ROW
                for(int j = 0; j < 9; j++)
                {
                    if(!eliminatedNumbers[section][j][i])
                    {
                        if(j < 3)
                            first = false;
                        else if(j < 6)
                            second = false;
                        else
                            third = false;
                    }
                }
                if(first && second && !third) //must exist in third box
                    boxcol = 6;
                else if(first && !second && third)//must exist in second box
                    boxcol = 3; 
                else if (!first && second && third )//must exist in first box
                    boxcol = 0;
                else //does not necessarily exist in a specific box
                    continue;
                boxrow = (section / 3) * 3;
                for(int j = 0; j < 3; j++)
                    for(int k = 0; k < 3; k++)
                        if(j + boxrow != section)
                            eliminatedNumbers[j + boxrow][k + boxcol][i] = true;
            }
            for(int i = 1; i < 10; i++)
            {
                //become false if value can exist in that box
                first = true;
                second = true;
                third = true;
                boxrow = 0;
                boxcol = 0;

                // COLUMN
                for(int j = 0; j < 9; j++)
                {
                    if(!eliminatedNumbers[j][section][i])
                    {
                        if(j < 3)
                            first = false;
                        else if(j < 6)
                            second = false;
                        else
                            third = false;
                    }
                }
                if(first && second && !third) //must exist in third box
                    boxrow = 6;
                else if(first && !second && third)//must exist in second box
                    boxrow = 3; 
                else if (!first && second && third )//must exist in first box
                    boxrow = 0;
                else //does not necessarily exist in a specific box
                    continue;
                boxcol = (section / 3) * 3;
                for(int j = 0; j < 3; j++)
                    for(int k = 0; k < 3; k++)
                        if(k + boxcol != section)
                            eliminatedNumbers[j + boxrow][k + boxcol][i] = true;
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
        GridReader reader = new GridReader();
        int[][] grid = reader.getGridFromFile(args);
        
        long start = System.currentTimeMillis();
        solveGrid(grid);
        long end = System.currentTimeMillis();

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