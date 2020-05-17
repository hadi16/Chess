package chess.action

import chess.ChessIO
import chess.ChessState
import chess.info.ChessGameInfo

/**
 * Class: SaveGameAction
 * Action in the game triggered by the "Save" menu item.
 * Inherits from ChessMenuAction.
 *
 * @author Alex Hadi
 * @version May 2020
 */
class SaveGameAction : ChessMenuAction() {
    override fun getNextState(chessState: ChessState): ChessGameInfo? {
        ChessIO.saveGame(chessState)
        return null
    }
}
