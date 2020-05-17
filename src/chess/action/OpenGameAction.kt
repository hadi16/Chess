package chess.action

import chess.ChessIO
import chess.ChessState
import chess.info.ChessGameInfo

/**
 * Class: OpenGameAction
 * Action in the game triggered by the "Open" menu item.
 * Inherits from ChessMenuAction.
 *
 * @author Alex Hadi
 * @version May 2020
 */
class OpenGameAction : ChessMenuAction() {
    override fun getNextState(chessState: ChessState): ChessGameInfo? = ChessIO.openGame()
}
