import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ChessRules {
    private enum Direction {
        NORTH(0, -1),
        EAST(1, 0),
        SOUTH(0, 1),
        WEST(-1, 0),
        NORTHEAST(1, -1),
        NORTHWEST(-1, -1),
        SOUTHEAST(1, 1),
        SOUTHWEST(-1, 1);

        private int x;
        private int y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private boolean isFriendlyFire(Point start, Point end, Piece[][] board) {
        // If the ending square is null, the move doesn't constitute friendly fire.
        if (board[end.x][end.y] == null) {
            return false;
        }

        return board[start.x][start.y].getPlayer() == board[end.x][end.y].getPlayer();
    }

    private Direction getDirection(Point start, Point end) {
        int xDifference = end.x - start.x;
        int yDifference = end.y - start.y;

        int x = xDifference == 0 ? 0 : xDifference / Math.abs(xDifference);
        int y = yDifference == 0 ? 0 : yDifference / Math.abs(yDifference);

        for (Direction direction : Direction.values()) {
            if (direction.x == x && direction.y == y) {
                return direction;
            }
        }
        return null;
    }

    private boolean positionInBounds(Point point) {
        return point.x >= 0 && point.x <= Constants.BOARD_WIDTH - 1 &&
                point.y >= 0 && point.y <= Constants.BOARD_WIDTH;
    }

    private boolean pieceObstructingMove(Point start, Point end, Piece[][] board) {
        // TODO: Fix this.
        Direction direction = getDirection(start, end);

        //start.x += direction.x;
        //start.y += direction.y;

        //if (!positionInBounds(start)) return false;
		/*
		while (board[start.x][start.y] != null) {
			if (start == end) {
				break;
			}

			start.x += direction.x;
			start.y += direction.y;

			//if (!positionInBounds(start)) return false;
		}
		return start != end;*/
        return false;
    }

    private boolean isLegalForTypeOfPiece(Point start, Point end, Piece[][] board) {
        int xAbsDifference = Math.abs(start.x - end.x);
        int yAbsDifference = Math.abs(start.y - end.y);

        Piece pieceToMove = board[start.x][start.y];
        switch (pieceToMove.getPieceType()) {
            case PAWN:
                ArrayList<Direction> legalDirections = new ArrayList<>();
                if (pieceToMove.getPlayer() == 0) {
                    legalDirections.addAll(Arrays.asList(Direction.SOUTH, Direction.SOUTHEAST, Direction.SOUTHWEST));
                } else {
                    legalDirections.addAll(Arrays.asList(Direction.NORTH, Direction.NORTHEAST, Direction.NORTHWEST));
                }

                if (!legalDirections.contains(getDirection(start, end))) {
                    return false;
                }

                if (!pieceToMove.hasMoved() && xAbsDifference == 0 && yAbsDifference == 2) {
                    return true;
                }

                if (board[end.x][end.y] != null) {
                    return xAbsDifference == 1 && yAbsDifference == 1;
                }

                return xAbsDifference == 0 && yAbsDifference == 1;
            case ROOK:
                // Rooks can only move horizontally or vertically.
                // They cannot move over pieces.
                return !(xAbsDifference != 0 && yAbsDifference != 0);
            case KNIGHT:
                // These statements check for the only eight permitted L-shaped
                // moves that a Knight can make in a given turn.
                return (xAbsDifference == 2 && yAbsDifference == 1) ||
                        (xAbsDifference == 1 && yAbsDifference == 2);
            case BISHOP:
                // A bishop can only move diagonally. This means that the x difference
                // must equal the y difference.
                return xAbsDifference == yAbsDifference;
            case QUEEN:
                // A Queen can move diagonally, horizontally, or vertically anywhere
                // on the board as long as it doesn't jump over any other pieces.
                return xAbsDifference == yAbsDifference || !(xAbsDifference != 0 && yAbsDifference != 0);
            case KING:
                // A King can move in one square horizontally, vertically, or diagonally.
                return xAbsDifference + yAbsDifference == 1 || (xAbsDifference == 1 && yAbsDifference == 1);
            default:
                return false;
        }
    }

    public boolean isLegalMove(Point start, Point end, Piece[][] board) {
        if (isFriendlyFire(start, end, board) || !isLegalForTypeOfPiece(start, end, board)) {
            return false;
        }

        if (board[start.x][start.y].getPieceType() == PieceType.KNIGHT) {
            return true;
        }

        return !pieceObstructingMove(start, end, board);
    }
}