package chess.gui.listeners

import chess.ChessUtil
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
 * @version May 2020
 */
class ChessMouseMotionListener(chessGui: ChessGui) : ChessListener(chessGui), MouseMotionListener {
    /**
     * Overridden Method: mouseMoved
     * Called when the mouse cursor is moved by the user.
     *
     * @param mouseEvent Reference to the MouseEvent (used to find where the user's mouse is).
     */
    override fun mouseMoved(mouseEvent: MouseEvent) {
        // Uses the x & y positions of the mouse w/ the scaleDim
        // to calculate the position on the board where the user's mouse is.
        ChessUtil.getValidPointOrEmpty(Point(mouseEvent.x, mouseEvent.y), this.chessGui.scaleDim)?.let {
            // Determines if a piece is there & sets the hover point accordingly.
            val hoverPoint: Point? = if (this.chessGui.chessState.positionInBoard(it)) it else null

            this.chessGui.hoverPoint = hoverPoint
        }

        this.chessGui.repaint()
    }

    /*
     * This method is required when implementing MouseMotionListener, but is not used.
     */
    override fun mouseDragged(e: MouseEvent) {}
}
