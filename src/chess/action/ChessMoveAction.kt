package chess.action

import java.awt.Point

/**
 * Class: ChessMoveAction
 * The class of every move in the game.
 * Inherits from ChessAction.
 *
 * @author Alex Hadi
 * @version May 2020
 */
open class ChessMoveAction(val playerId: Int, startPosition: Point, endPosition: Point) : ChessAction() {
    val startPosition: Point
        get() = Point(field)

    val endPosition: Point
        get() = Point(field)

    init {
        this.startPosition = Point(startPosition)
        this.endPosition = Point(endPosition)
    }
}
