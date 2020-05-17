package chess

import java.io.Serializable

/**
 * Class: Piece
 * Represents a chess piece in the game.
 *
 * @author Alex Hadi
 * @version May 2020
 */
class Piece : Serializable {
    val pieceType: PieceType
    val player: Int
    val moved: Boolean

    /**
     * Constructor: Piece
     * Creates a new chess piece in the game.
     *
     * @param pieceType The piece type (as an instance of the PieceType enumeration).
     * @param player The player ID for the piece (0 or 1).
     * @param moved Whether the piece has moved.
     */
    constructor(pieceType: PieceType, player: Int, moved: Boolean) {
        this.pieceType = pieceType
        this.player = player
        this.moved = moved
    }

    /**
     * Constructor: Piece
     * Creates a new chess piece in the game that has not moved.
     *
     * @param pieceType The piece type (as an instance of the PieceType enumeration).
     * @param player The player ID for the piece (0 or 1).
     */
    constructor(pieceType: PieceType, player: Int) {
        this.pieceType = pieceType
        this.player = player
        this.moved = false
    }

    /**
     * Copy Constructor: Piece
     * Creates a deep copy of the passed in chess piece.
     *
     * @param piece The chess piece to copy.
     */
    constructor(piece: Piece) {
        this.pieceType = piece.pieceType
        this.player = piece.player
        this.moved = piece.moved
    }
}
