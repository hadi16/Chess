package chess

/**
 * Enumeration: Direction
 * Denotes one of eight "typical" directions
 * (North, South, East, West, Northeast, Northwest, Southeast, and Southwest)
 *
 * @author Alex Hadi
 * @version May 2020
 */
enum class Direction(val x: Int, val y: Int) {
    NORTH(0, -1),
    EAST(1, 0),
    SOUTH(0, 1),
    WEST(-1, 0),
    NORTHEAST(1, -1),
    NORTHWEST(-1, -1),
    SOUTHEAST(1, 1),
    SOUTHWEST(-1, 1);

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
            return if (player == 0) SOUTH else NORTH
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
