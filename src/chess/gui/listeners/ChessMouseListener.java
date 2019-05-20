package chess.gui.listeners;

import chess.ChessRules;
import chess.ChessState;
import chess.Helpers;
import chess.action.ChessAction;
import chess.action.ChessMoveAction;
import chess.action.PawnPromotionAction;
import chess.gui.ChessGui;
import chess.gui.PawnPromotionGui;

import javax.annotation.Nonnull;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Class: ChessMouseListener
 * The mouse listener for the game (allows the user to click pieces).
 * Inherits from ChessListener.
 *
 * @author Alex Hadi
 * @version May 2019
 */
public class ChessMouseListener extends ChessListener implements MouseListener {
    /**
     * Constructor: ChessMouseListener
     * Creates a new mouse listener for the game.
     *
     * @param chessGui The reference to the game's GUI.
     */
    public ChessMouseListener(@Nonnull ChessGui chessGui) {
        super(chessGui);
    }

    /**
     * Overridden Method: mouseClicked
     * Called when the user clicks on the GUI.
     *
     * @param mouseEvent The MouseEvent reference (used to find where the user clicked on the GUI).
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        // Get the current chess state.
        ChessState chessState = chessGui.getChessState();

        // Uses the x and y position of the mouse
        // w/ the scaleDim of the game to calculate the position clicked on the board.
        Point clickedPoint = Helpers.getValidPointOrEmpty(
                new Point(mouseEvent.getX(), mouseEvent.getY()), chessGui.getScaleDim()
        ).orElse(null);

        // The previously set Point is retrieved.
        Point previousClickedPoint = chessGui.getClickedPoint().orElse(null);

        // If the user didn't click a valid point on the board.
        // Or, if the user selected the same position on the board (deselected a piece).
        if (clickedPoint == null || (clickedPoint.equals(previousClickedPoint))) {
            chessGui.setClickedPoint(null);
            chessGui.repaint();
            return;
        }

        // If there was no previously selected Point, save it (if it is a valid point).
        if (previousClickedPoint == null) {
            chessState.getPieceAtPosition(clickedPoint).ifPresent(clickedPiece -> {
                // Makes sure the selected piece belongs to the current player.
                if (chessState.isMyTurn(clickedPiece.getPlayer())) {
                    chessGui.setClickedPoint(clickedPoint);
                } else {
                    System.err.println("It is not your turn!");
                }
            });
        // If there was a previously selected Point, make the move.
        } else {
            // Initializes the action, retrieves the piece to move, and retrieves the player.
            chessState.getPieceAtPosition(previousClickedPoint).ifPresent(pieceToMove -> {
                // Checks if the pawn can be promoted.
                ChessRules chessRules = new ChessRules(previousClickedPoint, chessState);

                ChessAction chessAction;
                int player = pieceToMove.getPlayer();

                if (chessRules.canPromotePawn(clickedPoint)) {
                    // Shows the GUI to the user to select the promoted piece type.
                    PawnPromotionGui promotionGui = new PawnPromotionGui();
                    chessAction = new PawnPromotionAction(
                            player,
                            previousClickedPoint,
                            clickedPoint,
                            promotionGui.getSelectedPieceType()
                    );
                } else {
                    // Should just be a normal move action.
                    chessAction = new ChessMoveAction(player, previousClickedPoint, clickedPoint);
                }

                // Sends the stored action to the game and resets the clicked point.
                chessGui.sendActionToGame(chessAction);
                chessGui.setClickedPoint(null);
            });
        }

        chessGui.repaint();
    }

    /*
     * These methods are required when implementing MouseListener, but are not used.
     */
    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}