package chess.action

import chess.ChessState
import chess.info.ChessGameInfo

/**
 * Class: ResetGameAction
 * Action in the game triggered by the "New Game" menu item.
 * Inherits from ChessMenuAction.
 *
 * @author Alex Hadi
 * @version May 2020
 */
class ResetGameAction : ChessMenuAction() {
    override fun getNextState(chessState: ChessState): ChessGameInfo? = ChessState()
}
