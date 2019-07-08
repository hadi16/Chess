package chess.gui.listeners

import chess.Helpers
import chess.gui.ChessGui
import java.awt.Point
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionListener

/**
 * Class: ChessMouseMotionListener
 * The mouse motion listener for the game (for the hovering text functionality).
 * Inherits from ChessListener.
 *
 * @author Alex Hadi
 * @version July 2019
 */
class ChessMouseMotionListener : ChessListener, MouseMotionListener {
    /**
     * Constructor: ChessMouseMotionListener
     * Creates a new mouse motion listener for the game.
     *
     * @param chessGui The reference to the game's GUI.
     */
    constructor(chessGui: ChessGui) : super(chessGui)

    /**
     * Overridden Method: mouseMoved
     * Called when the mouse cursor is moved by the user.
     *
     * @param mouseEvent Reference to the MouseEvent (used to find where the user's mouse is).
     */
    override fun mouseMoved(mouseEvent: MouseEvent) {
        // Uses the x & y positions of the mouse w/ the scaleDim
        // to calculate the position on the board where the user's mouse is.
        Helpers.getValidPointOrEmpty(Point(mouseEvent.x, mouseEvent.y), chessGui.scaleDim)?.let {
            // Determines if a piece is there & sets the hover point accordingly.
            val hoverPoint: Point? = if (chessGui.chessState.positionInBoard(it)) it else null

            chessGui.hoverPoint = hoverPoint
        }

        chessGui.repaint()
    }

    /*
     * This method is required when implementing MouseMotionListener, but is not used.
     */
    override fun mouseDragged(e: MouseEvent) {}
}
