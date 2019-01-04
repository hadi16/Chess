package chess;

public class ChessGame {
    public static void main(String[] args) {
        ChessGame chessGame = new ChessGame();
    }

    public ChessGame() {
        ChessState chessState = new ChessState();
        chessState.setDefaultState();

        ChessGui chessGui = new ChessGui(chessState);
        chessGui.repaint();
    }
}
