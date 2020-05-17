package chess.gui.listeners

import chess.Constants
import chess.gui.ChessGui

import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import kotlin.math.min

/**
 * Class: ChessComponentListener
 * The component listener for the game. Allows the game's GUI to be resized.
 * Inherits from ChessListener.
 *
 * @author Alex Hadi
 * @version May 2020
 */
class ChessComponentListener(chessGui: ChessGui) : ChessListener(chessGui), ComponentListener {
    /**
     * Overridden Method: componentResized
     * Calls setGameSize() when the game window is resized by the user.
     *
     * @param c The ComponentEvent reference (it is not used in this implementation)
     */
    override fun componentResized(c: ComponentEvent) = this.setGameSize()

    /**
     * Method: setGameSize
     * Sets the scaleDim for the game when the window is resized, then calls repaint on the GUI.
     */
    private fun setGameSize() {
        // Finds shorter dimension.
        val gameFrameBounds = this.chessGui.gameFrameBounds()
        val frameDim = min(gameFrameBounds.height, gameFrameBounds.width)

        // Resets scaleDim and repaints.
        this.chessGui.scaleDim = frameDim / (Constants.BOARD_WIDTH + 1)
        this.chessGui.repaint()
    }

    /*
     * These methods are not used, but are required when implementing ComponentListener.
     */
    override fun componentShown(c: ComponentEvent) {}
    override fun componentMoved(c: ComponentEvent) {}
    override fun componentHidden(c: ComponentEvent) {}
}
