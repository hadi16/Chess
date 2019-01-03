import javax.swing.*;
import java.awt.*;

public class PawnPromotion extends JPanel {
    public PawnPromotion(Point position, int player, int scaleDim, Piece[][] board) {
        PieceType[] PIECE_TYPES = {PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP, PieceType.QUEEN};

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
                    PIECE_TYPES,
                    PIECE_TYPES[0]
            );
        }

        board[position.x][position.y] = new Piece(
                selectedPieceType, new Point(position.x * scaleDim, position.y * scaleDim), player, scaleDim
        );
    }
}
