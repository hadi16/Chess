package chess.gui.listeners;

import chess.Constants;
import chess.action.ChessMenuAction;
import chess.action.OpenGameAction;
import chess.action.ResetGameAction;
import chess.action.SaveGameAction;
import chess.gui.ChessGui;

import javax.annotation.Nonnull;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class: ChessMenuBarListener
 * The game's listener for the menu bar.
 * Inherits from ChessListener.
 *
 * @author Alex Hadi
 * @version May 2019
 */
public class ChessMenuBarListener extends ChessListener implements ActionListener {
    /**
     * Constructor: ChessMenuBarListener
     * Creates a new listener for the menu bar.
     *
     * @param chessGui The reference to the game's GUI.
     */
    public ChessMenuBarListener(@Nonnull ChessGui chessGui) {
        super(chessGui);
    }

    /**
     * Overridden Method: actionPerformed
     * Called when the user clicks on a menu bar item.
     *
     * @param e The ActionEvent reference (used to determine which menu item was clicked).
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // The user should have either clicked the
        // open, save, or reset buttons on the menu.
        ChessMenuAction chessMenuAction;
        switch (e.getActionCommand()) {
            case Constants.NEW_GAME_MENU_TEXT:
                chessMenuAction = new ResetGameAction();
                break;
            case Constants.OPEN_GAME_MENU_TEXT:
                chessMenuAction = new OpenGameAction();
                break;
            case Constants.SAVE_GAME_MENU_TEXT:
                chessMenuAction = new SaveGameAction();
                break;
            default:
                System.err.println("An invalid menu item was clicked!");
                return;
        }

        // Sends the action back to the game and repaints.
        chessGui.sendActionToGame(chessMenuAction);
        chessGui.repaint();
    }
}