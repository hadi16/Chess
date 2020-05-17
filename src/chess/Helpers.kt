package chess

import java.awt.Point

/**
 * Class: Helpers
 * Contains static helper methods that are used throughout the codebase.
 *
 * @author Alex Hadi
 * @version May 2020
 */
class Helpers {
    companion object {
        /**
         * Static Method: positionInBounds
         * Checks if a point is a valid point on the chess board.
         *
         * @param position The Point to check.
         * @return true if a valid point (otherwise false)
         */
        fun positionInBounds(position: Point): Boolean {
            return position.x >= 0 && position.x < Constants.BOARD_WIDTH &&
                    position.y >= 0 && position.y < Constants.BOARD_WIDTH
        }

        /**
         * Static Method: getValidPointOrEmpty
         * Gets the corresponding chess board point that was clicked if it is in bounds.
         * Otherwise, return null.
         *
         * @param mousePosition The Point that was clicked.
         * @param scaleDim The current scaling factor value.
         * @return A valid chess board Point (or null).
         */
        fun getValidPointOrEmpty(mousePosition: Point, scaleDim: Int): Point? {
            // Use the scaling factor to get the corresponding point on the board.
            val pointClicked = Point(mousePosition.x / scaleDim, mousePosition.y / scaleDim)

            // If the position is in bounds of the chess board,
            // return it (otherwise, return empty optional).
            return if (this.positionInBounds(pointClicked)) pointClicked else null
        }
    }
}
