package chess

import chess.action.*
import chess.gui.ChessGui
import chess.info.IllegalMoveInfo
import chess.info.NotYourTurnInfo

/**
 * Class: ChessGame
 * The local chess game. Where the program is executed from.
 *
 * @author Alex Hadi
 * @version July 2019
 */
class ChessGame {
    // The references to the GUI and IO for the program (never reassigned).
    private val chessGui: ChessGui
    private val chessIO = ChessIO()

    // The master state for the chess game
    // (can be reassigned by open and reset game functionality)
    private var chessState: ChessState

    // Creates a new chess game.
    init {
        chessState = ChessState()
        chessGui = ChessGui(this, chessState)
        chessGui.updateCurrentPlayerLegalMoves()
    }

    /**
     * Method: sendUpdatedStateToGui
     * Sends a deep copy of the master ChessState to the ChessGui reference.
     */
    private fun sendUpdatedStateToGui() = chessGui.receiveInfo(ChessState(chessState))

    /**
     * Method: canMove
     * Returns whether the player is allowed to make a move (if it is their turn).
     *
     * @param playerId The player number to check.
     * @return true if the player is allowed to make a move (otherwise false).
     */
    private fun canMove(playerId: Int): Boolean = chessState.isMyTurn(playerId)

    /**
     * Method: receiveAction
     * Receives an action from ChessGui & changes the master game state accordingly.
     * Finally, sends a deep copy of this updated state to the GUI.
     *
     * @param action The ChessAction to perform.
     */
    fun receiveAction(action: ChessAction) {
        // If the user clicked the New, Open, or Save menu items.
        if (action is ChessMenuAction) {
            when (action) {
                // Case 1: The "New" menu item was clicked (reset the game).
                is ResetGameAction -> chessState = ChessState()

                // Case 2: The "Open" menu item was clicked (trigger the open dialog).
                is OpenGameAction -> chessIO.openGame()?.let { chessState = it }

                // Case 3: The "Save" menu item was clicked (trigger the save dialog).
                is SaveGameAction -> chessIO.saveGame(chessState)
            }

            // If the user made a move, first check if it is valid.
            // Then, either send info that move was invalid or perform the move.
        } else if (action is ChessMoveAction) {
            // The player attempted to make a move when it isn't their turn.
            if (!canMove(action.playerId)) {
                chessGui.receiveInfo(NotYourTurnInfo())
                return
            }

            // The player attempted to make an illegal move.
            val chessRules = ChessRules(action.startPosition, chessState)
            if (!chessRules.isLegalMove(action.endPosition)) {
                chessGui.receiveInfo(IllegalMoveInfo())
                return
            }

            chessState = chessState.getNextState(action)
        }

        // Send a deep copy of the updated master state to the GUI.
        sendUpdatedStateToGui()
    }
}

/**
 * Static Method: main
 * The entry point for the program.
 */
fun main() {
    ChessGame()
}
