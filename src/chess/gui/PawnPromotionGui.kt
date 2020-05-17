package chess.gui

import chess.PieceType
import javax.swing.JFrame
import javax.swing.JOptionPane

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
        selectedPieceType = showSelectionGuiAndReturnResult()
    }

    /**
     * Method: showSelectionGuiAndReturnResult
     * Shows the GUI for selecting the pawn's new piece type and returns it.
     *
     * @return The selected piece type.
     */
    private fun showSelectionGuiAndReturnResult(): PieceType {
        // Constant that is never changed (declared as val)
        val PROMOTION_OPTIONS = arrayOf(
                PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP, PieceType.QUEEN
        )

        // While loop runs until the user selects an option from the dialog box.
        val pawnPromotionFrame = JFrame("Pawn Promotion")

        var selectedPieceType: PieceType? = null
        while (selectedPieceType == null) {
            selectedPieceType = JOptionPane.showInputDialog(
                    pawnPromotionFrame,
                    "You can promote your pawn. Please select from the following options.",
                    "Pawn Promotion",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    PROMOTION_OPTIONS,
                    PROMOTION_OPTIONS[0]
            ) as PieceType
        }
        return selectedPieceType
    }
}
