package chess.action;

import chess.ChessState;
import chess.Piece;
import chess.PieceType;

import java.awt.*;

public class ChessMoveAction {
    private ChessState chessState;

    public ChessMoveAction(ChessState chessState) {
        this.chessState = chessState;
    }

    public void movePiece(Point start, Point end) {
        Piece pieceToMove = chessState.removeAndReturnPieceAtPosition(start);
        pieceToMove.setMoved(true);
        chessState.setBoardAtPosition(end, pieceToMove);

        // Checks for pawn promotion.
        int player = pieceToMove.getPlayer();
        if (pieceToMove.getPieceType() == PieceType.PAWN) {
            if ((player == 0 && end.y == 7) || (player == 1 && end.y == 0)) {
                PawnPromotionAction pawnPromotionAction = new PawnPromotionAction(pieceToMove);
                pawnPromotionAction.promotePawn();
            }
        }

        // Change the turn.
        chessState.changeTurn();
    }
}
