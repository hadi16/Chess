package chess

import java.awt.Point
import java.util.*

/**
 * Class: CheckTester
 * Determines which player is in check (if any).
 *
 * @author Alex Hadi
 * @version May 2020
 */
class CheckTester(private val chessState: ChessState) {
    // The positions of each player's king (index 0 maps to position for player 0's king, etc.)
    private val kingPositions = ArrayList<Point>()

    // The positions and pieces of each player's pieces (index 0 maps to positions/pieces for player 0, etc.)
    private val playerPieces = ArrayList<Map<Point, Piece>>()

    // Whether each player is in check (index 0 is true if player 0 is in check, etc.)
    private val playersChecked = ArrayList<Boolean>()

    init {
        for (playerId in 0 until Constants.NUM_PLAYERS) {
            this.playerPieces.add(this.chessState.getPlayerPieces(playerId))
            this.chessState.getKingPosition(playerId)?.let(this.kingPositions::add)
        }

        // Loops through and determines which player(s) are in check (if any)
        this.run {
            if (this.kingPositions.size != 2) {
                return@run
            }

            for (playerId in 0 until Constants.NUM_PLAYERS) {
                this.playersChecked.add(this.playerInCheck(playerId))
            }
        }
    }

    /**
     * Method: playerInCheck
     * Determines if the given player is in check.
     *
     * @param playerId The player ID to check for (0 or 1).
     * @return true if this player is in check (otherwise false).
     */
    private fun playerInCheck(playerId: Int): Boolean {
        // Gets the position of the player's king and their enemy's pieces.
        val myKingPosition = this.kingPositions[playerId]
        val enemyPlayerPieces = this.playerPieces[1 - playerId]

        // Determines if any of the enemy's pieces can legally attack their king (means they are in check).
        return enemyPlayerPieces.any { ChessRules(it.key, this.chessState).isLegalMove(myKingPosition) }
    }

    /**
     * Getter: getCheckedPlayer
     * Returns which player is in check (if any).
     *
     * @return The player ID of the player in check (or -1 if neither are in check).
     */
    fun playerInCheck(): Int? {
        val checkedPlayer = this.playersChecked.indexOf(true)
        return if (checkedPlayer != -1) checkedPlayer else null
    }
}
