package chess.info

/**
 * Abstract Class: ChessGameInfo
 * The abstract base class for info in the Chess game.
 *
 * @author Alex Hadi
 * @version May 2020
 */
abstract class ChessGameInfo {
    open fun maybeErrorMessage(): String? = null
}
