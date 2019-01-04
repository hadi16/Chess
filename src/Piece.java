import java.awt.*;

public class Piece {
    private Point position;
    private int player;
    private PieceType pieceType;
    private boolean moved = false;

    public Piece(PieceType pieceType, Point position, int player, int scaleDim) {
        this.pieceType = pieceType;
        this.position = position;
        this.player = player;
    }

    public void drawPiece(Graphics canvas, int scaleDim) {
        canvas.setColor(player == 0 ? Color.black : Color.white);

        canvas.setFont(new Font("Serif", Font.PLAIN, scaleDim));
        canvas.drawString(pieceType.getUnicodeValue(), position.x, position.y + 7 * scaleDim / 8);
    }

    public void drawHoverText(Graphics canvas, int scaleDim) {
        canvas.setColor(player == 0 ? Color.white : Color.black);

        canvas.setFont(new Font("Arial", Font.BOLD, (scaleDim / 5)));
        canvas.drawString(getPieceType().toString(), position.x + scaleDim / 6, position.y + (3 * scaleDim / 4));
    }

    public void setPosition(Point position) {
        this.position = position;
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