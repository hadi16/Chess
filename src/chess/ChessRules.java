package chess;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Class: ChessRules
 * Determines whether given moves in the game are legal,
 * according to the established chess rules.
 * Note: en passant & castling are not supported yet.
 *
 * @author Alex Hadi
 * @version May 2019
 */
public class ChessRules {
    // The starting position to check (never reassigned).
    @Nonnull private final Point startPosition;

    // The current board (never reassigned).
    @Nonnull private final Map<Point, Piece> board;

    /**
     * Constructor: ChessRules
     * Creates a new ChessRules object.
     *
     * @param startPosition The start position to check.
     * @param currentState The current state of the game.
     */
    public ChessRules(@Nonnull Point startPosition, @Nonnull ChessState currentState) {
        this.startPosition = startPosition;
        board = currentState.getBoardDeepCopy();
    }

    /**
     * Helper Method: notFriendlyFire
     * Determines whether the given move constitutes friendly fire.
     *
     * @param endPosition The end position to check.
     * @return true if the move doesn't constitute friendly fire (otherwise false).
     */
    private boolean notFriendlyFire(@Nonnull Point endPosition) {
        // If the board doesn't contain the position,
        // the move doesn't constitute friendly fire.
        if (!board.containsKey(endPosition)) {
            return true;
        }

        // Check to make sure that the starting position's player doesn't equal the ending position's player.
        return board.get(startPosition).getPlayer() != board.get(endPosition).getPlayer();
    }

    /**
     * Helper Method: getLegalEndPointsInDirection
     * Finds all the legal end points in a single direction.
     *
     * @param direction The Direction to check for legal end positions.
     * @param maxPoints Maximum amount of legal end points that can be in this direction (especially useful for king)
     * @return The list of all legal end points in the direction.
     */
    @Nonnull
    private ArrayList<Point> getLegalEndPointsInDirection(@Nonnull Direction direction, int maxPoints) {
        // Add the x & y value to the current position's x & y and construct as a new position.
        Point currentPosition = new Point(
                startPosition.x + direction.getX(),
                startPosition.y + direction.getY()
        );

        ArrayList<Point> legalEndPoints = new ArrayList<>();
        // The end points must be in bounds.
        // The # of current end points must also be less than the max # of end points to be able to add more to it.
        while (Helpers.positionInBounds(currentPosition) && legalEndPoints.size() < maxPoints) {
            // If the position has a piece in it.
            if (board.containsKey(currentPosition)) {
                // As long as it is not one of the player's own pieces, they can attack it.
                if (notFriendlyFire(currentPosition)) {
                    legalEndPoints.add(currentPosition);
                }

                // The pieces can't jump over other pieces.
                return legalEndPoints;
            }

            // Add the current position (there was no piece at this position)
            legalEndPoints.add(currentPosition);

            // Add the x & y value for the direction to the current position's x & y value.
            currentPosition = new Point(
                    currentPosition.x + direction.getX(),
                    currentPosition.y + direction.getY()
            );
        }
        return legalEndPoints;
    }

    /**
     * Helper Method: getAllLegalEndPointsForDirections
     * Loops through all the passed in directions & finds the legal end points for all directions.
     *
     * @param directions The directions to check.
     * @param maxPointsPerDirection The maximum amount of legal end points each direction can have.
     * @return A list of all the legal end points.
     */
    @Nonnull
    private ArrayList<Point> getAllLegalEndPointsForDirections(@Nonnull ArrayList<Direction> directions,
                                                               int maxPointsPerDirection) {
        ArrayList<Point> legalEndPoints = new ArrayList<>();
        for (Direction direction : directions) {
            legalEndPoints.addAll(getLegalEndPointsInDirection(direction, maxPointsPerDirection));
        }
        return legalEndPoints;
    }

    /**
     * Overloaded method: getAllLegalEndPointsForDirections
     * Uses a default value of Constants.BOARD_WIDTH - 1 for the maximum amount of points per direction.
     * This is the maximum distance that any piece in chess could traverse in a single direction.
     *
     * @param directions The directions to check for legal end points.
     * @return A list of all the legal end points for the given position.
     */
    @Nonnull
    private ArrayList<Point> getAllLegalEndPointsForDirections(@Nonnull ArrayList<Direction> directions) {
        return getAllLegalEndPointsForDirections(directions, Constants.BOARD_WIDTH - 1);
    }

    /**
     * Helper Method: getKnightLegalEndPoints
     * Gets all the legal ending positions for a knight.
     *
     * @return The list of all the legal end positions for the knight.
     */
    @Nonnull
    private ArrayList<Point> getKnightLegalEndPoints() {
        // A knight can only move in an "L" shape (eight predefined directions).
        final Point[] ALL_KNIGHT_DIRECTIONS = {
                new Point(-2, -1), new Point(-2, 1),
                new Point(-1, -2), new Point(-1, 2),
                new Point(1, -2), new Point(1, 2),
                new Point(2, -1), new Point(2, 1)
        };

        ArrayList<Point> knightLegalEndPoints = new ArrayList<>();

        // Goes through each of the legal knight directions.
        for (Point direction : ALL_KNIGHT_DIRECTIONS) {
            Point endPosition = new Point(
                    startPosition.x + direction.x, startPosition.y + direction.y
            );

            // Checks to make sure this position is in bounds & not friendly fire.
            // If so, it is a legal end point, so it is added.
            if (Helpers.positionInBounds(endPosition) && notFriendlyFire(endPosition)) {
                knightLegalEndPoints.add(endPosition);
            }
        }
        return knightLegalEndPoints;
    }

    /**
     * Helper Method: getPawnLegalEndPoints
     * Finds all legal end positions for a pawn.
     *
     * @return The list of legal moves for the pawn.
     */
    @Nonnull
    private ArrayList<Point> getPawnLegalEndPoints() {
        // Create the list of legal end points to return & get the pawn.
        ArrayList<Point> pawnLegalEndPoints = new ArrayList<>();
        Piece pawn = board.get(startPosition);

        // Goes through the attacking directions & adds at most one position.
        for (Direction direction : Direction.getPawnAttackingDirections(pawn.getPlayer())) {
            Point attackingPosition = new Point(
                    startPosition.x + direction.getX(),
                    startPosition.y + direction.getY()
            );

            // There must be a piece to attack and it must not be friendly fire.
            if (board.containsKey(attackingPosition) && notFriendlyFire(attackingPosition)) {
                pawnLegalEndPoints.add(attackingPosition);
            }
        }

        // Gets the "regular" moving direction for the pawn & initializes the current position.
        Direction regularDirection = Direction.getPawnRegularDirection(pawn.getPlayer());
        Point currentPosition = new Point(startPosition.x, startPosition.y);

        // If the pawn hasn't moved, it can move up to two spaces forward.
        // Otherwise, it can only move one space forward.
        int maxDistance = pawn.hasMoved() ? 1 : 2;

        for (int i = 0; i < maxDistance; i++) {
            currentPosition = new Point(
                    currentPosition.x + regularDirection.getX(),
                    currentPosition.y + regularDirection.getY()
            );

            // There can't be any obstructing pieces when pawn is moving regularly.
            if (board.containsKey(currentPosition)) {
                break;
            }

            // The position for the pawn's end point must be in bounds.
            if (Helpers.positionInBounds(currentPosition)) {
                pawnLegalEndPoints.add(currentPosition);
            }
        }

        return pawnLegalEndPoints;
    }

    /**
     * Method: getLegalEndPointsForPosition
     * Gets a list of all the points that the given starting position's piece can go to.
     *
     * @return A list of Point objects representing all the legal end points for the piece to move.
     */
    @Nonnull
    public ArrayList<Point> getLegalEndPointsForPosition() {
        // If the position is empty, it can clearly go nowhere (empty list).
        if (!board.containsKey(startPosition)) {
            return new ArrayList<>();
        }

        // Checks the piece type.
        Piece pieceToCheck = board.get(startPosition);
        switch (pieceToCheck.getPieceType()) {
            case PAWN:
                // Pawn moves are more complex (uses dedicated method)
                return getPawnLegalEndPoints();
            case ROOK:
                // Rooks can move in any cardinal direction.
                return getAllLegalEndPointsForDirections(Constants.CARDINAL_DIRECTIONS);
            case KNIGHT:
                // Knight moves are a special case (handled with dedicated method)
                return getKnightLegalEndPoints();
            case BISHOP:
                // Bishops can move in all diagonal directions.
                return getAllLegalEndPointsForDirections(Constants.DIAGONAL_DIRECTIONS);
            case QUEEN:
                // Queens can move in any direction.
                return getAllLegalEndPointsForDirections(Constants.ALL_DIRECTIONS);
            case KING:
                // Kings can move in any direction (but only one tile).
                return getAllLegalEndPointsForDirections(Constants.ALL_DIRECTIONS, 1);
            default:
                return new ArrayList<>();
        }
    }

    /**
     * Method: isLegalMove
     * Determines whether the given end position constitutes a valid move.
     *
     * @param endPosition The end position to check.
     * @return true if the move is valid (otherwise false).
     */
    public boolean isLegalMove(@Nonnull Point endPosition) {
        // Get the list of all legal end points and determine if the point is in the list.
        ArrayList<Point> legalEndPoints = getLegalEndPointsForPosition();
        return legalEndPoints.contains(endPosition);
    }

    /**
     * Method: canPromotePawn
     * Determines whether the end position constitutes a valid pawn promotion move.
     *
     * @param endPosition The end position to check.
     * @return true if the pawn can be promoted (otherwise false).
     */
    public boolean canPromotePawn(@Nonnull Point endPosition) {
        // It must be a legal move.
        if (!isLegalMove(endPosition)) {
            return false;
        }

        // Must be a pawn to promote it.
        Piece pieceToMove = board.get(startPosition);
        if (pieceToMove.getPieceType() != PieceType.PAWN) {
            return false;
        }

        // Checks for whether the pawn moved up to the other side of the chess board (y value).
        if (pieceToMove.getPlayer() == 0) {
            return endPosition.y == Constants.BOARD_WIDTH - 1;
        } else {
            return endPosition.y == 0;
        }
    }
}