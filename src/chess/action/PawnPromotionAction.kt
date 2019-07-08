package chess.action

import chess.PieceType
import java.awt.Point

/**
 * Class: PawnPromotionAction
 * Action in the game triggered by a pawn being promoted to a rook, knight, bishop, or queen.
 * Inherits from ChessMoveAction.
 *
 * @author Alex Hadi
 * @version July 2019
 */
class PawnPromotionAction : ChessMoveAction {
    // The promoted piece type can never be reassigned.
    val promotedPieceType: PieceType

    /**
     * Constructor: PawnPromotionAction
     * Creates a new action to promote a pawn.
     *
     * @param playerId The player's ID.
     * @param startPoint The starting position of the pawn.
     * @param endPoint The ending position of the pawn.
     * @param promotedPieceType The type of piece that the player wishes to promote the pawn to.
     */
    constructor(
            playerId: Int, startPoint: Point, endPoint: Point, promotedPieceType: PieceType
    ) : super(playerId, startPoint, endPoint) {
        this.promotedPieceType = promotedPieceType
    }
}
