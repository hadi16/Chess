package chess.listeners;

import chess.ChessGui;
import chess.ChessRules;
import chess.ChessState;
import chess.Piece;
import chess.action.ChessMoveAction;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ChessMouseListener extends ChessListener implements MouseListener {
    private ChessRules chessRules = new ChessRules();

    public ChessMouseListener(ChessState chessState, ChessGui chessGui) {
        super(chessState, chessGui);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Point clickedPoint = chessState.getValidPointOrNull(new Point(mouseEvent.getX(), mouseEvent.getY()));
        if (clickedPoint == null || chessState.getClickedPoint() == clickedPoint) {
            chessState.setClickedPoint(null);
            return;
        }

        if (chessState.getClickedPoint() == null) {
            Piece clickedPiece = chessState.getBoardDeepCopy().get(clickedPoint);
            if (clickedPiece == null || !chessState.isMyTurn(clickedPiece.getPlayer())) {
                return;
            }
            chessState.setClickedPoint(clickedPoint);
        } else {
            if (chessRules.isLegalMove(chessState.getClickedPoint(), clickedPoint, chessState.getBoardDeepCopy())) {
                ChessMoveAction chessMoveAction = new ChessMoveAction(chessState);
                chessMoveAction.movePiece(chessState.getClickedPoint(), clickedPoint);
            }
            chessState.setClickedPoint(null);
        }

        chessGui.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
