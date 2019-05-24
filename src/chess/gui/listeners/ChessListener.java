package chess.gui.listeners;

import chess.gui.ChessGui;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Abstract Class: ChessListener
 * The base class for all listeners in the game.
 *
 * @author Alex Hadi
 * @version May 2019
 */
public abstract class ChessListener {
    // All listeners need access to the chess game's GUI.
    @Nonnull protected final ChessGui chessGui;

    /**
     * Protected Constructor: ChessListener
     * Creates a new listener for the game
     * Since this class is abstract, this constructor will only be called by super()
     *
     * @param chessGui The reference to the GUI of the chess game.
     */
    protected ChessListener(@Nonnull ChessGui chessGui) {
        Objects.requireNonNull(chessGui);

        this.chessGui = chessGui;
    }
}