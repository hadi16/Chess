package chess.action;

import chess.PieceType;

import javax.annotation.Nonnull;
import java.awt.*;

/**
 * Class: PawnPromotionAction
 * Action in the game triggered by a pawn being promoted to a rook, knight, bishop, or queen.
 * Inherits from ChessMoveAction.
 *
 * @author Alex Hadi
 * @version May 2019
 */
public class PawnPromotionAction extends ChessMoveAction {
    // The promoted piece type can never be reassigned.
    @Nonnull private final PieceType promotedPieceType;

    /**
     * Constructor: PawnPromotionAction
     * Creates a new action to promote a pawn.
     *
     * @param playerId The player's ID.
     * @param startPoint The starting position of the pawn.
     * @param endPoint The ending position of the pawn.
     * @param promotedPieceType The type of piece that the player wishes to promote the pawn to.
     */
    public PawnPromotionAction(int playerId, Point startPoint, Point endPoint, @Nonnull PieceType promotedPieceType) {
        super(playerId, startPoint, endPoint);

        this.promotedPieceType = promotedPieceType;
    }

    /**
     * Getter: getPromotedPieceType
     * Gets the type of piece that the player wishes to promote the pawn to.
     *
     * @return The piece type to promote the pawn to.
     */
    @Nonnull
    public PieceType getPromotedPieceType() {
        return promotedPieceType;
    }
}