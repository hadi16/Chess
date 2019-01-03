public enum PieceType {
    PAWN("\u2659"),
    ROOK("\u2656"),
    KNIGHT("\u2658"),
    BISHOP("\u2657"),
    QUEEN("\u2655"),
    KING("\u2654");

    private String unicodeValue;

    PieceType(String unicodeValue) {
        this.unicodeValue = unicodeValue;
    }

    public String getUnicodeValue() {
        return unicodeValue;
    }
}