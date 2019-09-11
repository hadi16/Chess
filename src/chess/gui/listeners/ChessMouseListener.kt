package chess.gui.listeners

import chess.ChessRules
import chess.Helpers
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
 * @version July 2019
 */
class ChessMouseListener : ChessListener, MouseListener {
    /**
     * Constructor: ChessMouseListener
     * Creates a new mouse listener for the game.
     *
     * @param chessGui The reference to the game's GUI.
     */
    constructor(chessGui: ChessGui) : super(chessGui)

    /**
     * Overridden Method: mouseClicked
     * Called when the user clicks on the GUI.
     *
     * @param mouseEvent The MouseEvent reference (used to find where the user clicked on the GUI).
     */
    override fun mouseClicked(mouseEvent: MouseEvent) {
        // Get the current chess state.
        val chessState = chessGui.chessState

        // Uses the x and y position of the mouse
        // w/ the scaleDim of the game to calculate the position clicked on the board.
        val clickedPoint = Helpers.getValidPointOrEmpty(
                Point(mouseEvent.x, mouseEvent.y), chessGui.scaleDim
        )

        // The previously set Point is retrieved.
        val previousClickedPoint = chessGui.clickedPoint

        // If the user didn't click a valid point on the board.
        // Or, if the user selected the same position on the board (deselected a piece).
        if (clickedPoint == null || (clickedPoint == previousClickedPoint)) {
            chessGui.clickedPoint = null
            chessGui.repaint()
            return
        }

        // If there was no previously selected Point, save it (if it is a valid point).
        if (previousClickedPoint == null) {
            chessState.getPieceAtPosition(clickedPoint)?.let {
                // Makes sure the selected piece belongs to the current player.
                if (chessState.isMyTurn(it.player)) {
                    chessGui.clickedPoint = clickedPoint
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

                val chessAction: ChessAction
                val player = it.player

                if (chessRules.canPromotePawn(clickedPoint)) {
                    // Shows the GUI to the user to select the promoted piece type.
                    val promotionGui = PawnPromotionGui()
                    chessAction = PawnPromotionAction(
                            player,
                            previousClickedPoint,
                            clickedPoint,
                            promotionGui.selectedPieceType
                    )
                } else {
                    // Should just be a normal move action.
                    chessAction = ChessMoveAction(player, previousClickedPoint, clickedPoint)
                }

                // Sends the stored action to the game and resets the clicked point.
                chessGui.sendActionToGame(chessAction)
                chessGui.clickedPoint = null
            }
        }

        chessGui.repaint()
    }

    /*
     * These methods are required when implementing MouseListener, but are not used.
     */
    override fun mousePressed(e: MouseEvent) {}
    override fun mouseReleased(e: MouseEvent) {}
    override fun mouseEntered(e: MouseEvent) {}
    override fun mouseExited(e: MouseEvent) {}
}