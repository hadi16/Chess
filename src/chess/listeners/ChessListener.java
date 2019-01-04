package chess.listeners;

import chess.ChessGui;
import chess.ChessState;
import chess.Helpers;

import java.awt.*;

public abstract class ChessListener {
    protected ChessState chessState;
    protected ChessGui chessGui;

    public ChessListener(ChessState chessState, ChessGui chessGui) {
        this.chessState = chessState;
        this.chessGui = chessGui;
    }

    protected Point getValidPointOrNull(Point mousePosition) {
        Point pointClicked = new Point(
                mousePosition.x / chessGui.getScaleDim(),
                mousePosition.y / chessGui.getScaleDim()
        );

        return Helpers.positionInBounds(pointClicked) ? pointClicked : null;
    }
}
