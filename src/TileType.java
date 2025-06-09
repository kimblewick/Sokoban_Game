/**
 * @author Raj Kavathekar
 * @version 1.0
 * I implemented this class to be more efficient in terms of menory instead of 2D array
 * This allowd me to hold more than 1 number on a tile by appending the number with another one in case of
 * Dynamic or moving objects such as box, player etc as the floor tile below that has its own tile type.
 */
public class TileType {
    //Static elements (lower 4 bits)
    public static final int FLOOR = 0b0001;  // 1
    public static final int WALL  = 0b0010;  // 2
    public static final int GOAL  = 0b0100;  // 4

    //Dynamic elements (upper 4 bits)
    public static final int PLAYER = 0b0001_0000;  // 16
    public static final int BOX    = 0b0010_0000;  // 32

    //Helper masks
    public static final int STATIC_MASK  = 0b0000_1111;  // Get static elements
    public static final int DYNAMIC_MASK = 0b1111_0000;  // Get dynamic elements
}
