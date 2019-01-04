package chess.listeners;

import chess.ChessGui;
import chess.ChessState;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class ChessMouseMotionListener extends ChessListener implements MouseMotionListener {
    public ChessMouseMotionListener(ChessState chessState, ChessGui chessGui) {
        super(chessState, chessGui);
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        Point pointClicked = chessState.getValidPointOrNull(new Point(mouseEvent.getX(), mouseEvent.getY()));
        if (pointClicked == null || !chessState.positionInBoard(pointClicked)) {
            chessState.setHoverPoint(null);
        }
        else {
            chessState.setHoverPoint(pointClicked);
        }

        chessGui.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {}
}
