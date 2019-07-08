package chess.action

import java.awt.Point

/**
 * Class: ChessMoveAction
 * The class of every move in the game.
 * Inherits from ChessAction.
 *
 * @author Alex Hadi
 * @version July 2019
 */
open class ChessMoveAction : ChessAction {
    val playerId: Int

    val startPosition: Point
        get() = Point(field)

    val endPosition: Point
        get() = Point(field)

    /**
     * Constructor: ChessMoveAction
     * Creates a new action to move a chess piece.
     *
     * @param playerId The ID of the player requesting the move.
     * @param startPosition The current position of the piece.
     * @param endPosition The position of the piece to move to.
     */
    constructor(playerId: Int, startPosition: Point, endPosition: Point) {
        this.playerId = playerId

        // Uses Point class copy constructor
        this.startPosition = Point(startPosition)
        this.endPosition = Point(endPosition)
    }
}
