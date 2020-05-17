package chess.info

/**
 * Class: IllegalMoveInfo
 * Signifies that the player attempted to make an illegal move.
 * Inherits from ChessGameInfo.
 *
 * @author Alex Hadi
 * @version May 2020
 */
class IllegalMoveInfo : ChessGameInfo() {
    override fun maybeErrorMessage(): String? = "You attempted to make an illegal move!"
}
