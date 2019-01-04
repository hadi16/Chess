package chess;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ChessState {
    private Map<Point, Piece> board = new HashMap<>();
    private int currentTurn;

    private Point clickedPoint;
    private Point hoverPoint;

    public void setDefaultState() {
        board = new HashMap<>();

        // Player 0's pieces are set.
        for (int i = 0; i < Helpers.BOARD_WIDTH; i++) {
            board.put(new Point(i, 1), new Piece(PieceType.PAWN, 0));
        }
        board.put(new Point(0, 0), new Piece(PieceType.ROOK, 0));
        board.put(new Point(7, 0), new Piece(PieceType.ROOK, 0));
        board.put(new Point(1, 0), new Piece(PieceType.KNIGHT, 0));
        board.put(new Point(6, 0), new Piece(PieceType.KNIGHT, 0));
        board.put(new Point(2, 0), new Piece(PieceType.BISHOP, 0));
        board.put(new Point(5, 0), new Piece(PieceType.BISHOP, 0));
        board.put(new Point(3, 0), new Piece(PieceType.QUEEN, 0));
        board.put(new Point(4, 0), new Piece(PieceType.KING, 0));

        // Player 1's pieces are set.
        for (int i = 0; i < Helpers.BOARD_WIDTH; i++) {
            board.put(new Point(i, 6), new Piece(PieceType.PAWN, 1));
        }
        board.put(new Point(0, 7), new Piece(PieceType.ROOK, 1));
        board.put(new Point(7, 7), new Piece(PieceType.ROOK, 1));
        board.put(new Point(1, 7), new Piece(PieceType.KNIGHT, 1));
        board.put(new Point(6, 7), new Piece(PieceType.KNIGHT, 1));
        board.put(new Point(2, 7), new Piece(PieceType.BISHOP, 1));
        board.put(new Point(5, 7), new Piece(PieceType.BISHOP, 1));
        board.put(new Point(3, 7), new Piece(PieceType.QUEEN, 1));
        board.put(new Point(4, 7), new Piece(PieceType.KING, 1));

        clickedPoint = null;
        currentTurn = 1;
    }

    public Map<Point, Piece> getBoard() {
        return board;
    }

    public boolean positionInBoard(Point position) {
        return board.containsKey(position);
    }

    public void setBoardAtPosition(Point position, Piece piece) {
        board.put(position, piece);
    }

    public Piece removeAndReturnPieceAtPosition(Point position) {
        return board.remove(position);
    }

    public boolean isMyTurn(int player) {
        return currentTurn == player;
    }

    public void changeTurn() {
        currentTurn = 1 - currentTurn;
    }

    public Point getClickedPoint() {
        if (clickedPoint == null) {
            return null;
        }

        return new Point(clickedPoint.x, clickedPoint.y);
    }

    public void setClickedPoint(Point clickedPoint) {
        if (clickedPoint == null) {
            this.clickedPoint = null;
            return;
        }

        this.clickedPoint = new Point(clickedPoint.x, clickedPoint.y);
    }

    public Point getHoverPoint() {
        if (hoverPoint == null) {
            return null;
        }

        return new Point(hoverPoint.x, hoverPoint.y);
    }

    public void setHoverPoint(Point hoverPoint) {
        if (hoverPoint == null) {
            this.hoverPoint = null;
            return;
        }

        this.hoverPoint = new Point(hoverPoint.x, hoverPoint.y);
    }
}