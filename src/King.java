import java.awt.*;
/**
 * This program allows a user to move pieces around on a chess board. It
 * restricts moves to only legal chess moves and highlights these moves when the
 * user clicks on a piece. All rules of chess are implemented, including
 * castling and pawn promotion.
 * 
 * This class holds the commands to draw kings.
 * 
 * @author Alex Hadi
 * @version May 5, 2017
 */
public class King extends Piece {
	/**
	 * The constructor initializes each of the variables by calling super.
	 */
	public King(int xPos, int yPos, int player, int scaleDim) {
		super(xPos, yPos, player, scaleDim);
	}

	/**
	 * When executed, this method draws the king based on the value of
	 * pieceHeight.
	 */
	public void drawPiece(Graphics canvas, int scaleDim) {
		super.drawPiece(canvas, scaleDim);
	}
	
	public void drawHoverText(Graphics canvas, int scaleDim) {
		super.drawHoverText(canvas, scaleDim);
	}

	// Getters and setters below.
	public String getPieceName() {
		return "KING";
	}

	public int getPieceIdentifier() {
		return 6;
	}

	public String getPieceUnicodeValue() {
		return "\u2654";
	}
}