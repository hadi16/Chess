package chess;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Class: Piece
 * Represents a chess piece in the game.
 *
 * @author Alex Hadi
 * @version May 2019
 */
public class Piece implements Serializable {
    private final int player;
    private boolean moved;

    @Nonnull private PieceType pieceType; // Can only change with pawn promotion.

    /**
     * Constructor: Piece
     * Creates a new chess piece in the game.
     *
     * @param pieceType The piece type (as an instance of the PieceType enumeration).
     * @param player The player ID for the piece (0 or 1).
     */
    public Piece(@Nonnull PieceType pieceType, int player) {
        Objects.requireNonNull(pieceType);

        this.pieceType = pieceType;
        this.player = player;
    }

    /**
     * Copy Constructor: Piece
     * Creates a deep copy of the passed in chess piece.
     *
     * @param piece The chess piece to copy.
     */
    public Piece(@Nonnull Piece piece) {
        Objects.requireNonNull(piece);

        pieceType = piece.pieceType;
        player = piece.player;
        moved = piece.moved;
    }

    /**
     * Getter: getPlayer
     * Gets the player ID associated with a specific piece.
     *
     * @return The player ID (0 or 1).
     */
    public int getPlayer() {
        return player;
    }

    /**
     * Getter: getPieceType
     * Gets the piece type of the piece.
     *
     * @return The piece's type (as an instance of the PieceType enumeration).
     */
    @Nonnull
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Setter: setPieceType
     * Sets the piece's type. Only allows pawns to access this (for pawn promotion).
     *
     * @param pieceType The piece type to set the piece to.
     */
    public void setPieceType(@Nonnull PieceType pieceType) {
        Objects.requireNonNull(pieceType);

        // The piece type can only be changed
        // for a pawn (pawn promotion).
        if (this.pieceType != PieceType.PAWN) {
            return;
        }

        this.pieceType = pieceType;
    }

    /**
     * Getter: hasMoved
     * Whether the piece has been moved or not.
     * @return true if the piece has been moved (otherwise false).
     */
    public boolean hasMoved() {
        return moved;
    }

    /**
     * Setter: setMoved
     * Set whether the piece has been moved.
     * @param moved true to set the piece as moved (otherwise false).
     */
    public void setMoved(boolean moved) {
        this.moved = moved;
    }
}