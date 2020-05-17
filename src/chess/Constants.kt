package chess

import java.awt.Color

/**
 * Object: Constants
 * Contains only immutable variables (constant values used throughout the game)
 *
 * @author Alex Hadi
 * @version May 2020
 */
object Constants {
    // The white player always starts first.
    const val DEFAULT_TURN = 1

    // The board width and number of players (standard chess values)
    const val BOARD_WIDTH = 8
    const val NUM_PLAYERS = 2

    // The three custom colors used for the board.
    val FIRST_BOARD_COLOR = Color(255, 205, 159)
    val SECOND_BOARD_COLOR = Color(210, 138, 71)
    val GOLD = Color(255, 215, 0) // For highlighting legal moves.

    // The values of the new, open, and save menus in the game.
    const val NEW_GAME_MENU_TEXT = "New Game"
    const val OPEN_GAME_MENU_TEXT = "Open..."
    const val SAVE_GAME_MENU_TEXT = "Save..."

    // The cardinal directions, diagonal directions, and all directions for the pieces.
    val CARDINAL_DIRECTIONS = listOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)
    val DIAGONAL_DIRECTIONS = listOf(Direction.NORTHWEST, Direction.NORTHEAST, Direction.SOUTHEAST, Direction.SOUTHWEST)
    val ALL_DIRECTIONS: List<Direction> = Direction.values().toList()
}
