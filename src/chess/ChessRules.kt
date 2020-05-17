package chess

import java.awt.Point
import java.util.*

/**
 * Class: ChessRules
 * Determines whether given moves in the game are legal,
 * according to the established chess rules.
 * Note: en passant & castling are not supported yet.
 *
 * @author Alex Hadi
 * @version May 2020
 */
class ChessRules(private val startPosition: Point, currentState: ChessState) {
    // The current board (never reassigned).
    private val board: Map<Point, Piece>

    init {
        this.board = currentState.board
    }

    /**
     * Helper Method: notFriendlyFire
     * Determines whether the given move constitutes friendly fire.
     *
     * @param endPosition The end position to check.
     * @return true if the move doesn't constitute friendly fire (otherwise false).
     */
    private fun notFriendlyFire(endPosition: Point): Boolean {
        // If the board doesn't contain the position,
        // the move doesn't constitute friendly fire.
        if (!this.board.containsKey(endPosition)) {
            return true
        }

        // Check to make sure that the starting position's player doesn't equal the ending position's player.
        return this.board[this.startPosition]?.player != this.board[endPosition]?.player
    }

    /**
     * Helper Method: getLegalEndPointsInDirection
     * Finds all the legal end points in a single direction.
     *
     * @param direction The Direction to check for legal end positions.
     * @param maxPoints Maximum amount of legal end points that can be in this direction (especially useful for king)
     * @return The list of all legal end points in the direction.
     */
    private fun getLegalEndPointsInDirection(direction: Direction, maxPoints: Int): ArrayList<Point> {
        // Add the x & y value to the current position's x & y and construct as a new position.
        var currentPosition = Point(
                this.startPosition.x + direction.x,
                this.startPosition.y + direction.y
        )

        // The end points must be in bounds.
        // The # of current end points must also be less than the max # of end points to be able to add more to it.
        val legalEndPoints = ArrayList<Point>()
        while (Helpers.positionInBounds(currentPosition) && legalEndPoints.size < maxPoints) {
            // If the position has a piece in it.
            if (this.board.containsKey(currentPosition)) {
                // As long as it is not one of the player's own pieces, they can attack it.
                if (this.notFriendlyFire(currentPosition)) {
                    legalEndPoints.add(currentPosition)
                }

                // The pieces can't jump over other pieces.
                return legalEndPoints
            }

            // Add the current position (there was no piece at this position)
            legalEndPoints.add(currentPosition)

            // Add the x & y value for the direction to the current position's x & y value.
            currentPosition = Point(
                    currentPosition.x + direction.x,
                    currentPosition.y + direction.y
            )
        }
        return legalEndPoints
    }

    /**
     * Helper Method: getAllLegalEndPointsForDirections
     * Loops through all the passed in directions & finds the legal end points for all directions.
     *
     * @param directions The directions to check.
     * @param maxPointsPerDirection The maximum amount of legal end points each direction can have.
     * @return A list of all the legal end points.
     */
    private fun getAllLegalEndPointsForDirections(
            directions: List<Direction>, maxPointsPerDirection: Int
    ): ArrayList<Point> {
        val legalEndPoints = ArrayList<Point>()
        for (direction in directions) {
            legalEndPoints.addAll(this.getLegalEndPointsInDirection(direction, maxPointsPerDirection))
        }
        return legalEndPoints
    }

    /**
     * Overloaded method: getAllLegalEndPointsForDirections
     * Uses a default value of Constants.BOARD_WIDTH - 1 for the maximum amount of points per direction.
     * This is the maximum distance that any piece in chess could traverse in a single direction.
     *
     * @param directions The directions to check for legal end points.
     * @return A list of all the legal end points for the given position.
     */
    private fun getAllLegalEndPointsForDirections(
            directions: List<Direction>
    ): ArrayList<Point> = this.getAllLegalEndPointsForDirections(directions, Constants.BOARD_WIDTH - 1)

    /**
     * Helper Method: getKnightLegalEndPoints
     * Gets all the legal ending positions for a knight.
     *
     * @return The list of all the legal end positions for the knight.
     */
    private fun getKnightLegalEndPoints(): ArrayList<Point> {
        // A knight can only move in an "L" shape (eight predefined directions).
        val ALL_KNIGHT_DIRECTIONS: Array<Point> = arrayOf(
                Point(-2, -1), Point(-2, 1),
                Point(-1, -2), Point(-1, 2),
                Point(1, -2), Point(1, 2),
                Point(2, -1), Point(2, 1)
        )

        // Goes through each of the legal knight directions.
        val knightLegalEndPoints = ArrayList<Point>()
        for (direction: Point in ALL_KNIGHT_DIRECTIONS) {
            val endPosition = Point(this.startPosition.x + direction.x, this.startPosition.y + direction.y)

            // Checks to make sure this position is in bounds & not friendly fire.
            // If so, it is a legal end point, so it is added.
            if (Helpers.positionInBounds(endPosition) && this.notFriendlyFire(endPosition)) {
                knightLegalEndPoints.add(endPosition)
            }
        }
        return knightLegalEndPoints
    }

    /**
     * Helper Method: getPawnLegalEndPoints
     * Finds all legal end positions for a pawn.
     *
     * @return The list of legal moves for the pawn.
     */
    private fun getPawnLegalEndPoints(): ArrayList<Point> {
        // Create the list of legal end points to return & get the pawn.
        val pawn = this.board[this.startPosition] ?: return ArrayList()

        val pawnLegalEndPoints = ArrayList<Point>()
        // Goes through the attacking directions & adds at most one position.
        for (direction in Direction.getPawnAttackingDirections(pawn.player)) {
            val attackingPosition = Point(
                    this.startPosition.x + direction.x,
                    this.startPosition.y + direction.y
            )

            // There must be a piece to attack and it must not be friendly fire.
            if (this.board.containsKey(attackingPosition) && this.notFriendlyFire(attackingPosition)) {
                pawnLegalEndPoints.add(attackingPosition)
            }
        }

        // Gets the "regular" moving direction for the pawn & initializes the current position.
        val regularDirection = Direction.getPawnRegularDirection(pawn.player)

        // If the pawn hasn't moved, it can move up to two spaces forward.
        // Otherwise, it can only move one space forward.
        val maxDistance = if (pawn.moved) 1 else 2

        var currentPosition = Point(this.startPosition.x, this.startPosition.y)
        for (i in 0 until maxDistance) {
            currentPosition = Point(
                    currentPosition.x + regularDirection.x,
                    currentPosition.y + regularDirection.y
            )

            // There can't be any obstructing pieces when pawn is moving regularly.
            if (this.board.containsKey(currentPosition)) {
                break
            }

            // The position for the pawn's end point must be in bounds.
            if (Helpers.positionInBounds(currentPosition)) {
                pawnLegalEndPoints.add(currentPosition)
            }
        }

        return pawnLegalEndPoints
    }

    /**
     * Method: getLegalEndPointsForPosition
     * Gets a list of all the points that the given starting position's piece can go to.
     *
     * @return A list of Point objects representing all the legal end points for the piece to move.
     */
    fun getLegalEndPointsForPosition(): ArrayList<Point> {
        // If the position is empty, it can clearly go nowhere (empty list).
        if (!this.board.containsKey(this.startPosition)) {
            return arrayListOf()
        }

        // Checks the piece type.
        val pieceToCheck = this.board[this.startPosition]
        when (pieceToCheck?.pieceType) {
            PieceType.PAWN ->
                // Pawn moves are more complex (uses dedicated method)
                return this.getPawnLegalEndPoints()
            PieceType.ROOK ->
                // Rooks can move in any cardinal direction.
                return this.getAllLegalEndPointsForDirections(Constants.CARDINAL_DIRECTIONS)
            PieceType.KNIGHT ->
                // Knight moves are a special case (handled with dedicated method)
                return this.getKnightLegalEndPoints()
            PieceType.BISHOP ->
                // Bishops can move in all diagonal directions.
                return this.getAllLegalEndPointsForDirections(Constants.DIAGONAL_DIRECTIONS)
            PieceType.QUEEN ->
                // Queens can move in any direction.
                return this.getAllLegalEndPointsForDirections(Constants.ALL_DIRECTIONS)
            PieceType.KING ->
                // Kings can move in any direction (but only one tile).
                return this.getAllLegalEndPointsForDirections(Constants.ALL_DIRECTIONS, 1)
            else ->
                return arrayListOf()
        }
    }

    /**
     * Method: isLegalMove
     * Determines whether the given end position constitutes a valid move.
     *
     * @param endPosition The end position to check.
     * @return true if the move is valid (otherwise false).
     */
    fun isLegalMove(endPosition: Point): Boolean {
        // Get the list of all legal end points and determine if the point is in the list.
        val legalEndPoints = this.getLegalEndPointsForPosition()
        return legalEndPoints.contains(endPosition)
    }

    /**
     * Method: canPromotePawn
     * Determines whether the end position constitutes a valid pawn promotion move.
     *
     * @param endPosition The end position to check.
     * @return true if the pawn can be promoted (otherwise false).
     */
    fun canPromotePawn(endPosition: Point): Boolean {
        // It must be a legal move.
        if (!this.isLegalMove(endPosition)) {
            return false
        }

        // Must be a pawn to promote it.
        val pieceToMove = this.board[this.startPosition]
        if (pieceToMove?.pieceType != PieceType.PAWN) {
            return false
        }

        // Checks for whether the pawn moved up to the other side of the chess board (y value).
        return if (pieceToMove.player == 0) {
            endPosition.y == Constants.BOARD_WIDTH - 1
        } else {
            endPosition.y == 0
        }
    }
}
