import java.util.ArrayList;
import java.util.Random;

/**
 * @author Raj Kavathekar
 * @version 8.0
 * Yes it took that many iterations.
 * Level class that stores grid values for everything in the level as well as generates and resets itself.
 */
public class Level {
    private int[][] grid;
    private int playerRow;
    private int playerCol;
    private ArrayList<int[]> goalLocations;
    private int[][] initialGrid;
    private int initialPlayerRow;
    private int initialPlayerCol;
    private final int size = 10;

    /**
     * Constructor for level
     */
    public Level()
    {
        goalLocations = new ArrayList<>();

        generateSimpleLevel();
        saveInitialState();


    }

    /**
     * Moves the player to a tile based on certain conditions
     * @param dx how many column's it is moving
     * @param dy how many row's it is moving
     * @return Returns whether player can move or not.
     */
    public boolean movePlayer(int dx, int dy)
    {
        int newX = playerCol + dx;
        int newY = playerRow + dy;

        if ((grid[newY][newX] & TileType.WALL) != 0)
        {
            return false;
        }

        if ((grid[newY][newX] & TileType.BOX) != 0)
        {
            int boxNewX = newX + dx;
            int boxNewY = newY + dy;

            // If box can't move ahead
            if((grid[boxNewY][boxNewX] & (TileType.WALL | TileType.BOX)) != 0)
            {
                return false;
            }
            removeElement(newY, newX,TileType.BOX);
            addElement(boxNewY,boxNewX,TileType.BOX);
        }

        removeElement(playerRow, playerCol, TileType.PLAYER);
        addElement(newY,newX, TileType.PLAYER);

        playerRow = newY;
        playerCol = newX;
        if (isComplete())
        {
            generateSimpleLevel();
        }
        return true;
    }

    /**
     * Saves initial values incase of resetting level.
     */
    private void saveInitialState()
    {
        initialGrid = new int[grid.length][grid[0].length];
        for(int i = 0; i < grid.length; i++) {
            initialGrid[i] = grid[i].clone();
        }

        for(int row = 0; row < grid.length; row++)
        {
            for (int col = 0; col < grid[0].length; col++)
            {
                if((grid[row][col] & TileType.GOAL) != 0)
                {
                    goalLocations.add(new int[]{row,col});
                }

                if((grid[row][col] & TileType.PLAYER) != 0)
                {
                    playerRow = initialPlayerRow = row;
                    playerCol = initialPlayerCol = col;
                }

            }
        }
    }

    /**
     * Generates a 10 by 10 level with 3 boxes.
     */
    public void generateSimpleLevel() {
        int attempts = 0;
        final int MAX_ATTEMPTS = 50;

        do {
            attempts++;
            generateRandomLayout();

            // Only keep the level if the solver confirms solvability.
            if (SokobanSolver.isLevelSolvable(this)) {
                saveInitialState();
                System.out.println("Generated solvable level in " + attempts + " attempt(s)");
                return;
            }
            // Otherwise try again.
        } while (attempts < MAX_ATTEMPTS);

        // Fallback – if we failed to make a solvable random level, create a trivial level.
        System.err.println("Failed to generate a solvable random level after " + MAX_ATTEMPTS + " attempts. Generating trivial level.");
        generateTrivialLevel();
        saveInitialState();
    }

    /**
     * Encapsulates the previous random-layout logic.
     */
    private void generateRandomLayout() {
        grid = new int[size][size];
        goalLocations.clear();

        // Fill floor tiles.
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                grid[y][x] = TileType.FLOOR;
            }
        }

        // Border walls.
        for (int i = 0; i < size; i++) {
            grid[0][i] = TileType.WALL;
            grid[size - 1][i] = TileType.WALL;
            grid[i][0] = TileType.WALL;
            grid[i][size - 1] = TileType.WALL;
        }

        // Additional random wall patterns.
        Random random = new Random();
        int numWalls = random.nextInt(15) + 10; // 10-25 walls

        for (int i = 0; i < numWalls; i++) {
            int x = 1 + random.nextInt(size - 2);
            int y = 1 + random.nextInt(size - 2);

            int pattern = random.nextInt(4);
            switch (pattern) {
                case 0: // Single wall
                    grid[y][x] = TileType.WALL;
                    break;
                case 1: // Horizontal segment
                    if (x < size - 3) {
                        grid[y][x] = TileType.WALL;
                        grid[y][x + 1] = TileType.WALL;
                    }
                    break;
                case 2: // Vertical segment
                    if (y < size - 3) {
                        grid[y][x] = TileType.WALL;
                        grid[y + 1][x] = TileType.WALL;
                    }
                    break;
                case 3: // L-shape
                    if (x < size - 2 && y < size - 2) {
                        grid[y][x] = TileType.WALL;
                        grid[y + 1][x] = TileType.WALL;
                        grid[y][x + 1] = TileType.WALL;
                    }
                    break;
            }
        }

        // Place goals, boxes, and player ensuring space.
        for (int i = 0; i < 3; i++) {
            placeWithSpace(TileType.GOAL);
            placeWithSpace(TileType.BOX);
        }
        placeWithSpace(TileType.PLAYER);
    }

    /**
     * Very simple fallback level with no internal walls, guaranteeing solvability.
     */
    private void generateTrivialLevel() {
        grid = new int[size][size];
        goalLocations.clear();

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                grid[y][x] = TileType.FLOOR;
            }
        }

        for (int i = 0; i < size; i++) {
            grid[0][i] = TileType.WALL;
            grid[size - 1][i] = TileType.WALL;
            grid[i][0] = TileType.WALL;
            grid[i][size - 1] = TileType.WALL;
        }

        for (int i = 0; i < 3; i++) {
            int goalX = 2 + i;
            int goalY = 2;
            grid[goalY][goalX] |= TileType.GOAL;

            int boxX = 2 + i;
            int boxY = 4;
            grid[boxY][boxX] |= TileType.BOX;
        }

        playerCol = 2;
        playerRow = 6;
        grid[playerRow][playerCol] |= TileType.PLAYER;
    }

    /**
     * Places objects, making sure there is space around it.
     * @param elementType
     */
    private void placeWithSpace(int elementType) {
        Random random = new Random();
        int maxAttempts = 100;
        int attempts = 0;
        int x, y;
        boolean placed = false;

        while (!placed && attempts < maxAttempts) {
            x = 2 + random.nextInt(size-4);
            y = 2 + random.nextInt(size-4);
            attempts++;

            if (hasSpaceAround(x, y)) {
                if (elementType == TileType.PLAYER) {
                    playerCol = x;
                    playerRow = y;
                }
                grid[y][x] |= elementType;
                placed = true;
                System.out.println("Successfully placed " + elementType + " at " + x + "," + y);
            }
        }

        if (!placed) {
            // If it couldn't place after max attempts, clears some walls and tries again
            clearSomeWalls(size);
            placeWithSpace(elementType);  // Recursive call after clearing walls
        }
    }

    /**
     * Clears some walls to ensure space
     * @param size
     */
    private void clearSomeWalls(int size) {
        // Clears a few random walls to make more space
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            int x = 2 + random.nextInt(size-4);
            int y = 2 + random.nextInt(size-4);
            if (grid[y][x] == TileType.WALL) {
                grid[y][x] = TileType.FLOOR;
            }
        }
    }

    /**
     * Checks if there is space around piece.
     * @param x
     * @param y
     * @return
     */
    private boolean hasSpaceAround(int x, int y) {

        if (grid[y][x] != TileType.FLOOR) {
            return false;
        }

        // Will explain why only adjacent walls are checked and not diagonal in video.
        int[][] adjacent = {{0,1}, {0,-1}, {1,0}, {-1,0}};
        for (int[] dir : adjacent) {
            int newY = y + dir[0];
            int newX = x + dir[1];
            if (grid[newY][newX] != TileType.FLOOR) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if all boxes are placed on all goals.
     * @return Returns whether level is complete.
     */
    public boolean isComplete()
    {
        for (int[] i : goalLocations)
        {
            if (!isBoxOnGoal(i[0],i[1]))
            {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param row
     * @param col
     * @return Returns floor or wall tile on a space.
     */
    public int getStaticTile(int row, int col)
    {
        return grid[row][col] & TileType.STATIC_MASK;
    }

    /**
     * Returns if a tile has a dynamic object like player or wall.
     * @param row
     * @param col
     * @param element
     * @return
     */
    public boolean hasDynamicElement(int row, int col, int element)
    {
        return (grid[row][col] & element) !=0;
    }

    public void addElement(int row, int col, int element)
    {
        grid[row][col] |= element;
    }

    public void removeElement(int row, int col, int element)
    {
        grid[row][col] &= ~element;
    }

    public boolean isBoxOnGoal(int row, int col)
    {
        return (grid[row][col] & TileType.BOX) != 0 && (grid[row][col] & TileType.GOAL) != 0;
    }

    public int[][] getGrid()
    {
        return grid;
    }

    public int getPlayerRow()
    {
        return playerRow;
    }

    public int getPlayerCol()
    {
        return playerCol;
    }

    public ArrayList<int[]> getGoalLocations()
    {
        return goalLocations;
    }

    /**
     * Resets current level.
     */
    public void resetLevel()
    {
        for(int i = 0; i < grid.length; i++) {
            grid[i] = initialGrid[i].clone();
        }

        playerRow = initialPlayerRow;
        playerCol = initialPlayerCol;
    }
}
