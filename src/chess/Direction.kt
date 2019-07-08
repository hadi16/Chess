package chess

/**
 * Enumeration: Direction
 * Denotes one of eight "typical" directions
 * (North, South, East, West, Northeast, Northwest, Southeast, and Southwest)
 *
 * @author Alex Hadi
 * @version July 2019
 */
enum class Direction {
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
    val x: Int
    val y: Int

    /**
     * Constructor: Direction
     * Creates a Direction using the prescribed x & y values.
     *
     * @param x The x value (-1, 0, or 1).
     * @param y The y value (-1, 0, or 1).
     */
    constructor(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    companion object {
        /**
         * Static Method: getPawnRegularDirection
         * Gets the direction that a pawn "normally" moves in
         * (when it isn't attacking another piece)
         *
         * @param player The player ID of the pawn (0 or 1).
         * @return The Direction that the pawn can go in for "normal" movements.
         */
        fun getPawnRegularDirection(player: Int): Direction {
            return if (player == 0) {
                SOUTH
            } else {
                NORTH
            }
        }

        /**
         * Static Method: getPawnAttackingDirections
         * Gets a list of the Directions that a pawn can move when attacking another piece.
         *
         * @param player The player ID (0 or 1).
         * @return The list of Directions that the pawn can move when attacking.
         */
        fun getPawnAttackingDirections(player: Int): List<Direction> {
            return if (player == 0) {
                listOf(SOUTHEAST, SOUTHWEST)
            } else {
                listOf(NORTHEAST, NORTHWEST)
            }
        }
    }
}
