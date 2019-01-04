package chess;

public class Piece {
    private PieceType pieceType;
    private int player;
    private boolean moved = false;

    public Piece(PieceType pieceType, int player) {
        this.pieceType = pieceType;
        this.player = player;
    }

    public int getPlayer() {
        return player;
    }

    public PieceType getPieceType() {
        return pieceType;
    }
    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
    }

    public boolean hasMoved() {
        return moved;
    }
    public void setMoved(boolean moved) {
        this.moved = moved;
    }
}