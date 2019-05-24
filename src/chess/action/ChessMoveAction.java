package chess.action;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Objects;

/**
 * Class: ChessMoveAction
 * The class of every move in the game.
 * Inherits from ChessAction.
 *
 * @author Alex Hadi
 * @version May 2019
 */
public class ChessMoveAction extends ChessAction {
    private final int playerId;

    @Nonnull private final Point startPosition;
    @Nonnull private final Point endPosition;

    /**
     * Constructor: ChessMoveAction
     * Creates a new action to move a chess piece.
     *
     * @param playerId The ID of the player requesting the move.
     * @param startPosition The current position of the piece.
     * @param endPosition The position of the piece to move to.
     */
    public ChessMoveAction(int playerId, @Nonnull Point startPosition, @Nonnull Point endPosition) {
        Objects.requireNonNull(startPosition);
        Objects.requireNonNull(endPosition);

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
    @Nonnull
    public Point getStartPosition() {
        return new Point(startPosition);
    }

    /**
     * Getter: getEndPosition
     * @return The ending position of the piece as a Point (deep copy).
     */
    @Nonnull
    public Point getEndPosition() {
        return new Point(endPosition);
    }
}