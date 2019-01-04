package chess.listeners;

import chess.ChessGui;
import chess.ChessState;

public abstract class ChessListener {
    protected ChessState chessState;
    protected ChessGui chessGui;

    public ChessListener(ChessState chessState, ChessGui chessGui) {
        this.chessState = chessState;
        this.chessGui = chessGui;
    }
}
