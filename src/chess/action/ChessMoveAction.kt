package chess.action

import chess.ChessRules
import chess.ChessState
import chess.Piece
import chess.info.ChessGameInfo
import chess.info.IllegalMoveInfo
import chess.info.NotYourTurnInfo
import java.awt.Point

/**
 * Class: ChessMoveAction
 * The class of every move in the game.
 * Inherits from ChessAction.
 *
 * @author Alex Hadi
 * @version May 2020
 */
open class ChessMoveAction(private val playerId: Int, startPosition: Point, endPosition: Point) : ChessAction() {
    val startPosition: Point
        get() = Point(field)

    val endPosition: Point
        get() = Point(field)

    init {
        this.startPosition = Point(startPosition)
        this.endPosition = Point(endPosition)
    }

    open fun getMovedPiece(piece: Piece) = Piece(piece.pieceType, piece.player, true)

    override fun getNextState(chessState: ChessState): ChessGameInfo? {
        // The player attempted to make a move when it isn't their turn.
        if (!chessState.isMyTurn(this.playerId)) {
            return NotYourTurnInfo()
        }

        // The player attempted to make an illegal move.
        val chessRules = ChessRules(this.startPosition, chessState)
        if (!chessRules.isLegalMove(this.endPosition)) {
            return IllegalMoveInfo()
        }

        return chessState.getNextState(this)
    }
}
