import javax.swing.*;

/**
 * This program allows a user to move pieces around on a chess board. It
 * restricts moves to only legal chess moves and highlights these moves when the
 * user clicks on a piece. All rules of chess are implemented, including
 * castling and pawn promotion.
 * 
 * This class restricts moves to only legal chess ones. Moreover, the castling
 * and pawn promotion functionality is implemented in this class.
 * 
 * @author Alex Hadi
 * @version May 5, 2017
 */
public class Rules extends JPanel {
	// Stores information about whether rooks and kings have moved for castling
	// functionality.
	private boolean leftRookMoved0;
	private boolean rightRookMoved0;
	private boolean kingMoved0;
	private boolean leftRookMoved1;
	private boolean rightRookMoved1;
	private boolean kingMoved1;

	public boolean isChecked(int checkedPlayer, Piece[][] board) {
		for (int startX = 0; startX < 8; startX++) {
			for (int startY = 0; startY < 8; startY++) {
				if (board[startX][startY].getPlayer() == checkedPlayer) {
					for (int endX = 0; endX < 8; endX++) {
						for (int endY = 0; endY < 8; endY++) {
							if (isFriendlyFire(startX, startY, endX, endY, board) == false
									&& isLegalMove(startX, startY, endX, endY, board) == true) {
								if (board[endX][endY] instanceof King) {
									return true;
								} else {
									return false;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * This method determines if a move is illegal in that it results in taking
	 * one's own piece.
	 */
	public boolean isFriendlyFire(int startX, int startY, int endX, int endY, Piece[][] board) {
		// If the ending square is null, the move doesn't constitute friendly
		// fire.
		if (board[endX][endY] == null) {
			return false;
		} else if (board[startX][startY].getPlayer() == board[endX][endY].getPlayer()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method determines if the move is a legal chess move.
	 */
	public boolean isLegalMove(int startX, int startY, int endX, int endY, Piece[][] board) {
		// If any of the coordinates are -1, the move is invalid.
		if (startX == -1 || startY == -1 || endX == -1 || endY == -1) {
			return false;
		} else if (board[startX][startY] instanceof Pawn) {
			if (board[startX][startY].getPlayer() == 0) {
				// Pawns cannot move backwards.
				if (startY > endY) {
					return false;
				}
				// Pawns can move forwards onto squares that are unoccupied.
				else if (startX == endX && board[endX][endY] == null) {
					// Pawns can move forwards two spaces on the first turn as
					// long as the squares are unoccupied.
					if (startY == 1 && board[endX][startY + 1] == null && endY - startY == 2) {
						return true;
					}
					// Otherwise pawns can only move forwards one space.
					else if (endY - startY == 1) {
						return true;
					} else {
						return false;
					}
				}
				// Pawns can capture enemy pieces diagonally.
				else if (board[endX][endY] != null) {
					if (endX - startX == 1 && endY - startY == 1) {
						return true;
					} else if (endX - startX == -1 && endY - startY == 1) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else if (board[startX][startY].getPlayer() == 1) {
				// Pawns cannot move backwards.
				if (startY < endY) {
					return false;
				}
				// Pawns can move forwards onto squares that are unoccupied.
				else if (startX == endX && board[endX][endY] == null) {
					// Pawns can move forwards two spaces on the first turn as
					// long as the squares are unoccupied.
					if (startY == 6 && board[endX][startY - 1] == null && endY - startY == -2) {
						return true;
					}
					// Otherwise pawns can only move forwards one space.
					else if (endY - startY == -1) {
						return true;
					} else {
						return false;
					}
				}
				// Pawns can capture enemy pieces diagonally.
				else if (board[endX][endY] != null) {
					if (endX - startX == -1 && endY - startY == -1) {
						return true;
					} else if (endX - startX == 1 && endY - startY == -1) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else if (board[startX][startY] instanceof Rook) {
			// Rooks can only move horizontally or vertically.
			if (startX == endX || startY == endY) {
				// These statements check when the rook moves horizontally.
				if (startX != endX) {
					// These statements check when the rook moves from right to
					// left.
					if (startX > endX) {
						for (int i = 1; i < (startX - endX); i++) {
							if (board[endX + i][endY] != null) {
								return false;
							}
						}
						return true;
					}
					// These statements check when the rook moves from left to
					// right.
					else {
						for (int i = 1; i < (endX - startX); i++) {
							if (board[startX + i][endY] != null) {
								return false;
							}
						}
						return true;
					}
				}
				// These statements check when the rook moves vertically.
				else {
					// These statements check when the rook moves upwards.
					if (startY > endY) {
						for (int i = 1; i < (startY - endY); i++) {
							if (board[endX][endY + i] != null) {
								return false;
							}
						}
						return true;
					}
					// These statement check when the rook moves downwards.
					else {
						for (int i = 1; i < (endY - startY); i++) {
							if (board[endX][startY + i] != null) {
								return false;
							}
						}
						return true;
					}
				}
			} else {
				return false;
			}
		} else if (board[startX][startY] instanceof Knight) {
			// These statements check for the only eight permitted L-shaped
			// moves that a Knight can make in a given turn.
			// Since a Knight can jump over other pieces, the method doesn't
			// check for this.
			if (startX + 2 == endX && startY + 1 == endY) {
				return true;
			} else if (startX - 2 == endX && startY - 1 == endY) {
				return true;
			} else if (startX + 2 == endX && startY - 1 == endY) {
				return true;
			} else if (startX - 2 == endX && startY + 1 == endY) {
				return true;
			} else if (startY + 2 == endY && startX + 1 == endX) {
				return true;
			} else if (startY - 2 == endY && startX - 1 == endX) {
				return true;
			} else if (startY + 2 == endY && startX - 1 == endX) {
				return true;
			} else if (startY - 2 == endY && startX + 1 == endX) {
				return true;
			} else {
				return false;
			}
		} else if (board[startX][startY] instanceof Bishop) {
			// A bishop can only move diagonally. This means that the distance
			// from start x and end x must equal the distance from start y and
			// end y.
			if (Math.abs(endX - startX) == Math.abs(endY - startY)) {
				// These statements check for any pieces on the applicable
				// squares that would prevent bishop movement. A bishop cannot
				// jump over other pieces.
				if (startX > endX && startY > endY) {
					for (int i = 1; i < (startX - endX); i++) {
						if (board[endX + i][endY + i] != null) {
							return false;
						}
					}
					return true;
				} else if (startX < endX && startY < endY) {
					for (int i = 1; i < (endX - startX); i++) {
						if (board[startX + i][startY + i] != null) {
							return false;
						}
					}
					return true;
				} else if (startX > endX && startY < endY) {
					for (int i = 1; i < (startX - endX); i++) {
						if (board[endX + i][endY - i] != null) {
							return false;
						}
					}
					return true;
				} else if (startX < endX && startY > endY) {
					for (int i = 1; i < (endX - startX); i++) {
						if (board[startX + i][startY - i] != null) {
							return false;
						}
					}
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else if (board[startX][startY] instanceof Queen) {
			// A Queen can move diagonally, horizontally, or vertically anywhere
			// on the board as long as it doesn't jump over any other pieces.
			// The rules for a bishop and rook are combined to form the rules
			// for a queen.
			if (startX == endX || startY == endY) {
				if (startX != endX) {
					if (startX > endX) {
						for (int i = 1; i < (startX - endX); i++) {
							if (board[endX + i][endY] != null) {
								return false;
							}
						}
						return true;
					} else {
						for (int i = 1; i < (endX - startX); i++) {
							if (board[startX + i][endY] != null) {
								return false;
							}
						}
						return true;
					}
				} else {
					if (startY > endY) {
						for (int i = 1; i < (startY - endY); i++) {
							if (board[endX][endY + i] != null) {
								return false;
							}
						}
						return true;
					} else {
						for (int i = 1; i < (endY - startY); i++) {
							if (board[endX][startY + i] != null) {
								return false;
							}
						}
						return true;
					}
				}
			} else if (Math.abs(endX - startX) == Math.abs(endY - startY)) {
				if (startX > endX && startY > endY) {
					for (int i = 1; i < (startX - endX); i++) {
						if (board[endX + i][endY + i] != null) {
							return false;
						}
					}
					return true;
				} else if (startX < endX && startY < endY) {
					for (int i = 1; i < (endX - startX); i++) {
						if (board[startX + i][startY + i] != null) {
							return false;
						}
					}
					return true;
				} else if (startX > endX && startY < endY) {
					for (int i = 1; i < (startX - endX); i++) {
						if (board[endX + i][endY - i] != null) {
							return false;
						}
					}
					return true;
				} else if (startX < endX && startY > endY) {
					for (int i = 1; i < (endX - startX); i++) {
						if (board[startX + i][startY - i] != null) {
							return false;
						}
					}
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else if (board[startX][startY] instanceof King) {
			// A King can move in one square horizontally, vertically, or
			// diagonally.
			if (startX - 1 == endX && startY == endY) {
				return true;
			} else if (startX + 1 == endX && startY == endY) {
				return true;
			} else if (startY - 1 == endY && startX == endX) {
				return true;
			} else if (startY + 1 == endY && startX == endX) {
				return true;
			} else if (startX + 1 == endX && startY + 1 == endY) {
				return true;
			} else if (startX + 1 == endX && startY - 1 == endY) {
				return true;
			} else if (startX - 1 == endX && startY + 1 == endY) {
				return true;
			} else if (startX - 1 == endX && startY - 1 == endY) {
				return true;
			}
			// These statements allow the king to castle. It checks to make sure
			// that the king and rook involved have not moved in order to allow
			// for this.
			else if (board[startX][startY].getPlayer() == 0) {
				if (endX == 2 && endY == 0 && kingMoved0 == false && leftRookMoved0 == false) {
					if (board[1][0] == null && board[2][0] == null && board[3][0] == null) {
						return true;
					} else {
						return false;
					}
				} else if (endX == 6 && endY == 0 && kingMoved0 == false && rightRookMoved0 == false) {
					if (board[5][0] == null && board[6][0] == null) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else if (board[startX][startY].getPlayer() == 1) {
				if (endX == 2 && endY == 7 && kingMoved1 == false && leftRookMoved1 == false) {
					if (board[1][7] == null && board[2][7] == null && board[3][7] == null) {
						return true;
					} else {
						return false;
					}
				} else if (endX == 6 && endY == 7 && kingMoved1 == false && rightRookMoved1 == false) {
					if (board[5][7] == null && board[6][7] == null) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public void castle(int endX, int endY, Piece[][] board, int scaleDim) {
		if (board[endX][endY].getPlayer() == 0) {
			if (endX == 2) {
				board[3][0] = board[0][0];
				board[3][0].setXPos(3 * scaleDim);
				board[3][0].setYPos(0 * scaleDim);
				board[0][0] = null;
				leftRookMoved0 = true;
			} else if (endX == 6) {
				board[5][0] = board[7][0];
				board[5][0].setXPos(5 * scaleDim);
				board[5][0].setYPos(0 * scaleDim);
				board[7][0] = null;
				rightRookMoved0 = true;
			}
		} else if (board[endX][endY].getPlayer() == 1) {
			if (endX == 2) {
				board[3][7] = board[0][7];
				board[3][7].setXPos(3 * scaleDim);
				board[3][7].setYPos(7 * scaleDim);
				board[0][7] = null;
				leftRookMoved1 = true;
			} else if (endX == 6) {
				board[5][7] = board[7][7];
				board[5][7].setXPos(5 * scaleDim);
				board[5][7].setYPos(7 * scaleDim);
				board[7][7] = null;
				rightRookMoved1 = true;
			}
		}
	}

	/**
	 * This method is executed when the pawn can be promoted by the user. This
	 * happens when the pawn reaches the other side of the board.
	 */
	public void pawnPromotion(int xPos, int yPos, int player, int scaleDim, Piece[][] board) {
		// pieceList holds possible options user can select when promoting.
		String[] pieceList = { "Rook", "Knight", "Bishop", "Queen" };
		JFrame pawnPromotionFrame = new JFrame("Pawn Promotion");
		String option = null;

		// While loop runs until the user selects an option from the dialog box.
		while (true) {
			if (option == null) {
				option = (String) JOptionPane.showInputDialog(pawnPromotionFrame,
						"You can promote your pawn. Please select from the following options.", "Pawn Promotion",
						JOptionPane.QUESTION_MESSAGE, null, pieceList, pieceList[0]);
			} else {
				break;
			}
		}

		// Switch statement instantiates kind of piece that the user selected.
		switch (option) {
		case "Rook":
			board[xPos][yPos] = new Rook(xPos, yPos, player, scaleDim);
			break;

		case "Knight":
			board[xPos][yPos] = new Knight(xPos, yPos, player, scaleDim);
			break;

		case "Bishop":
			board[xPos][yPos] = new Bishop(xPos, yPos, player, scaleDim);
			break;

		case "Queen":
			board[xPos][yPos] = new Queen(xPos, yPos, player, scaleDim);
			break;
		}
	}

	public void setLeftRookMoved0(boolean leftRookMoved0) {
		this.leftRookMoved0 = leftRookMoved0;
	}

	public void setRightRookMoved0(boolean rightRookMoved0) {
		this.rightRookMoved0 = rightRookMoved0;
	}

	public void setKingMoved0(boolean kingMoved0) {
		this.kingMoved0 = kingMoved0;
	}

	public void setLeftRookMoved1(boolean leftRookMoved1) {
		this.leftRookMoved1 = leftRookMoved1;
	}

	public void setRightRookMoved1(boolean rightRookMoved1) {
		this.rightRookMoved1 = rightRookMoved1;
	}

	public void setKingMoved1(boolean kingMoved1) {
		this.kingMoved1 = kingMoved1;
	}

	public boolean getLeftRookMoved0() {
		return leftRookMoved0;
	}

	public boolean getRightRookMoved0() {
		return rightRookMoved0;
	}

	public boolean getKingMoved0() {
		return kingMoved0;
	}

	public boolean getLeftRookMoved1() {
		return leftRookMoved1;
	}

	public boolean getRightRookMoved1() {
		return rightRookMoved1;
	}

	public boolean getKingMoved1() {
		return kingMoved1;
	}
}