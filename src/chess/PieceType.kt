package chess

/**
 * Enumeration: PieceType
 * Represents each possible chess piece in the game & its corresponding unicode value.
 *
 * @author Alex Hadi
 * @version May 2020
 */
enum class PieceType(val unicodeValue: String) {
    /* Each piece type is associated with its proper unicode value. */
    PAWN("\u2659"),
    ROOK("\u2656"),
    KNIGHT("\u2658"),
    BISHOP("\u2657"),
    QUEEN("\u2655"),
    KING("\u2654");
}
