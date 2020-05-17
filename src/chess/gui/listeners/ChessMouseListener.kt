package chess.gui.listeners

import chess.ChessRules
import chess.ChessUtil
import chess.action.ChessAction
import chess.action.ChessMoveAction
import chess.action.PawnPromotionAction
import chess.gui.ChessGui
import chess.gui.PawnPromotionGui
import java.awt.Point
import java.awt.event.MouseEvent
import java.awt.event.MouseListener

/**
 * Class: ChessMouseListener
 * The mouse listener for the game (allows the user to click pieces).
 * Inherits from ChessListener.
 *
 * @author Alex Hadi
 * @version May 2020
 */
class ChessMouseListener(chessGui: ChessGui) : ChessListener(chessGui), MouseListener {
    /**
     * Overridden Method: mouseClicked
     * Called when the user clicks on the GUI.
     *
     * @param mouseEvent The MouseEvent reference (used to find where the user clicked on the GUI).
     */
    override fun mouseClicked(mouseEvent: MouseEvent) {
        // Get the current chess state.
        val chessState = this.chessGui.chessState

        // Uses the x and y position of the mouse
        // w/ the scaleDim of the game to calculate the position clicked on the board.
        val clickedPoint = ChessUtil.getValidPointOrEmpty(
                Point(mouseEvent.x, mouseEvent.y), this.chessGui.scaleDim
        )

        // The previously set Point is retrieved.
        val previousClickedPoint = this.chessGui.clickedPoint

        // If the user didn't click a valid point on the board.
        // Or, if the user selected the same position on the board (deselected a piece).
        if (clickedPoint == null || clickedPoint == previousClickedPoint) {
            this.chessGui.clickedPoint = null
            this.chessGui.repaint()
            return
        }

        // If there was no previously selected Point, save it (if it is a valid point).
        if (previousClickedPoint == null) {
            chessState.getPieceAtPosition(clickedPoint)?.let {
                // Makes sure the selected piece belongs to the current player.
                if (chessState.isMyTurn(it.player)) {
                    this.chessGui.clickedPoint = clickedPoint
                } else {
                    System.err.println("It is not your turn!")
                }
            }
            // If there was a previously selected Point, make the move.
        } else {
            // Initializes the action, retrieves the piece to move, and retrieves the player.
            chessState.getPieceAtPosition(previousClickedPoint)?.let {
                // Checks if the pawn can be promoted.
                val chessRules = ChessRules(previousClickedPoint, chessState)

                val player = it.player
                val chessAction: ChessAction = if (chessRules.canPromotePawn(clickedPoint)) {
                    // Shows the GUI to the user to select the promoted piece type.
                    val promotionGui = PawnPromotionGui()
                    PawnPromotionAction(
                            player,
                            previousClickedPoint,
                            clickedPoint,
                            promotionGui.selectedPieceType
                    )
                } else {
                    // Should just be a normal move action.
                    ChessMoveAction(player, previousClickedPoint, clickedPoint)
                }

                // Sends the stored action to the game and resets the clicked point.
                this.chessGui.sendActionToGame(chessAction)
                this.chessGui.clickedPoint = null
            }
        }

        this.chessGui.repaint()
    }

    /* These methods are required when implementing MouseListener, but are not used. */
    override fun mousePressed(e: MouseEvent) {}
    override fun mouseReleased(e: MouseEvent) {}
    override fun mouseEntered(e: MouseEvent) {}
    override fun mouseExited(e: MouseEvent) {}
}
