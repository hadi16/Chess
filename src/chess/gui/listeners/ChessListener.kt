package chess.gui.listeners

import chess.gui.ChessGui

/**
 * Abstract Class: ChessListener
 * The base class for all listeners in the game.
 *
 * @author Alex Hadi
 * @version July 2019
 */
abstract class ChessListener {
    // All listeners need access to the chess game's GUI.
    protected val chessGui: ChessGui

    /**
     * Protected Constructor: ChessListener
     * Creates a new listener for the game
     * Since this class is abstract, this constructor will only be called by super()
     *
     * @param chessGui The reference to the GUI of the chess game.
     */
    constructor(chessGui: ChessGui) {
        this.chessGui = chessGui
    }
}
