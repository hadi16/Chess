import java.awt.*;

/**
 * This program allows a user to move pieces around on a chess board. It
 * restricts moves to only legal chess moves and highlights these moves when the
 * user clicks on a piece. All rules of chess are implemented, including
 * castling and pawn promotion.
 * 
 * This class holds the commands to draw the board.
 * 
 * @author Alex Hadi
 * @version May 5, 2017
 */
public class Board {
	// These variables hold values that pertain to different characteristics of
	// the board.
	private Color firstColor;
	private Color secondColor;
	private int scaleDim;

	/**
	 * The constructor initializes each of the variables based on what is passed
	 * into it from ChessSetState.
	 */
	public Board(Color firstColor, Color secondColor, int scaleDim) {
		this.firstColor = firstColor;
		this.secondColor = secondColor;
		this.scaleDim = scaleDim;
	}

	/**
	 * When executed, this method draws the board.
	 */
	public void drawChessBoard(Graphics canvas) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				// If statement determines the color of the square.
				if (i % 2 == 0) {
					if (j % 2 == 0) {
						canvas.setColor(firstColor);
					} else {
						canvas.setColor(secondColor);
					}
				} else {
					if (j % 2 == 0) {
						canvas.setColor(secondColor);
					} else {
						canvas.setColor(firstColor);
					}
				}

				// Square is printed to the screen.
				canvas.fillRect(i * scaleDim, j * scaleDim, scaleDim, scaleDim);
			}
		}
	}

	public void drawGoldSquare(Graphics canvas, int xPos, int yPos) {
		// Gold squares are painted to highlight pieces that are clicked.
		Color gold = new Color(255, 215, 0);
		canvas.setColor(gold);
		canvas.fillRect(xPos * scaleDim, yPos * scaleDim, scaleDim, scaleDim);
	}

	public void drawGreenSquare(Graphics canvas, int xPos, int yPos) {
		// Squares are drawn over in green to show the user where they can
		// legally move.
		canvas.setColor(Color.green);
		canvas.fillRect(xPos * scaleDim + (scaleDim / 32), yPos * scaleDim + (scaleDim / 32), 15 * scaleDim / 16,
				15 * scaleDim / 16);
	}
}