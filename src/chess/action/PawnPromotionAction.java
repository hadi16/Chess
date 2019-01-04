package chess.action;

import chess.Piece;
import chess.PieceType;

import javax.swing.*;

public class PawnPromotionAction extends JPanel {
    private Piece pawnToPromote;

    public PawnPromotionAction(Piece pawnToPromote) {
        this.pawnToPromote = pawnToPromote;
    }

    public void promotePawn() {
        PieceType[] PROMOTION_OPTIONS = {PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP, PieceType.QUEEN};

        JFrame pawnPromotionFrame = new JFrame("Pawn Promotion");

        // While loop runs until the user selects an option from the dialog box.
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

        pawnToPromote.setPieceType(selectedPieceType);
    }
}
