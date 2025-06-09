import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Raj Kavathekar
 * @version 2.0
 * Test to check important functions of level such as player and box movements.
 * Did not test random map as I did not have anything to test it with.
 */
public class LevelTest {
    private Level level;

    @BeforeEach
    void setUp()
    {
        level = new Level();
    }

    /**
     * Tests whether player has moved to the right square.
     */
    @Test
    void testPlayerMovement()
    {
        int initialX = level.getPlayerCol();
        int initialY = level.getPlayerRow();

        assertTrue(level.movePlayer(1,0));
        assertEquals(initialX + 1, level.getPlayerCol());
        assertEquals(initialY, level.getPlayerRow());

        // There was a wall here.
        //assertFalse(level.movePlayer(-1,-1));
    }

    /**
     * Tests whether box is being pushed
     */
    @Test
    void testBoxPushing() {
        int[][] map = level.getGrid();
        int boxX = -1, boxY = -1;

        // Finding a boz
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if ((map[y][x] & TileType.BOX) != 0) {
                    boxX = x;
                    boxY = y;
                    break;
                }
            }
        }

        // Testing pushing box to empty space
        assertTrue((level.getGrid()[boxY][boxX] & TileType.BOX) != 0);
        level.movePlayer(boxX - level.getPlayerCol()-1, boxY - level.getPlayerRow());
        assertTrue(level.movePlayer(1, 0));
    }

    @Test
    void testLevelCompletion() {
        // Initial state shouldn't be complete
        assertFalse(level.isComplete());

        // Moving all boxes to goals and verify completion
        int[][] map = level.getGrid();
        for (int[] i : level.getGoalLocations()) {
            level.addElement(i[0],i[1],TileType.BOX);
        }
        assertTrue(level.isComplete());
    }
}
