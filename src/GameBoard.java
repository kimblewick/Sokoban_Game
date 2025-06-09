import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author Raj Kavathekar
 * @version 1.0
 * Game board visualizes the Level and handles input.
 */
public class GameBoard extends JPanel {
    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 10;
    private static final int TILE_SIZE = 16;
    // Multiplies the size of my tiny drawings.
    private static final int SCALE = 4;
    private Level level;

    /**
     * Constructor for GameBoard that sets the UI and key binds.
     */
    public GameBoard()
    {
        level = new Level();
        setPreferredSize(new Dimension(level.getGrid().length*TILE_SIZE*SCALE , level.getGrid()[0].length*TILE_SIZE*SCALE));

        setFocusable(true);
        requestFocus();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode())
                {
                    case KeyEvent.VK_N:
                        level.generateSimpleLevel();
                        break;
                    case KeyEvent.VK_R:
                        level.resetLevel();
                        break;
                    case KeyEvent.VK_LEFT:
                        level.movePlayer(-1, 0);
                        break;
                    case KeyEvent.VK_RIGHT:
                        level.movePlayer(1, 0);
                        break;
                    case KeyEvent.VK_UP:
                        level.movePlayer(0, -1);
                        break;
                    case KeyEvent.VK_DOWN:
                        level.movePlayer(0, 1);
                        break;
                }

                repaint(); // Redraws the board after movement
            }
        });

    }

    /**
     * Paints and draws the level according to the tileset of my sprite and increases their size.
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics g2d = (Graphics) g;

        int[][] map = level.getGrid();
        for (int row = 0; row < map.length; row++)
        {
            for (int col = 0; col < map[0].length; col++)
            {
                int tile = map[row][col];

                if((tile & TileType.FLOOR) != 0)
                {
                    g2d.drawImage(Sprite.getSprite(TileType.FLOOR), col * TILE_SIZE * SCALE, row * TILE_SIZE* SCALE, null);
                }

                if((tile & TileType.WALL) != 0)
                {
                    g2d.drawImage(Sprite.getSprite(TileType.WALL), col * TILE_SIZE * SCALE, row * TILE_SIZE * SCALE, null);
                }

                if((tile & TileType.GOAL) != 0)
                {
                    g2d.drawImage(Sprite.getSprite(TileType.GOAL), col * TILE_SIZE * SCALE, row * TILE_SIZE * SCALE, null);
                }

                if((tile & TileType.BOX) != 0)
                {
                    g2d.drawImage(Sprite.getSprite(TileType.BOX), col * TILE_SIZE * SCALE, row * TILE_SIZE * SCALE, null);
                }

                if((tile & TileType.PLAYER) != 0)
                {
                    g2d.drawImage(Sprite.getSprite(TileType.PLAYER), col * TILE_SIZE * SCALE, row * TILE_SIZE * SCALE, null);
                }
            }
        }
    }

    /**
     * Only for JUnit Test.
     * @param Key
     */
    public void testKeyboard(int Key)
    {
        switch (Key)
        {
            case KeyEvent.VK_R:
                level.resetLevel();
                break;
            case KeyEvent.VK_LEFT:
                level.movePlayer(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                level.movePlayer(1, 0);
                break;
            case KeyEvent.VK_UP:
                level.movePlayer(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                level.movePlayer(0, 1);
                break;
        }
    }


    public Level getLevel()
    {
        return level;
    }


}
