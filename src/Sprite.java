import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Raj Kavathekar
 * @version 1.0
 * Class to organize and return sprites from my sprite sheet.
 */
public class Sprite {
    private static BufferedImage spriteSheet;
    private static final Map<Integer, BufferedImage> sprites = new HashMap<>();
    private static final int SCALE = 4;

    /**
     * Loads sprites and organizes them, I hope it works for you.
     */
    public static void loadSprites()
    {
        try
        {
            String currentPath = System.getProperty("user.dir");
            File file = new File(currentPath + "/src/spritesheet.png");

            if (!file.exists()) {
                System.err.println("Sprite sheet not found at: " + file.getAbsolutePath());
                return;
            }
            spriteSheet = ImageIO.read(file);
            sprites.put(TileType.PLAYER, scaleImage(spriteSheet.getSubimage(0,0,16,16)));
            sprites.put(TileType.BOX, scaleImage(spriteSheet.getSubimage(16,0,16,16)));
            sprites.put(TileType.FLOOR, scaleImage(spriteSheet.getSubimage(32,0,16,16)));
            sprites.put(TileType.WALL, scaleImage(spriteSheet.getSubimage(48,0,16,16)));
            sprites.put(TileType.GOAL, scaleImage(spriteSheet.getSubimage(64,0,16,15)));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Scales my tiny drawings to 4 times their size.
     * @param original Takes ins the small 16 bit by 16 bit image.
     * @return 4x scaled image.
     */
    private static BufferedImage scaleImage(BufferedImage original) {
        BufferedImage scaled = new BufferedImage(
                original.getWidth() * SCALE,
                original.getHeight() * SCALE,
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2d = scaled.createGraphics();
        g2d.drawImage(original, 0, 0, original.getWidth() * SCALE, original.getHeight() * SCALE, null);
        g2d.dispose();
        return scaled;
    }

    /**
     * @param ID Takes in the TileType value of sprite.
     * @return Returns the sprite
     */
    public static BufferedImage getSprite(int ID)
    {
        return sprites.get(ID);
    }
}
