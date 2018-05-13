import javax.swing.*;
/**
 * This program allows a user to move pieces around on a chess board. It
 * restricts moves to only legal chess moves and highlights these moves when the
 * user clicks on a piece. All rules of chess are implemented, including
 * castling and pawn promotion.
 * 
 * This class holds the score board and updates the score.
 * 
 * @author Alex Hadi
 * @version May 5, 2017
 */
public class ScoreBoard extends JPanel {
	private JTextArea scoreText = new JTextArea(50, 7);

	// For Player 0
	private int numPawn0;
	private int numRook0;
	private int numKnight0;
	private int numBishop0;
	private int numQueen0;
	private int numKing0;

	// For Player 1
	private int numPawn1;
	private int numRook1;
	private int numKnight1;
	private int numBishop1;
	private int numQueen1;
	private int numKing1;

	/**
	 * This constructor adds the JTextArea to the JPanel. It also prevents the
	 * user from being able to edit the JTextArea.
	 */
	public ScoreBoard() {
		scoreText.setEditable(false);
		this.add(scoreText);
	}

	/**
	 * This method updates the score for each player based on how many pieces
	 * they have.
	 */
	public void updateScore(Piece[][] board, int turnCounter) {
		// Every time method is run, variables are reset to 0.
		numPawn0 = 0;
		numRook0 = 0;
		numKnight0 = 0;
		numBishop0 = 0;
		numQueen0 = 0;
		numKing0 = 0;

		numPawn1 = 0;
		numRook1 = 0;
		numKnight1 = 0;
		numBishop1 = 0;
		numQueen1 = 0;
		numKing1 = 0;

		// This for loop goes through the entire board and increments the value
		// of a variable if it finds a variant of that piece.
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] != null) {
					if (board[i][j].getPlayer() == 0) {
						if (board[i][j] instanceof Pawn) {
							numPawn0++;
						} else if (board[i][j] instanceof Rook) {
							numRook0++;
						} else if (board[i][j] instanceof Knight) {
							numKnight0++;
						} else if (board[i][j] instanceof Bishop) {
							numBishop0++;
						} else if (board[i][j] instanceof Queen) {
							numQueen0++;
						} else if (board[i][j] instanceof King) {
							numKing0++;
						}
					} else if (board[i][j].getPlayer() == 1) {
						if (board[i][j] instanceof Pawn) {
							numPawn1++;
						} else if (board[i][j] instanceof Rook) {
							numRook1++;
						} else if (board[i][j] instanceof Knight) {
							numKnight1++;
						} else if (board[i][j] instanceof Bishop) {
							numBishop1++;
						} else if (board[i][j] instanceof Queen) {
							numQueen1++;
						} else if (board[i][j] instanceof King) {
							numKing1++;
						}
					}
				}
			}
		}

		// King is not included in score because game would be over at that
		// point.
		int score0 = (8 - numPawn1) * 1 + (2 - numRook1) * 5 + (2 - numKnight1) * 3 + (2 - numBishop1) * 3
				+ (1 - numQueen1) * 9;
		int score1 = (8 - numPawn0) * 1 + (2 - numRook0) * 5 + (2 - numKnight0) * 3 + (2 - numBishop0) * 3
				+ (1 - numQueen0) * 9;

		// The score cannot be negative. This can happen if a pawn has been
		// promoted.
		if (score0 < 0) {
			score0 = 0;
		}
		if (score1 < 0) {
			score1 = 0;
		}

		// The text is set with this code.
		scoreText.setText("Turn:\nPlayer " + turnCounter);
		scoreText.append("\n\n\n\n\n\nScores:\n\nPlayer 0\nScore [" + score0 + "]\nPawn: " + numPawn0 + "\nRook: "
				+ numRook0 + "\nKnight: " + numKnight0 + "\nBishop: " + numBishop0 + "\nQueen: " + numQueen0
				+ "\nKing: " + numKing0);
		scoreText.append(
				"\n\n\n\n\n\nPlayer 1\nScore [" + score1 + "]\nPawn: " + numPawn1 + "\nRook: " + numRook1 + "\nKnight: "
						+ numKnight1 + "\nBishop: " + numBishop1 + "\nQueen: " + numQueen1 + "\nKing: " + numKing1);
	}
}