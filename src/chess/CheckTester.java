package chess;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Class: CheckTester
 * Determines which player is in check (if any).
 *
 * @author Alex Hadi
 * @version May 2019
 */
public class CheckTester {
    // The current state
    @Nonnull private final ChessState chessState;

    // The positions of each player's king (index 0 maps to position for player 0's king, etc.)
    @Nonnull private final ArrayList<Point> kingPositions = new ArrayList<>();

    // The positions and pieces of each player's pieces (index 0 maps to positions/pieces for player 0, etc.)
    @Nonnull private final ArrayList<Map<Point, Piece>> playerPieces = new ArrayList<>();

    // Whether each player is in check (index 0 is true if player 0 is in check, etc.)
    @Nonnull private final ArrayList<Boolean> playersChecked = new ArrayList<>();

    /**
     * Constructor: CheckTester
     * Creates a new CheckTester object.
     *
     * @param chessState The current state of the game.
     */
    public CheckTester(@Nonnull ChessState chessState) {
        this.chessState = chessState;

        // Loops through and adds each player's pieces and king position to the corresponding instance variables.
        for (int playerId = 0; playerId < Constants.NUM_PLAYERS; playerId++) {
            playerPieces.add(chessState.getPlayerPieces(playerId));
            chessState.getKingPosition(playerId).ifPresent(kingPositions::add);
        }

        // Loops through and determines which player(s) are in check (if any)
        for (int playerId = 0; playerId < Constants.NUM_PLAYERS; playerId++) {
            playersChecked.add(playerInCheck(playerId));
        }
    }

    /**
     * Method: playerInCheck
     * Determines if the given player is in check.
     *
     * @param playerId The player ID to check for (0 or 1).
     * @return true if this player is in check (otherwise false).
     */
    private boolean playerInCheck(int playerId) {
        // Gets the position of the player's king and their enemy's pieces.
        Point myKingPosition = kingPositions.get(playerId);
        Map<Point, Piece> enemyPlayerPieces = playerPieces.get(1 - playerId);

        // Loops through and determines if any of the enemy's pieces
        // can legally attack their king (means they are in check).
        for (Map.Entry<Point, Piece> entry : enemyPlayerPieces.entrySet()) {
            Point position = entry.getKey();

            ChessRules chessRules = new ChessRules(position, chessState);
            if (chessRules.isLegalMove(myKingPosition)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Getter: getCheckedPlayer
     * Returns which player is in check (if any).
     *
     * @return The player ID of the player in check (or -1 if neither are in check).
     */
    public int getCheckedPlayer() {
        // Finds the index of "true" in the list (or -1 if both indices are false).
        return playersChecked.indexOf(true);
    }
}