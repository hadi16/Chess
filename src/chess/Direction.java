package chess;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Enumeration: Direction
 * Denotes one of eight "typical" directions
 * (North, South, East, West, Northeast, Northwest, Southeast, and Southwest)
 *
 * @author Alex Hadi
 * @version May 2019
 */
public enum Direction {
    NORTH(0, -1),
    EAST(1, 0),
    SOUTH(0, 1),
    WEST(-1, 0),
    NORTHEAST(1, -1),
    NORTHWEST(-1, -1),
    SOUTHEAST(1, 1),
    SOUTHWEST(-1, 1);

    // The x & y value associated with the direction.
    // Each is one of these values: -1, 0, 1
    private final int x;
    private final int y;

    /**
     * Constructor: Direction
     * Creates a Direction using the prescribed x & y values.
     *
     * @param x The x value (-1, 0, or 1).
     * @param y The y value (-1, 0, or 1).
     */
    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Static Method: getPawnRegularDirection
     * Gets the direction that a pawn "normally" moves in
     * (when it isn't attacking another piece)
     *
     * @param player The player ID of the pawn (0 or 1).
     * @return The Direction that the pawn can go in for "normal" movements.
     */
    @Nonnull
    public static Direction getPawnRegularDirection(int player) {
        return (player == 0) ? SOUTH : NORTH;
    }

    /**
     * Static Method: getPawnAttackingDirections
     * Gets a list of the Directions that a pawn can move when attacking another piece.
     *
     * @param player The player ID (0 or 1).
     * @return The list of Directions that the pawn can move when attacking.
     */
    @Nonnull
    public static ArrayList<Direction> getPawnAttackingDirections(int player) {
        if (player == 0) {
            return new ArrayList<>(Arrays.asList(SOUTHEAST, SOUTHWEST));
        } else {
            return new ArrayList<>(Arrays.asList(NORTHEAST, NORTHWEST));
        }
    }

    /**
     * Getter: getX
     * Gets the x value associated with the given Direction.
     *
     * @return The x value (-1, 0, or 1).
     */
    public int getX() {
        return x;
    }

    /**
     * Getter: getY
     * Gets the y value associated with the given Direction.
     *
     * @return The y value (-1, 0, or 1).
     */
    public int getY() {
        return y;
    }
}