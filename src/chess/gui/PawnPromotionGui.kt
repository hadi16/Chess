package chess.gui

import chess.PieceType
import javax.swing.JFrame
import javax.swing.JOptionPane.QUESTION_MESSAGE
import javax.swing.JOptionPane.showInputDialog

/**
 * Class: PawnPromotionGui
 * Allows the user to select which piece type to promote their pawn to.
 *
 * @author Alex Hadi
 * @version May 2020
 */
class PawnPromotionGui {
    val selectedPieceType: PieceType

    init {
        this.selectedPieceType = this.showSelectionGuiAndReturnResult()
    }

    /**
     * Method: showSelectionGuiAndReturnResult
     * Shows the GUI for selecting the pawn's new piece type and returns it.
     *
     * @return The selected piece type.
     */
    private fun showSelectionGuiAndReturnResult(): PieceType {
        val PROMOTION_OPTIONS = arrayOf(PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP, PieceType.QUEEN)

        val FRAME = JFrame("Pawn Promotion")
        val MESSAGE = "You can promote your pawn. Please select from the following options."
        val TITLE = "Pawn Promotion"

        while (true) {
            val maybePieceType = showInputDialog(
                    FRAME, MESSAGE, TITLE, QUESTION_MESSAGE, null, PROMOTION_OPTIONS, PROMOTION_OPTIONS[0]
            )

            if (maybePieceType is PieceType) {
                return maybePieceType
            }
        }
    }
}
