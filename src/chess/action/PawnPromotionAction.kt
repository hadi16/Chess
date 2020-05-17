package chess.action

import chess.PieceType
import java.awt.Point

/**
 * Class: PawnPromotionAction
 * Action in the game triggered by a pawn being promoted to a rook, knight, bishop, or queen.
 * Inherits from ChessMoveAction.
 *
 * @author Alex Hadi
 * @version May 2020
 */
class PawnPromotionAction(
        playerId: Int, startPoint: Point, endPoint: Point, val promotedPieceType: PieceType
) : ChessMoveAction(playerId, startPoint, endPoint)
