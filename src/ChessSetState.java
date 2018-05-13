import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

/**
 * This program allows a user to move pieces around on a chess board. It
 * restricts moves to only legal chess moves and highlights these moves when the
 * user clicks on a piece. All rules of chess are implemented, including
 * castling and pawn promotion.
 * 
 * This class holds the current state of the chess board and information about
 * the game. It also holds the save, open, and new game functionality.
 * 
 * @author Alex Hadi
 * @version May 5, 2017
 */
public class ChessSetState extends JPanel
		implements ActionListener, MouseListener, MouseMotionListener, ComponentListener {

	private static JFrame mainFrame;

	// Dimension of each square on the chess board.
	private int scaleDim;

	// Array stores values of chess pieces.
	private Piece board[][] = new Piece[8][8];

	// Work with MouseMotionListener to show name of piece when user hovers over
	// it.
	private int xHover;
	private int yHover;

	// Shows whether piece should be highlighted or not.
	private boolean paintGold;

	// Stores x and y coordinate for MouseListener to highlight squares.
	private int xClicked;
	private int yClicked;
	private int prevXClicked;
	private int prevYClicked;

	// Counters determine whether to move piece and whose turn it is.
	private int clickCounter;
	private int turnCounter;

	private AlertBoard alerts = new AlertBoard();
	private ScoreBoard score = new ScoreBoard();
	private Rules chessRules = new Rules();

	/**
	 * This is the main method, where the program is executed from.
	 */
	public static void main(String[] args) {
		// These commands set up the JFrame size and behavior.
		mainFrame = new JFrame("Chess Game");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(1000, 1000);

		ChessSetState theGame = new ChessSetState();
		theGame.setGameSize();

		// game instance is passed into Buttons.
		Buttons gameButtons = new Buttons(theGame);

		mainFrame.setLayout(new BorderLayout());

		mainFrame.add(theGame, BorderLayout.CENTER);
		mainFrame.add(gameButtons, BorderLayout.SOUTH);
		mainFrame.add(theGame.alerts, BorderLayout.NORTH);
		mainFrame.add(theGame.score, BorderLayout.EAST);

		theGame.resetBoard();

		// Displays the canvas for the user.
		mainFrame.setVisible(true);
	}

	/**
	 * The constructor sets scaleDim and adds the mouse listeners.
	 */
	public ChessSetState() {
		mainFrame.addComponentListener(this);

		// Mouse listeners are added.
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	public void setGameSize() {
		// Finds shorter dimension.
		int height = mainFrame.getBounds().height;
		int width = mainFrame.getBounds().width;
		int frameDim;
		if (height < width) {
			frameDim = height;
		} else {
			frameDim = width;
		}

		// Resets scaleDim and repaints.
		scaleDim = (int) (frameDim / 10);
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] != null) {
					board[i][j].setXPos(i * scaleDim);
					board[i][j].setYPos(j * scaleDim);

				}
			}
		}
		repaint();
	}

	public void componentResized(ComponentEvent c) {
		setGameSize();
	}

	public void componentShown(ComponentEvent c) {
	}

	public void componentMoved(ComponentEvent c) {
	}

	public void componentHidden(ComponentEvent c) {
	}

	/**
	 * This method is executed when the mouse moves over the chess board and
	 * allows the name of the piece to be displayed on each piece.
	 */
	public void mouseMoved(MouseEvent hover) {
		// Temporary variable is created and then checked to make sure it is not
		// out of bounds in the array.
		int tempXHover = (int) (hover.getX() / scaleDim);
		int tempYHover = (int) (hover.getY() / scaleDim);
		if (tempXHover > 7 || tempXHover < 0 || tempYHover > 7 || tempYHover < 0) {
			// If out of bound, variables are set to -1 to overwrite the
			// previous value of xHover and yHover.
			xHover = -1;
			yHover = -1;
			repaint();
		} else {
			xHover = tempXHover;
			yHover = tempYHover;
		}

		// Nothing is drawn if xHover or yHover equal -1 or if the square is
		// null.
		if (xHover == -1 || yHover == -1) {
		} else if (board[xHover][yHover] != null) {
			repaint();
		} else if (board[xHover][yHover] == null) {
			xHover = -1;
			yHover = -1;
			repaint();
		}
	}

	/**
	 * This method is necessary for mouseMotionListener. It is not used by the
	 * program.
	 */
	public void mouseDragged(MouseEvent hover) {
	}

	/**
	 * This method is executed when a button is clicked. It stores the save,
	 * open, and new game functionality.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Save")) {
			// JFileChooser is utilized to allow the user to choose whichever
			// save location they prefer.
			JFrame saveDialog = new JFrame();
			saveDialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Please choose a save location and name.");

			File saveFile = null;
			// If user clicks "Save", the file path is updated accordingly.
			// Otherwise, the save operation is aborted.
			try {
				// JFileChooser.APPROVE_OPTION signifies that the user selected
				// a save location.
				if (fc.showSaveDialog(saveDialog) == JFileChooser.APPROVE_OPTION) {
					saveFile = fc.getSelectedFile();
					// CSV file extension is added to ensure file format remains
					// the same throughout all save files.
					String path = saveFile.getAbsolutePath();
					if (!path.endsWith(".csv")) {
						saveFile = new File(path + ".csv");
					}
					alerts.updateAlerts(4,
							"The file was successfully created in the directory " + saveFile.getAbsolutePath() + ".");
				} else {
					alerts.updateAlerts(4, "The save operation was terminated.");
				}
			} catch (HeadlessException he) {
				alerts.updateAlerts(4, "There was an error!");
			}

			// If save operation was aborted by user, this is not executed.
			if (saveFile != null) {
				FileWriter fw = null;
				try {
					// Essential values pertaining to the game state are saved
					// to the CSV file.
					fw = new FileWriter(saveFile);
					fw.write(scaleDim + "\r\n");
					fw.write(turnCounter + "\r\n");
					fw.write(chessRules.getLeftRookMoved0() + "\r\n");
					fw.write(chessRules.getRightRookMoved0() + "\r\n");
					fw.write(chessRules.getKingMoved0() + "\r\n");
					fw.write(chessRules.getLeftRookMoved1() + "\r\n");
					fw.write(chessRules.getRightRookMoved1() + "\r\n");
					fw.write(chessRules.getKingMoved1() + "\r\n");
					// Piece identifier, xPos, yPos, and player allow the game
					// to be restored correctly.
					for (int i = 0; i < board.length; i++) {
						for (int j = 0; j < board[i].length; j++) {
							if (board[i][j] != null) {
								fw.write(board[i][j].getPieceIdentifier() + "," + board[i][j].getXPos() / scaleDim + ","
										+ board[i][j].getYPos() / scaleDim + "," + board[i][j].getPlayer() + "\r\n");
							}
						}
					}
					alerts.updateAlerts(4, "The file was successfully saved with the current game state.");
				} catch (IOException ioe) {
					alerts.updateAlerts(4, "There was an error!");
				} finally {
					try {
						fw.close();
					} catch (IOException ioe) {
						alerts.updateAlerts(4, "There was an error!");
					}
				}
			}
		} else if (e.getActionCommand().equals("Open")) {
			// JFileChooser is utilized to allow the user to choose to open the
			// file from wherever they prefer.
			JFrame openDialog = new JFrame();
			openDialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Please choose a CSV file to open.");

			File saveFile = null;
			// If user selects a file, this is executed.
			if (fc.showOpenDialog(openDialog) == JFileChooser.APPROVE_OPTION) {
				File tempSaveFile = fc.getSelectedFile();
				String path = tempSaveFile.getAbsolutePath();
				// File is checked to ensure that it has a CSV extension.
				if (path.endsWith(".csv")) {
					saveFile = fc.getSelectedFile();
					alerts.updateAlerts(4,
							"The file was successfully opened in the directory " + saveFile.getAbsolutePath() + ".");
				} else {
					alerts.updateAlerts(4, "The file must have a .csv extension!");
				}
			} else {
				alerts.updateAlerts(4, "The open operation was terminated.");
			}

			if (saveFile != null) {
				// Each piece in the array is set to null.
				for (int i = 0; i < board.length; i++) {
					for (int j = 0; j < board[i].length; j++) {
						board[i][j] = null;
					}
				}

				// Counters are reset.
				clickCounter = 0;
				prevXClicked = -1;
				prevYClicked = -1;
				xClicked = -1;
				yClicked = -1;
				paintGold = false;

				// Game state is restored based on CSV file.
				try {
					// Delimiter uses commas and whitespace with scanner instead
					// of just whitespace.
					Scanner file = new Scanner(saveFile);
					file.useDelimiter("[,\\s]+");

					// Essential variables are restored.
					scaleDim = file.nextInt();
					file.nextLine();
					turnCounter = file.nextInt();
					file.nextLine();
					chessRules.setLeftRookMoved0(file.nextBoolean());
					file.nextLine();
					chessRules.setRightRookMoved0(file.nextBoolean());
					file.nextLine();
					chessRules.setKingMoved0(file.nextBoolean());
					file.nextLine();
					chessRules.setLeftRookMoved1(file.nextBoolean());
					file.nextLine();
					chessRules.setRightRookMoved1(file.nextBoolean());
					file.nextLine();
					chessRules.setKingMoved1(file.nextBoolean());
					file.nextLine();

					// While loop runs until the end of the file is reached.
					// Method readSaveState is executed for each piece.
					while (file.hasNextInt()) {
						int pieceIdentifier = file.nextInt();
						int xPos = file.nextInt();
						int yPos = file.nextInt();
						int player = file.nextInt();
						readSaveState(pieceIdentifier, xPos, yPos, player);
						file.nextLine();
					}

					// Scanner is closed.
					file.close();
				} catch (FileNotFoundException fnfe) {
					alerts.updateAlerts(4, "The file could not be found!");
				}

				// Score is updated.
				score.updateScore(getBoardCopy(), turnCounter);

				repaint();
				alerts.updateAlerts(4, "The game state was successfully restored!");
			}
		} else if (e.getActionCommand().equals("New Game")) {
			// resetBoard method restores the game.
			resetBoard();
			repaint();
		}
	}

	/**
	 * This class is necessary for mouseListener. It is not used by the program.
	 */
	public void mouseEntered(MouseEvent m) {
	}

	/**
	 * This class is necessary for mouseListener. It is not used by the program.
	 */
	public void mouseExited(MouseEvent m) {
	}

	/**
	 * This class is necessary for mouseListener. It is not used by the program.
	 */
	public void mouseReleased(MouseEvent m) {
	}

	/**
	 * This class is necessary for mouseListener. It is not used by the program.
	 */
	public void mousePressed(MouseEvent m) {
	}

	/**
	 * This method is executed when the mouse is clicked. It highlights the
	 * selected piece.
	 */
	public void mouseClicked(MouseEvent m) {
		// Variables xClicked and yClicked are set only if it is within the
		// bounds of the array.
		int tempXClicked = (int) (m.getX() / getScaleDim());
		int tempYClicked = (int) (m.getY() / getScaleDim());
		if (tempXClicked > 7 || tempXClicked < 0 || tempYClicked > 7 || tempYClicked < 0) {
			xClicked = -1;
			yClicked = -1;
			alerts.updateAlerts(4, null);
		} else {
			xClicked = tempXClicked;
			yClicked = tempYClicked;
		}

		// If xClicked or yClicked are -1, all variables related to highlighting
		// a piece are reset.
		if (xClicked == -1 || yClicked == -1) {
			prevXClicked = -1;
			prevYClicked = -1;
			xClicked = -1;
			yClicked = -1;
			paintGold = false;
			clickCounter = 0;
			repaint();
		}
		// If the same piece is selected twice, the square is deselected and
		// clickCounter is reset. The turnCounter remains unchanged.
		else if (prevXClicked == xClicked && prevYClicked == yClicked) {
			prevXClicked = -1;
			prevYClicked = -1;
			xClicked = -1;
			yClicked = -1;
			paintGold = false;
			clickCounter = 0;
			repaint();
			alerts.updateAlerts(4, null);
		}
		// This code highlights a piece if certain conditions are met.
		else if (clickCounter == 0) {
			// Checks to make sure piece is on square
			if (board[xClicked][yClicked] != null) {
				// These if statements check to make sure that the piece belongs
				// to the correct player and then highlights the piece. If so,
				// clickCounter is incremented.
				if ((turnCounter == 0 && board[xClicked][yClicked].getPlayer() == 0)) {
					paintGold = true;
					repaint();
					alerts.updateAlerts(3, Integer.toString(board[xClicked][yClicked].getPieceIdentifier()));
					clickCounter++;
					prevXClicked = xClicked;
					prevYClicked = yClicked;
				}
				if (turnCounter == 1 && board[xClicked][yClicked].getPlayer() == 1) {
					paintGold = true;
					repaint();
					alerts.updateAlerts(3, Integer.toString(board[xClicked][yClicked].getPieceIdentifier()));
					clickCounter++;
					prevXClicked = xClicked;
					prevYClicked = yClicked;
				}
			}
		}
		// This if statement contains the code that determines whether to move
		// the piece.
		else if (clickCounter == 1) {
			// If the user is not attempting to take their own piece and it is a
			// legal chess move, the piece is moved.
			if (chessRules.isFriendlyFire(prevXClicked, prevYClicked, xClicked, yClicked, getBoardCopy()) == false
					&& chessRules.isLegalMove(prevXClicked, prevYClicked, xClicked, yClicked, getBoardCopy()) == true) {
				movePiece(prevXClicked, prevYClicked, xClicked, yClicked);
				prevXClicked = -1;
				prevYClicked = -1;
				xClicked = -1;
				yClicked = -1;
				paintGold = false;
				clickCounter--;
				repaint();
			}
			// If the user attempts to take their own piece, the piece is not
			// moved and a message is printed as an alert.
			else if (chessRules.isFriendlyFire(prevXClicked, prevYClicked, xClicked, yClicked,
					getBoardCopy()) == true) {
				prevXClicked = -1;
				prevYClicked = -1;
				xClicked = -1;
				yClicked = -1;
				paintGold = false;
				clickCounter--;
				repaint();
				alerts.updateAlerts(1, null);
			}
			// If the user attempts to make an illegal chess move, the piece is
			// not moved and a message is printed as an alert.
			else {
				prevXClicked = -1;
				prevYClicked = -1;
				xClicked = -1;
				yClicked = -1;
				paintGold = false;
				clickCounter--;
				repaint();
				alerts.updateAlerts(2, null);
			}
		}
	}

	/**
	 * This method resets the board to its initial state.
	 */
	public void resetBoard() {
		// Each position on the array is initially set to null.
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = null;
			}
		}

		// Player 0's pieces are set.
		for (int i = 0; i < board.length; i++) {
			board[i][1] = new Pawn(i, 1, 0, scaleDim);
		}
		board[0][0] = new Rook(0, 0, 0, scaleDim);
		board[7][0] = new Rook(7, 0, 0, scaleDim);
		board[1][0] = new Knight(1, 0, 0, scaleDim);
		board[6][0] = new Knight(6, 0, 0, scaleDim);
		board[2][0] = new Bishop(2, 0, 0, scaleDim);
		board[5][0] = new Bishop(5, 0, 0, scaleDim);
		board[3][0] = new Queen(3, 0, 0, scaleDim);
		board[4][0] = new King(4, 0, 0, scaleDim);

		// Player 1's pieces are set.
		for (int i = 0; i < board.length; i++) {
			board[i][6] = new Pawn(i, 6, 1, scaleDim);
		}
		board[0][7] = new Rook(0, 7, 1, scaleDim);
		board[7][7] = new Rook(7, 7, 1, scaleDim);
		board[1][7] = new Knight(1, 7, 1, scaleDim);
		board[6][7] = new Knight(6, 7, 1, scaleDim);
		board[2][7] = new Bishop(2, 7, 1, scaleDim);
		board[5][7] = new Bishop(5, 7, 1, scaleDim);
		board[3][7] = new Queen(3, 7, 1, scaleDim);
		board[4][7] = new King(4, 7, 1, scaleDim);

		// Booleans that allow for castling functionality are reset.
		chessRules.setLeftRookMoved0(false);
		chessRules.setRightRookMoved0(false);
		chessRules.setKingMoved0(false);
		chessRules.setLeftRookMoved1(false);
		chessRules.setRightRookMoved1(false);
		chessRules.setKingMoved1(false);

		// Other essential variables are reset.
		clickCounter = 0;
		prevXClicked = -1;
		prevYClicked = -1;
		xClicked = -1;
		yClicked = -1;
		turnCounter = 1;
		paintGold = false;

		alerts.updateAlerts(0, null);
		score.updateScore(getBoardCopy(), turnCounter);
	}

	/**
	 * This is the paint method, which prints everything onto the canvas.
	 */
	public void paint(Graphics canvas) {
		// This is essential for the proper painting of the board.
		super.paint(canvas);

		// The board is drawn.
		Board theBoard = new Board(new Color(255, 205, 159), new Color(210, 138, 71), scaleDim);
		theBoard.drawChessBoard(canvas);
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (paintGold == true && xClicked != -1 && yClicked != -1) {
					theBoard.drawGoldSquare(canvas, xClicked, yClicked);
				}

				if (chessRules.isLegalMove(xClicked, yClicked, i, j, getBoardCopy()) == true
						&& chessRules.isFriendlyFire(xClicked, yClicked, i, j, getBoardCopy()) == false
						&& paintGold == true) {
					theBoard.drawGreenSquare(canvas, i, j);
				}
			}
		}

		// The pieces are printed to the screen based on the value of each space
		// in the array.
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] != null) {
					board[i][j].drawPiece(canvas, scaleDim);
				}
			}
		}

		if (xHover != -1 && yHover != -1) {
			board[xHover][yHover].drawHoverText(canvas, scaleDim);
		}
	}

	/**
	 * This method instantiates each piece based on what is passed in from the
	 * game state save file.
	 */
	public void readSaveState(int pieceIdentifier, int xPos, int yPos, int player) {
		// Based on the value of pieceIdentifier, xPos, yPos, and player, each
		// piece is made.
		if (pieceIdentifier == 1) {
			board[xPos][yPos] = new Pawn(xPos, yPos, player, scaleDim);
		} else if (pieceIdentifier == 2) {
			board[xPos][yPos] = new Rook(xPos, yPos, player, scaleDim);
		} else if (pieceIdentifier == 3) {
			board[xPos][yPos] = new Knight(xPos, yPos, player, scaleDim);
		} else if (pieceIdentifier == 4) {
			board[xPos][yPos] = new Bishop(xPos, yPos, player, scaleDim);
		} else if (pieceIdentifier == 5) {
			board[xPos][yPos] = new Queen(xPos, yPos, player, scaleDim);
		} else if (pieceIdentifier == 6) {
			board[xPos][yPos] = new King(xPos, yPos, player, scaleDim);
		}
	}

	public void movePiece(int startX, int startY, int endX, int endY) {
		// The piece is moved to the new position in the array. The value of
		// xPos and yPos are set accordingly. Old position in array is set to
		// null.
		board[endX][endY] = board[startX][startY];
		board[endX][endY].setXPos(endX * scaleDim);
		board[endX][endY].setYPos(endY * scaleDim);
		board[startX][startY] = null;

		// These if statements allow the rook to be moved when the king is moved
		// when castling.
		if (board[endX][endY] instanceof King && Math.abs(endX - startX) == 2) {
			chessRules.castle(endX, endY, getBoardCopy(), scaleDim);
		}

		// These if statements execute the pawnPromotion method when a pawn
		// reaches the other side of the board.
		if (board[endX][endY] instanceof Pawn) {
			if (board[endX][endY].getPlayer() == 0 && endY == 7) {
				chessRules.pawnPromotion(endX, endY, 0, scaleDim, board);
			} else if (board[endX][endY].getPlayer() == 1 && endY == 0) {
				chessRules.pawnPromotion(endX, endY, 1, scaleDim, board);
			}
		}

		// These if statements check to see if either rook or king moves. If
		// they move, the user cannot castle with those pieces.
		if (board[endX][endY] instanceof Rook) {
			if (board[endX][endY].getPlayer() == 0) {
				if (startX == 0 && startY == 0 && chessRules.getLeftRookMoved0() == false) {
					chessRules.setLeftRookMoved0(true);
				} else if (startX == 7 && startY == 0 && chessRules.getRightRookMoved0() == false) {
					chessRules.setRightRookMoved0(true);
				}
			} else if (board[endX][endY].getPlayer() == 1) {
				if (startX == 0 && startY == 7 && chessRules.getLeftRookMoved1() == false) {
					chessRules.setLeftRookMoved1(true);
				} else if (startX == 7 && startY == 7 && chessRules.getRightRookMoved1() == false) {
					chessRules.setRightRookMoved1(true);
				}
			}
		} else if (board[endX][endY] instanceof King) {
			if (board[endX][endY].getPlayer() == 0) {
				if (startX == 4 && startY == 0 && chessRules.getKingMoved0() == false) {
					chessRules.setKingMoved0(true);
				}
			} else if (board[endX][endY].getPlayer() == 1) {
				if (startX == 4 && startY == 7 && chessRules.getKingMoved1() == false) {
					chessRules.setKingMoved1(true);
				}
			}
		}

		// The turnCounter is incremented or decremented accordingly to allow
		// for proper turns.
		if (board[endX][endY].getPlayer() == 0) {
			turnCounter++;
		} else if (board[endX][endY].getPlayer() == 1) {
			turnCounter--;
		}

		// The score is updated.
		score.updateScore(getBoardCopy(), turnCounter);
		// The alerts are reset.
		alerts.updateAlerts(4, null);
	}

	// Getters and setters below.
	public int getScaleDim() {
		return scaleDim;
	}

	public Piece[][] getBoardCopy() {
		return Arrays.copyOf(board, board.length);
	}
}