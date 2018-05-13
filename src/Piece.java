import java.awt.*;

/**
 * This program allows a user to move pieces around on a chess board. It
 * restricts moves to only legal chess moves and highlights these moves when the
 * user clicks on a piece. All rules of chess are implemented, including
 * castling and pawn promotion.
 * 
 * This class holds the general info for pieces to be passed to other classes.
 * 
 * @author Alex Hadi
 * @version May 5, 2017
 */
public class Piece {
	// Private variables store information about chess pieces.
	private int xPos;
	private int yPos;
	private int player;

	/**
	 * The constructor initializes each variable based on what is passed in from
	 * ChessSetState.
	 */
	public Piece(int xPos, int yPos, int player, int scaleDim) {
		this.xPos = xPos * scaleDim;
		this.yPos = yPos * scaleDim;
		this.player = player;
	}

	/**
	 * Empty drawPiece method is inherited from the other classes.
	 */
	public void drawPiece(Graphics canvas, int scaleDim) {
		if (player == 0) {
			canvas.setColor(Color.black);
		} else {
			canvas.setColor(Color.white);
		}
		canvas.setFont(new Font("Serif", Font.PLAIN, scaleDim));
		canvas.drawString(getPieceUnicodeValue(), xPos, yPos + 7 * scaleDim / 8);
	}

	public void drawHoverText(Graphics canvas, int scaleDim) {
		if (player == 0) {
			canvas.setColor(Color.white);
		} else {
			canvas.setColor(Color.black);
		}
		canvas.setFont(new Font("Arial", Font.BOLD, (scaleDim / 5)));
		canvas.drawString(getPieceName(), xPos + scaleDim / 6, yPos + (3 * scaleDim / 4));
	}

	// These getters and setters are used to access and set the private
	// variables stored in the Piece class.
	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

	public void setXPos(int xPos) {
		this.xPos = xPos;
	}

	public void setYPos(int yPos) {
		this.yPos = yPos;
	}

	public int getPlayer() {
		return player;
	}

	public String getPieceName() {
		return null;
	}

	public int getPieceIdentifier() {
		return -1;
	}

	public String getPieceUnicodeValue() {
		return null;
	}
}