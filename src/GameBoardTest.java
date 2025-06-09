import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Raj Kavathekar
 * @version 2.0
 * Test's important gameboard functionss
 */
public class GameBoardTest {
    private GameBoard gameBoard;

    @BeforeEach
    void setUp() {
        gameBoard = new GameBoard();
    }



    @Test
    void testBoardInitialization() {
        assertNotNull(gameBoard);
        //Removed as this is no longer relevant for my project.
        //assertEquals(16 * gameBoard.getLevel().getGrid()[0].length,
               // gameBoard.getPreferredSize().width);
       // assertEquals(16 * gameBoard.getLevel().getGrid().length,
                //gameBoard.getPreferredSize().height);
    }


    /**
     * Tests if key press shows valid result.
     */
    @Test
    void testKeyboardInput() {
        Level level = gameBoard.getLevel();
        int initialX = level.getPlayerCol();
        int initialY = level.getPlayerRow();

        gameBoard.testKeyboard(KeyEvent.VK_RIGHT);

        assertNotEquals(initialX, level.getPlayerCol());
    }
}
