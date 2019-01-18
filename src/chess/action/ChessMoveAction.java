package chess.action;

import java.awt.*;

/**
 * Class: ChessMoveAction
 * The class of every move in the game.
 * Inherits from ChessAction.
 *
 * @author Alex Hadi
 * @version January 2019
 */
public class ChessMoveAction extends ChessAction {
    // The player ID, start position, and
    // end position of a move (never reassigned)
    private final int playerId;
    private final Point startPosition;
    private final Point endPosition;

    /**
     * Constructor: ChessMoveAction
     * Creates a new action to move a chess piece.
     *
     * @param playerId The ID of the player requesting the move.
     * @param startPosition The current position of the piece.
     * @param endPosition The position of the piece to move to.
     */
    public ChessMoveAction(int playerId, Point startPosition, Point endPosition) {
        this.playerId = playerId;

        // Uses Point class copy constructor
        this.startPosition = new Point(startPosition);
        this.endPosition = new Point(endPosition);
    }

    /**
     * Getter: getPlayerId
     * @return The ID of the player for the move.
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * Getter: getStartPosition
     * @return The current position of the piece as a Point (deep copy).
     */
    public Point getStartPosition() {
        return new Point(startPosition);
    }

    /**
     * Getter: getEndPosition
     * @return The ending position of the piece as a Point (deep copy).
     */
    public Point getEndPosition() {
        return new Point(endPosition);
    }
}