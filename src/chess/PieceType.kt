package chess

/**
 * Enumeration: PieceType
 * Represents each possible chess piece in the game & its corresponding unicode value.
 *
 * @author Alex Hadi
 * @version July 2019
 */
enum class PieceType {
    // Each piece type is associated with its proper unicode value
    PAWN("\u2659"),
    ROOK("\u2656"),
    KNIGHT("\u2658"),
    BISHOP("\u2657"),
    QUEEN("\u2655"),
    KING("\u2654");

    // A given piece type's unicode value can never change.
    val unicodeValue: String

    /**
     * Constructor: PieceType
     * Creates a new PieceType.
     *
     * @param unicodeValue The unicode value to associate with each piece type.
     */
    constructor(unicodeValue: String) {
        this.unicodeValue = unicodeValue
    }
}
