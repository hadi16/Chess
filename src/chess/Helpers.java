package chess;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Optional;

/**
 * Class: Helpers
 * Contains static helper methods that are used throughout the codebase.
 *
 * @author Alex Hadi
 * @version May 2019
 */
public class Helpers {
    /**
     * Static Method: positionInBounds
     * Checks if a point is a valid point on the chess board.
     *
     * @param position The Point to check.
     * @return true if a valid point (otherwise false)
     */
    public static boolean positionInBounds(@Nonnull Point position) {
        return position.x >= 0 && position.x < Constants.BOARD_WIDTH &&
               position.y >= 0 && position.y < Constants.BOARD_WIDTH;
    }

    /**
     * Static Method: getValidPointOrEmpty
     * Gets the corresponding chess board point that was clicked if it is in bounds.
     * Otherwise, return null.
     *
     * @param mousePosition The Point that was clicked.
     * @param scaleDim The current scaling factor value.
     * @return A valid chess board Point (or null).
     */
    @Nonnull
    public static Optional<Point> getValidPointOrEmpty(@Nonnull Point mousePosition, int scaleDim) {
        // Use the scaling factor to get the corresponding point on the board.
        Point pointClicked = new Point(
                mousePosition.x / scaleDim, mousePosition.y / scaleDim
        );

        // If the position is in bounds of the chess board,
        // return it (otherwise, return empty optional).
        return Helpers.positionInBounds(pointClicked) ? Optional.of(pointClicked) : Optional.empty();
    }
}