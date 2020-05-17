package chess.gui.listeners

import chess.Constants
import chess.action.ChessMenuAction
import chess.action.OpenGameAction
import chess.action.ResetGameAction
import chess.action.SaveGameAction
import chess.gui.ChessGui

import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * Class: ChessMenuBarListener
 * The game's listener for the menu bar.
 * Inherits from ChessListener.
 *
 * @author Alex Hadi
 * @version May 2020
 */
class ChessMenuBarListener(chessGui: ChessGui) : ChessListener(chessGui), ActionListener {
    /**
     * Overridden Method: actionPerformed
     * Called when the user clicks on a menu bar item.
     *
     * @param e The ActionEvent reference (used to determine which menu item was clicked).
     */
    override fun actionPerformed(e: ActionEvent) {
        // The user should have either clicked the
        // open, save, or reset buttons on the menu.
        val chessMenuAction: ChessMenuAction = when (e.actionCommand) {
            Constants.NEW_GAME_MENU_TEXT -> ResetGameAction()
            Constants.OPEN_GAME_MENU_TEXT -> OpenGameAction()
            Constants.SAVE_GAME_MENU_TEXT -> SaveGameAction()

            else -> {
                System.err.println("An invalid menu item was clicked!")
                return
            }
        }

        // Sends the action back to the game and repaints.
        chessGui.sendActionToGame(chessMenuAction)
        chessGui.repaint()
    }
}
