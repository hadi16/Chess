package chess.action

import chess.ChessState
import chess.info.ChessGameInfo

/**
 * Abstract Class: ChessAction
 * The abstract base class of every action in the game.
 *
 * @author Alex Hadi
 * @version May 2020
 */
abstract class ChessAction {
    abstract fun getNextState(chessState: ChessState): ChessGameInfo?
}
