package chess;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class: Constants
 * Contains only "public static final" instance variables (constant values used throughout the game)
 *
 * @author Alex Hadi
 * @version May 2019
 */
public class Constants {
    // The board width and number of players (standard chess values)
    public static final int BOARD_WIDTH = 8;
    public static final int NUM_PLAYERS = 2;

    // The three custom colors used for the board.
    @Nonnull public static final Color FIRST_BOARD_COLOR = new Color(255, 205, 159);
    @Nonnull public static final Color SECOND_BOARD_COLOR = new Color(210, 138, 71);
    @Nonnull public static final Color GOLD = new Color(255, 215, 0); // For highlighting legal moves.

    // The values of the new, open, and save menus in the game.
    @Nonnull public static final String NEW_GAME_MENU_TEXT = "New Game";
    @Nonnull public static final String OPEN_GAME_MENU_TEXT = "Open...";
    @Nonnull public static final String SAVE_GAME_MENU_TEXT = "Save...";

    // The cardinal directions, diagonal directions, and all directions for the pieces.
    @Nonnull public static final ArrayList<Direction> CARDINAL_DIRECTIONS = new ArrayList<>(
            Arrays.asList(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)
    );
    @Nonnull public static final ArrayList<Direction> DIAGONAL_DIRECTIONS = new ArrayList<>(
            Arrays.asList(Direction.NORTHWEST, Direction.NORTHEAST, Direction.SOUTHEAST, Direction.SOUTHWEST)
    );
    @Nonnull public static final ArrayList<Direction> ALL_DIRECTIONS = new ArrayList<>(
            Arrays.asList(Direction.values())
    );
}