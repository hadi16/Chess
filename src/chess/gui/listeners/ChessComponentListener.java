package chess.gui.listeners;

import chess.Constants;
import chess.gui.ChessGui;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * Class: ChessComponentListener
 * The component listener for the game. Allows the game's GUI to be resized.
 * Inherits from ChessListener.
 *
 * @author Alex Hadi
 * @version January 2019
 */
public class ChessComponentListener extends ChessListener implements ComponentListener {
    /**
     * Constructor: ChessComponentListener
     * Creates a new component listener for the game.
     *
     * @param chessGui The reference to the game's GUI.
     */
    public ChessComponentListener(ChessGui chessGui) {
        super(chessGui);
    }

    /**
     * Overridden Method: componentResized
     * Calls setGameSize() when the game window is resized by the user.
     *
     * @param c The ComponentEvent reference (it is not used in this implementation)
     */
    @Override
    public void componentResized(ComponentEvent c) {
        setGameSize();
    }

    /**
     * Method: setGameSize
     * Sets the scaleDim for the game when the window is resized, then calls repaint on the GUI.
     */
    private void setGameSize() {
        // Finds shorter dimension.
        Rectangle gameFrameBounds = chessGui.getGameFrameBounds();
        int frameDim = Math.min(gameFrameBounds.height, gameFrameBounds.width);

        // Resets scaleDim and repaints.
        chessGui.setScaleDim(frameDim / (Constants.BOARD_WIDTH + 1));
        chessGui.repaint();
    }

    /*
     * These methods are not used, but are required when implementing ComponentListener.
     */
    @Override
    public void componentShown(ComponentEvent c) {
    }

    @Override
    public void componentMoved(ComponentEvent c) {
    }

    @Override
    public void componentHidden(ComponentEvent c) {
    }
}