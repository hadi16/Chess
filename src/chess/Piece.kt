package chess

import java.io.Serializable

/**
 * Class: Piece
 * Represents a chess piece in the game.
 *
 * @author Alex Hadi
 * @version July 2019
 */
class Piece : Serializable {
    val player: Int

    var moved: Boolean = false
    var pieceType: PieceType // Can only change with pawn promotion.

    /**
     * Constructor: Piece
     * Creates a new chess piece in the game.
     *
     * @param pieceType The piece type (as an instance of the PieceType enumeration).
     * @param player The player ID for the piece (0 or 1).
     */
    constructor(pieceType: PieceType, player: Int) {
        this.pieceType = pieceType
        this.player = player
    }

    /**
     * Copy Constructor: Piece
     * Creates a deep copy of the passed in chess piece.
     *
     * @param piece The chess piece to copy.
     */
    constructor(piece: Piece) {
        pieceType = piece.pieceType
        player = piece.player
        moved = piece.moved
    }
}
