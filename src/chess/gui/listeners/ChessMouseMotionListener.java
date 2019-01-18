package chess.gui.listeners;

import chess.ChessState;
import chess.Helpers;
import chess.gui.ChessGui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Class: ChessMouseMotionListener
 * The mouse motion listener for the game (for the hovering text functionality).
 * Inherits from ChessListener.
 *
 * @author Alex Hadi
 * @version January 2019
 */
public class ChessMouseMotionListener extends ChessListener implements MouseMotionListener {
    /**
     * Constructor: ChessMouseMotionListener
     * Creates a new mouse motion listener for the game.
     *
     * @param chessGui The reference to the game's GUI.
     */
    public ChessMouseMotionListener(ChessGui chessGui) {
        super(chessGui);
    }

    /**
     * Overridden Method: mouseMoved
     * Called when the mouse cursor is moved by the user.
     *
     * @param mouseEvent Reference to the MouseEvent (used to find where the user's mouse is).
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        // Uses the x & y positions of the mouse w/ the scaleDim
        // to calculate the position on the board where the user's mouse is.
        Point pointClicked = Helpers.getValidPointOrNull(
                new Point(mouseEvent.getX(), mouseEvent.getY()),
                chessGui.getScaleDim()
        );

        // Determines if a piece is there & sets the hover point accordingly.
        ChessState chessState = chessGui.getChessState();
        Point hoverPoint = chessState.positionInBoard(pointClicked) ? pointClicked : null;
        chessGui.setHoverPoint(hoverPoint);
        chessGui.repaint();
    }

    /*
     * This method is required when implementing MouseMotionListener, but is not used.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
    }
}