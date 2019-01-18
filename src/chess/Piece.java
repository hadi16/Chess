package chess;

import java.io.Serializable;

/**
 * Class: Piece
 * Represents a chess piece in the game.
 *
 * @author Alex Hadi
 * @version January 2019
 */
public class Piece implements Serializable {
    // The piece's player (can never be reassigned).
    private final int player;

    // The piece type and whether the piece has moved
    private PieceType pieceType; // Can only change with pawn promotion.
    private boolean moved;

    /**
     * Constructor: Piece
     * Creates a new chess piece in the game.
     *
     * @param pieceType The piece type (as an instance of the PieceType enumeration).
     * @param player The player ID for the piece (0 or 1).
     */
    public Piece(PieceType pieceType, int player) {
        this.pieceType = pieceType;
        this.player = player;
    }

    /**
     * Copy Constructor: Piece
     * Creates a deep copy of the passed in chess piece.
     *
     * @param piece The chess piece to copy.
     */
    public Piece(Piece piece) {
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
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Setter: setPieceType
     * Sets the piece's type. Only allows pawns to access this (for pawn promotion).
     *
     * @param pieceType The piece type to set the piece to.
     */
    public void setPieceType(PieceType pieceType) {
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