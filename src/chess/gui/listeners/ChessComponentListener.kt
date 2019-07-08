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
 * @version July 2019
 */
class ChessComponentListener : ChessListener, ComponentListener {
    /**
     * Constructor: ChessComponentListener
     * Creates a new component listener for the game.
     *
     * @param chessGui The reference to the game's GUI.
     */
    constructor(chessGui: ChessGui) : super(chessGui)

    /**
     * Overridden Method: componentResized
     * Calls setGameSize() when the game window is resized by the user.
     *
     * @param c The ComponentEvent reference (it is not used in this implementation)
     */
    override fun componentResized(c: ComponentEvent) = setGameSize()

    /**
     * Method: setGameSize
     * Sets the scaleDim for the game when the window is resized, then calls repaint on the GUI.
     */
    private fun setGameSize() {
        // Finds shorter dimension.
        val gameFrameBounds = chessGui.gameFrameBounds()
        val frameDim = min(gameFrameBounds.height, gameFrameBounds.width)

        // Resets scaleDim and repaints.
        chessGui.scaleDim = frameDim / (Constants.BOARD_WIDTH + 1)
        chessGui.repaint()
    }

    /*
     * These methods are not used, but are required when implementing ComponentListener.
     */
    override fun componentShown(c: ComponentEvent) {}
    override fun componentMoved(c: ComponentEvent) {}
    override fun componentHidden(c: ComponentEvent) {}
}
