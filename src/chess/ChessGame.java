package chess;

import chess.action.*;
import chess.gui.ChessGui;
import chess.info.IllegalMoveInfo;
import chess.info.NotYourTurnInfo;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Class: ChessGame
 * The local chess game. Where the program is executed from.
 *
 * @author Alex Hadi
 * @version May 2019
 */
public class ChessGame {
    // The references to the GUI and IO for the program (never reassigned).
    @Nonnull private final ChessGui chessGui;
    @Nonnull private final ChessIO chessIO = new ChessIO();

    // The master state for the chess game
    // (can be reassigned by open and reset game functionality)
    @Nonnull private ChessState chessState;

    /**
     * Static Method: main
     * The entry point for the program.
     *
     * @param args Passed in arguments (as an array of strings)
     */
    public static void main(String[] args) {
        new ChessGame();
    }

    /**
     * Private Constructor: ChessGame
     * Creates a new chess game.
     */
    private ChessGame() {
        chessState = new ChessState();
        chessGui = new ChessGui(this, chessState);
        chessGui.updateCurrentPlayerLegalMoves();
    }

    /**
     * Method: sendUpdatedStateToGui
     * Sends a deep copy of the master ChessState to the ChessGui reference.
     */
    private void sendUpdatedStateToGui() {
        chessGui.receiveInfo(new ChessState(chessState));
    }

    /**
     * Method: canMove
     * Returns whether the player is allowed to make a move (if it is their turn).
     *
     * @param playerId The player number to check.
     * @return true if the player is allowed to make a move (otherwise false).
     */
    private boolean canMove(int playerId) {
        return chessState.isMyTurn(playerId);
    }

    /**
     * Method: receiveAction
     * Receives an action from ChessGui & changes the master game state accordingly.
     * Finally, sends a deep copy of this updated state to the GUI.
     *
     * @param action The ChessAction to perform.
     */
    public void receiveAction(@Nonnull ChessAction action) {
        Objects.requireNonNull(action);

        // If the user clicked the New, Open, or Save menu items.
        if (action instanceof ChessMenuAction) {
            // Case 1: The "New" menu item was clicked (reset the game).
            if (action instanceof ResetGameAction) {
                chessState = new ChessState();
            // Case 2: The "Open" menu item was clicked (trigger the open dialog).
            } else if (action instanceof OpenGameAction) {
                chessIO.openGame().ifPresent(
                        stateToSet -> chessState = stateToSet
                );
            // Case 3: The "Save" menu item was clicked (trigger the save dialog).
            } else if (action instanceof SaveGameAction) {
                chessIO.saveGame(chessState);
            }
        // If the user made a move, first check if it is valid.
        // Then, either send info that move was invalid or perform the move.
        } else if (action instanceof ChessMoveAction) {
            ChessMoveAction moveAction = (ChessMoveAction) action;

            // The player attempted to make a move when it isn't their turn.
            if (!canMove(moveAction.getPlayerId())) {
                NotYourTurnInfo notYourTurnInfo = new NotYourTurnInfo();
                chessGui.receiveInfo(notYourTurnInfo);
                return;
            }

            // The player attempted to make an illegal move.
            ChessRules chessRules = new ChessRules(moveAction.getStartPosition(), chessState);
            if (!chessRules.isLegalMove(moveAction.getEndPosition())) {
                IllegalMoveInfo illegalMoveInfo = new IllegalMoveInfo();
                chessGui.receiveInfo(illegalMoveInfo);
                return;
            }

            chessState = chessState.getNextState(moveAction);
        }

        // Send a deep copy of the updated master state to the GUI.
        sendUpdatedStateToGui();
    }
}