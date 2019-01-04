package chess.listeners;

import chess.ChessGui;
import chess.ChessState;
import chess.Helpers;

import javax.swing.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class ChessComponentListener extends ChessListener implements ComponentListener {
    public ChessComponentListener(ChessState chessState, ChessGui chessGui) {
        super(chessState, chessGui);
    }

    public void componentResized(ComponentEvent c) {
        setGameSize();
    }

    private void setGameSize() {
        // Finds shorter dimension.
        JFrame chessGameFrame = chessGui.getChessGameFrame();

        int height = chessGameFrame.getBounds().height;
        int width = chessGameFrame.getBounds().width;
        int frameDim = height < width ? height : width;

        // Resets scaleDim and repaints.
        chessGui.setScaleDim(frameDim / (Helpers.BOARD_WIDTH + 1));
        chessGui.repaint();
    }

    public void componentShown(ComponentEvent c) {}

    public void componentMoved(ComponentEvent c) {}

    public void componentHidden(ComponentEvent c) {}
}
