package chess;

import chess.action.ChessMoveAction;
import chess.action.PawnPromotionAction;
import chess.info.ChessGameInfo;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Class: ChessState
 * Represents a state in the chess game.
 *
 * @author Alex Hadi
 * @version May 2019
 */
public class ChessState extends ChessGameInfo implements Serializable {
    @Nonnull private Map<Point, Piece> board = new HashMap<>();

    private int currentTurn;
    private int checkedPlayer; // The player ID of the checked player (or -1 if neither are in check).

    /**
     * Constructor: ChessState
     * Creates a new chess state. Just sets it to the default state
     */
    public ChessState() {
        setDefaultState();
    }

    /**
     * Copy Constructor: ChessState
     * Makes a deep copy of the passed in ChessState.
     *
     * @param originalState The ChessState to copy.
     */
    public ChessState(@Nonnull ChessState originalState) {
        board = originalState.getBoardDeepCopy();
        currentTurn = originalState.currentTurn;
        checkedPlayer = originalState.checkedPlayer;
    }

    /**
     * Method: setDefaultState
     * Resets the state to its default configuration.
     */
    private void setDefaultState() {
        // Player 0's pieces are set.
        for (int i = 0; i < Constants.BOARD_WIDTH; i++) {
            board.put(new Point(i, 1), new Piece(PieceType.PAWN, 0));
        }
        board.put(new Point(0, 0), new Piece(PieceType.ROOK, 0));
        board.put(new Point(1, 0), new Piece(PieceType.KNIGHT, 0));
        board.put(new Point(2, 0), new Piece(PieceType.BISHOP, 0));
        board.put(new Point(3, 0), new Piece(PieceType.QUEEN, 0));
        board.put(new Point(4, 0), new Piece(PieceType.KING, 0));
        board.put(new Point(5, 0), new Piece(PieceType.BISHOP, 0));
        board.put(new Point(6, 0), new Piece(PieceType.KNIGHT, 0));
        board.put(new Point(7, 0), new Piece(PieceType.ROOK, 0));

        // Player 1's pieces are set.
        for (int i = 0; i < Constants.BOARD_WIDTH; i++) {
            board.put(new Point(i, 6), new Piece(PieceType.PAWN, 1));
        }
        board.put(new Point(0, 7), new Piece(PieceType.ROOK, 1));
        board.put(new Point(1, 7), new Piece(PieceType.KNIGHT, 1));
        board.put(new Point(2, 7), new Piece(PieceType.BISHOP, 1));
        board.put(new Point(3, 7), new Piece(PieceType.QUEEN, 1));
        board.put(new Point(4, 7), new Piece(PieceType.KING, 1));
        board.put(new Point(5, 7), new Piece(PieceType.BISHOP, 1));
        board.put(new Point(6, 7), new Piece(PieceType.KNIGHT, 1));
        board.put(new Point(7, 7), new Piece(PieceType.ROOK, 1));

        // The white player always starts first.
        currentTurn = 1;

        // Neither player is initially in check.
        checkedPlayer = -1;
    }

    /**
     * Method: getNextState
     * Gets the next state of the game after a move is played in the game.
     *
     * @param moveAction The move to play in the game.
     * @return The next state in the game after the given move is played.
     */
    @Nonnull
    public ChessState getNextState(@Nonnull ChessMoveAction moveAction) {
        // Get the starting and ending position for the piece.
        Point startPosition = moveAction.getStartPosition();
        Point endPosition = moveAction.getEndPosition();

        // Gets a new ChessState to return (as a deep copy).
        ChessState nextState = new ChessState(this);

        // Get the piece to move and sets it as moved.
        nextState.removeAndReturnPieceAtPosition(startPosition).ifPresent(pieceToMove -> {
            pieceToMove.setMoved(true);

            // If the action involved pawn promotion, update the piece type.
            if (moveAction instanceof PawnPromotionAction) {
                PawnPromotionAction pawnPromotionAction = (PawnPromotionAction) moveAction;
                pieceToMove.setPieceType(pawnPromotionAction.getPromotedPieceType());
            }

            // Set the piece in the master state.
            nextState.setBoardAtPosition(endPosition, pieceToMove);
        });

        // Change the turn.
        nextState.changeTurn();

        // Reset the checked player.
        CheckTester checkTester = new CheckTester(nextState);
        nextState.checkedPlayer = checkTester.getCheckedPlayer();

        return nextState;
    }

    /**
     * Helper Method: setBoardAtPosition
     * Sets the given position in the board to a piece.
     *
     * @param position The position in the board to set.
     * @param piece The piece to set the board's position to.
     */
    private void setBoardAtPosition(@Nonnull Point position, @Nonnull Piece piece) {
        // Position must always be in bounds.
        if (!Helpers.positionInBounds(position)) {
            return;
        }

        board.put(position, piece);
    }

    /**
     * Helper Method: removeAndReturnPieceAtPosition
     * Removes the Piece at the given position (or null).
     *
     * @param position The position to remove the piece at.
     * @return The Piece at this position (or null).
     */
    @Nonnull
    private Optional<Piece> removeAndReturnPieceAtPosition(@Nonnull Point position) {
        Piece removedPiece = board.remove(position);
        if (removedPiece == null) {
            return Optional.empty();
        } else {
            return Optional.of(removedPiece);
        }
    }

    /**
     * Helper Method: changeTurn
     * Changes the current turn to the other player ID (0->1 or 1->0)
     */
    private void changeTurn() {
        currentTurn = 1 - currentTurn;
    }

    /**
     * Method: isMyTurn
     * Determines whether it is the given player's turn or not.
     *
     * @param player The player ID to check (0 or 1).
     * @return true if it is their turn (otherwise false).
     */
    public boolean isMyTurn(int player) {
        return currentTurn == player;
    }

    /**
     * Method: positionInBoard
     * Determines if a given position is in the board.
     *
     * @param position The position to check.
     * @return true if the position is in the board (otherwise false).
     */
    public boolean positionInBoard(@Nonnull Point position) {
        return board.containsKey(position);
    }

    /**
     * Getter: getPieceAtPosition
     * Gets a copy of a piece (value of the HashMap) at a position (key of the HashMap).
     *
     * @param position The position to query.
     * @return The piece at this position.
     */
    @Nonnull
    public Optional<Piece> getPieceAtPosition(@Nonnull Point position) {
        Piece pieceToGet = board.get(position);
        // Required, since using copy constructor below.
        if (pieceToGet == null) {
            return Optional.empty();
        }

        // Uses Piece's copy constructor to get deep copy.
        return Optional.of(
                new Piece(pieceToGet)
        );
    }

    /**
     * Getter: getBoardDeepCopy
     * Gets a deep copy of the board.
     *
     * @return A deep copy of the board.
     */
    @Nonnull
    public Map<Point, Piece> getBoardDeepCopy() {
        Map<Point, Piece> boardCopy = new HashMap<>();

        // Loops through the entire current board to copy it.
        for (Map.Entry<Point, Piece> entry : board.entrySet()) {
            boardCopy.put(
                    new Point(entry.getKey()),
                    new Piece(entry.getValue())
            );
        }

        return boardCopy;
    }

    /**
     * Getter: getKingPosition
     * Gets the position of the given player's king (to determine whether in check)
     *
     * @param playerId The ID of the player (0 or 1).
     * @return A deep copy of the position of the player's king.
     */
    @Nonnull
    public Optional<Point> getKingPosition(int playerId) {
        // Loops through the whole board, finds the player's king,
        // and returns a deep copy of the king's position.
        for (Map.Entry<Point, Piece> entry : board.entrySet()) {
            Piece piece = entry.getValue();
            if (piece.getPieceType() == PieceType.KING && piece.getPlayer() == playerId) {
                return Optional.of(
                        new Point(entry.getKey())
                );
            }
        }
        return Optional.empty();
    }

    /**
     * Getter: getPlayerPieces
     * Gets a copy of the positions and pieces associated with the given player.
     *
     * @param playerId The player ID to check.
     * @return A HashMap mapping each position of the player's pieces to the Piece.
     */
    @Nonnull
    public Map<Point, Piece> getPlayerPieces(int playerId) {
        Map<Point, Piece> playerPieces = new HashMap<>();

        // Loops through the board, finds if the piece belongs to the given player ID,
        // and makes a deep copy of each position and piece if it is.
        for (Map.Entry<Point, Piece> entry : board.entrySet()) {
            Piece piece = entry.getValue();
            if (piece.getPlayer() == playerId) {
                playerPieces.put(
                        new Point(entry.getKey()),
                        new Piece(piece)
                );
            }
        }
        return playerPieces;
    }

    /**
     * Getter: getCurrentTurn
     * Gets the player ID for the player whose turn it currently is.
     *
     * @return The player ID representing the current turn (0 or 1).
     */
    public int getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Getter: getCheckedPlayer
     * Gets the ID of the currently checked player.
     * A value of -1 denotes that neither player is in check.
     *
     * @return The ID of the checked player (-1, 0, or 1).
     */
    public int getCheckedPlayer() {
        return checkedPlayer;
    }
}