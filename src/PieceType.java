import java.util.*;

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

    public ArrayList<Direction> getLegalDirections(Piece piece) {
        switch (this) {
            case PAWN:
                if (piece.getPlayer() == 0) {
                    return new ArrayList<>(Arrays.asList(Direction.SOUTH, Direction.SOUTHEAST, Direction.SOUTHWEST));
                }
                else {
                    return new ArrayList<>(Arrays.asList(Direction.NORTH, Direction.NORTHEAST, Direction.NORTHWEST));
                }
            case ROOK:
                return new ArrayList<>(Arrays.asList(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST));
            case BISHOP:
                return new ArrayList<>(
                        Arrays.asList(Direction.NORTHEAST, Direction.NORTHWEST, Direction.SOUTHEAST, Direction.SOUTHWEST)
                );
            default:
                return new ArrayList<>(Arrays.asList(Direction.values()));
        }
    }

    public String getUnicodeValue() {
        return unicodeValue;
    }
}