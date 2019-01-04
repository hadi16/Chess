import java.awt.*;
import java.util.ArrayList;

public class ChessRules {
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
            if (direction.getX() == x && direction.getY() == y) {
                return direction;
            }
        }
        return null;
    }

    private boolean positionInBounds(Point position) {
        return position.x >= 0 && position.x < Constants.BOARD_WIDTH &&
               position.y >= 0 && position.y < Constants.BOARD_WIDTH;
    }

    private boolean pieceClear(Point start, Point end, Piece[][] board) {
        Direction direction = getDirection(start, end);
        Point currentPosition = new Point(start.x + direction.getX(), start.y + direction.getY());
        if (!positionInBounds(currentPosition)) {
            return false;
        }

		while (positionInBounds(currentPosition) && !currentPosition.equals(end)) {
		    if (board[currentPosition.x][currentPosition.y] != null) {
		        return currentPosition.equals(end);
            }

			currentPosition.x += direction.getX();
			currentPosition.y += direction.getY();
		}
		return true;
    }

    private boolean isLegalForTypeOfPiece(Point start, Point end, Piece[][] board) {
        int xAbsDifference = Math.abs(start.x - end.x);
        int yAbsDifference = Math.abs(start.y - end.y);

        Piece pieceToMove = board[start.x][start.y];

        ArrayList<Direction> legalDirections = pieceToMove.getPieceType().getLegalDirections(pieceToMove);
        if (!legalDirections.contains(getDirection(start, end))) {
            return false;
        }

        switch (pieceToMove.getPieceType()) {
            case PAWN:
                if (!pieceToMove.hasMoved() && xAbsDifference == 0 && yAbsDifference == 2) {
                    return board[end.x][end.y] == null;
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
                return true;
        }
    }

    public boolean isLegalMove(Point start, Point end, Piece[][] board) {
        if (isFriendlyFire(start, end, board) || !isLegalForTypeOfPiece(start, end, board)) {
            return false;
        }

        if (board[start.x][start.y].getPieceType() == PieceType.KNIGHT) {
            return true;
        }

        return pieceClear(start, end, board);
    }
}