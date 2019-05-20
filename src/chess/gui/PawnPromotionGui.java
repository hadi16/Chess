package chess.gui;

import chess.PieceType;

import javax.annotation.Nonnull;
import javax.swing.*;

/**
 * Class: PawnPromotionGui
 * Allows the user to select which piece type to promote their pawn to.
 *
 * @author Alex Hadi
 * @version May 2019
 */
public class PawnPromotionGui {
    // The selected piece type is only assigned once.
    @Nonnull private final PieceType selectedPieceType;

    /**
     * Constructor: PawnPromotionGui
     * Creates the GUI to select the piece type for pawn promotion.
     */
    public PawnPromotionGui() {
        selectedPieceType = showSelectionGuiAndReturnResult();
    }

    /**
     * Method: showSelectionGuiAndReturnResult
     * Shows the GUI for selecting the pawn's new piece type and returns it.
     *
     * @return The selected piece type.
     */
    @Nonnull
    private PieceType showSelectionGuiAndReturnResult() {
        // Constant that is never changed (declared as final)
        final PieceType[] PROMOTION_OPTIONS = {
                PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP, PieceType.QUEEN
        };

        // While loop runs until the user selects an option from the dialog box.
        JFrame pawnPromotionFrame = new JFrame("Pawn Promotion");
        PieceType selectedPieceType = null;
        while (selectedPieceType == null) {
            selectedPieceType = (PieceType) JOptionPane.showInputDialog(
                    pawnPromotionFrame,
                    "You can promote your pawn. Please select from the following options.",
                    "Pawn Promotion",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    PROMOTION_OPTIONS,
                    PROMOTION_OPTIONS[0]
            );
        }
        return selectedPieceType;
    }

    /**
     * Getter: getSelectedPieceType
     * Gets the piece type that the user selected for the pawn.
     *
     * @return The selected piece type (instance of PieceType enum).
     */
    @Nonnull
    public PieceType getSelectedPieceType() {
        return selectedPieceType;
    }
}