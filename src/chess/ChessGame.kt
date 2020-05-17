package chess

import chess.action.ChessAction
import chess.gui.ChessGui

/**
 * Object: ChessGame
 * The local chess game. Where the program is executed from.
 *
 * @author Alex Hadi
 * @version May 2020
 */
object ChessGame {
    private val chessGui: ChessGui
    private var chessState: ChessState

    init {
        this.chessState = ChessState()
        this.chessGui = ChessGui(this, this.chessState)
        this.chessGui.updateCurrentPlayerLegalMoves()
    }

    /**
     * Method: sendUpdatedStateToGui
     * Sends a deep copy of the master ChessState to the ChessGui reference.
     */
    private fun sendUpdatedStateToGui() = this.chessGui.receiveInfo(ChessState(this.chessState))

    /**
     * Method: receiveAction
     * Receives an action from ChessGui & changes the master game state accordingly.
     * Finally, sends a deep copy of this updated state to the GUI.
     *
     * @param action The ChessAction to perform.
     */
    fun receiveAction(action: ChessAction) {
        action.getNextState(this.chessState)?.let {
            when (it) {
                is ChessState -> this.chessState = it
                else -> this.chessGui.receiveInfo(it)
            }
        }

        // Send a deep copy of the updated master state to the GUI.
        this.sendUpdatedStateToGui()
    }
}
