package chess

import chess.action.ChessMoveAction
import chess.action.PawnPromotionAction
import chess.info.ChessGameInfo
import java.awt.Point
import java.io.Serializable

/**
 * Class: ChessState
 * Represents a state in the chess game.
 *
 * @author Alex Hadi
 * @version July 2019
 */
class ChessState : ChessGameInfo, Serializable {
    val board: HashMap<Point, Piece>

    var currentTurn: Int
        private set

    // The player ID of the checked player (or -1 if neither are in check).
    var checkedPlayer: Int
        private set

    /**
     * Constructor: ChessState
     * Creates a new chess state. Just sets it to the default state
     */
    constructor() {
        board = getDefaultBoard()
        currentTurn = getDefaultTurn()
        checkedPlayer = getDefaultCheckedPlayer()
    }

    /**
     * Copy Constructor: ChessState
     * Makes a deep copy of the passed in ChessState.
     *
     * @param originalState The ChessState to copy.
     */
    constructor(originalState: ChessState) {
        board = originalState.board
        currentTurn = originalState.currentTurn
        checkedPlayer = originalState.checkedPlayer
    }

    /**
     * Method: getDefaultBoard
     * Resets the board to its default configuration.
     */
    private fun getDefaultBoard(): HashMap<Point, Piece> {
        val board = HashMap<Point, Piece>()

        // Player 0's pieces are set.
        for (i in 0 until Constants.BOARD_WIDTH) {
            board[Point(i, 1)] = Piece(PieceType.PAWN, 0)
        }
        board[Point(0, 0)] = Piece(PieceType.ROOK, 0)
        board[Point(1, 0)] = Piece(PieceType.KNIGHT, 0)
        board[Point(2, 0)] = Piece(PieceType.BISHOP, 0)
        board[Point(3, 0)] = Piece(PieceType.QUEEN, 0)
        board[Point(4, 0)] = Piece(PieceType.KING, 0)
        board[Point(5, 0)] = Piece(PieceType.BISHOP, 0)
        board[Point(6, 0)] = Piece(PieceType.KNIGHT, 0)
        board[Point(7, 0)] = Piece(PieceType.ROOK, 0)

        // Player 1's pieces are set.
        for (i in 0 until Constants.BOARD_WIDTH) {
            board[Point(i, 6)] = Piece(PieceType.PAWN, 1)
        }
        board[Point(0, 7)] = Piece(PieceType.ROOK, 1)
        board[Point(1, 7)] = Piece(PieceType.KNIGHT, 1)
        board[Point(2, 7)] = Piece(PieceType.BISHOP, 1)
        board[Point(3, 7)] = Piece(PieceType.QUEEN, 1)
        board[Point(4, 7)] = Piece(PieceType.KING, 1)
        board[Point(5, 7)] = Piece(PieceType.BISHOP, 1)
        board[Point(6, 7)] = Piece(PieceType.KNIGHT, 1)
        board[Point(7, 7)] = Piece(PieceType.ROOK, 1)

        return board
    }

    /** The white player always starts first. */
    private fun getDefaultTurn(): Int = 1

    /** Neither player is initially in check. */
    private fun getDefaultCheckedPlayer(): Int = -1

    /**
     * Method: getNextState
     * Gets the next state of the game after a move is played in the game.
     *
     * @param moveAction The move to play in the game.
     * @return The next state in the game after the given move is played.
     */
    fun getNextState(moveAction: ChessMoveAction): ChessState {
        // Get the starting and ending position for the piece.
        val startPosition = moveAction.startPosition
        val endPosition = moveAction.endPosition

        // Gets a new ChessState to return (as a deep copy).
        val nextState = ChessState(this)

        // Get the piece to move and sets it as moved.
        nextState.removeAndReturnPieceAtPosition(startPosition)?.let {
            it.moved = true

            // If the action involved pawn promotion, update the piece type.
            if (moveAction is PawnPromotionAction) {
                it.pieceType = moveAction.promotedPieceType
            }

            // Set the piece in the master state.
            nextState.setBoardAtPosition(endPosition, it)
        }

        // Change the turn.
        nextState.changeTurn()

        // Reset the checked player.
        val checkTester = CheckTester(nextState)
        nextState.checkedPlayer = checkTester.playerInCheck()

        return nextState
    }

    /**
     * Helper Method: setBoardAtPosition
     * Sets the given position in the board to a piece.
     *
     * @param position The position in the board to set.
     * @param piece The piece to set the board's position to.
     */
    private fun setBoardAtPosition(position: Point, piece: Piece) {
        // Position must always be in bounds.
        if (!Helpers.positionInBounds(position)) {
            return
        }

        board[position] = piece
    }

    /**
     * Helper Method: removeAndReturnPieceAtPosition
     * Removes the Piece at the given position (or null).
     *
     * @param position The position to remove the piece at.
     * @return The Piece at this position (or null).
     */
    private fun removeAndReturnPieceAtPosition(position: Point): Piece? = board.remove(position)

    /**
     * Helper Method: changeTurn
     * Changes the current turn to the other player ID (0->1 or 1->0)
     */
    private fun changeTurn() {
        currentTurn = 1 - currentTurn
    }

    /**
     * Method: isMyTurn
     * Determines whether it is the given player's turn or not.
     *
     * @param player The player ID to check (0 or 1).
     * @return true if it is their turn (otherwise false).
     */
    fun isMyTurn(player: Int): Boolean {
        return currentTurn == player
    }

    /**
     * Method: positionInBoard
     * Determines if a given position is in the board.
     *
     * @param position The position to check.
     * @return true if the position is in the board (otherwise false).
     */
    fun positionInBoard(position: Point): Boolean = board.containsKey(position)

    /**
     * Getter: getPieceAtPosition
     * Gets a copy of a piece (value of the HashMap) at a position (key of the HashMap).
     *
     * @param position The position to query.
     * @return The piece at this position.
     */
    fun getPieceAtPosition(position: Point): Piece? {
        val pieceToGet = board[position] ?: return null

        // Uses Piece's copy constructor to get deep copy.
        return Piece(pieceToGet)
    }

    /**
     * Getter: getKingPosition
     * Gets the position of the given player's king (to determine whether in check)
     *
     * @param playerId The ID of the player (0 or 1).
     * @return A deep copy of the position of the player's king.
     */
    fun getKingPosition(playerId: Int): Point? = board.entries.singleOrNull {
        val piece = it.value
        piece.pieceType == PieceType.KING && piece.player == playerId
    }?.key

    /**
     * Getter: getPlayerPieces
     * Gets a copy of the positions and pieces associated with the given player.
     *
     * @param playerId The player ID to check.
     * @return A HashMap mapping each position of the player's pieces to the Piece.
     */
    fun getPlayerPieces(playerId: Int): Map<Point, Piece> = board.filter { it.value.player == playerId }
}
