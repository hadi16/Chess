package chess.info

/**
 * Class: NotYourTurnInfo
 * Signifies that the player is not allowed to make a turn (it isn't their turn).
 * Inherits from ChessGameInfo.
 *
 * @author Alex Hadi
 * @version May 2020
 */
class NotYourTurnInfo : ChessGameInfo() {
    override fun maybeErrorMessage(): String? = "It is not your turn!"
}
