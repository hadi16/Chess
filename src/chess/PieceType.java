package chess;

/**
 * Enumeration: PieceType
 * Represents each possible chess piece in the game & its corresponding unicode value.
 *
 * @author Alex Hadi
 * @version January 2019
 */
public enum PieceType {
    // Each piece type is associated with its proper unicode value
    PAWN("\u2659"),
    ROOK("\u2656"),
    KNIGHT("\u2658"),
    BISHOP("\u2657"),
    QUEEN("\u2655"),
    KING("\u2654");

    // A given piece type's unicode value can never change.
    private final String unicodeValue;

    /**
     * Constructor: PieceType
     * Creates a new PieceType.
     *
     * @param unicodeValue The unicode value to associate with each piece type.
     */
    PieceType(String unicodeValue) {
        this.unicodeValue = unicodeValue;
    }

    /**
     * Getter: getUnicodeValue
     * Gets the unicode value associated with the piece type.
     *
     * @return The Unicode value, as a String.
     */
    public String getUnicodeValue() {
        return unicodeValue;
    }
}