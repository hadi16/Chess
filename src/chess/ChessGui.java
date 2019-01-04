package chess;

import chess.listeners.ChessComponentListener;
import chess.listeners.ChessMouseListener;
import chess.listeners.ChessMouseMotionListener;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ChessGui extends JPanel {
    private JFrame chessGameFrame;
    private ChessState chessState;
    private int scaleDim;

    private ChessRules chessRules = new ChessRules();

    public ChessGui(ChessState chessState) {
        this.chessState = chessState;

        addMouseListener(new ChessMouseListener(chessState, this));
        addMouseMotionListener(new ChessMouseMotionListener(chessState, this));

        chessGameFrame = new JFrame("Chess Game");
        chessGameFrame.add(this);
        chessGameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        chessGameFrame.setSize(1000, 1000);
        chessGameFrame.addComponentListener(new ChessComponentListener(chessState, this));

        chessGameFrame.setVisible(true);
    }

    private void drawChessBoardWithHighlights(Graphics canvas) {
        for (int i = 0; i < Helpers.BOARD_WIDTH; i++) {
            for (int j = 0; j < Helpers.BOARD_WIDTH; j++) {
                if (i % 2 == 0) {
                    canvas.setColor(j % 2 == 0 ? Helpers.FIRST_BOARD_COLOR : Helpers.SECOND_BOARD_COLOR);
                } else {
                    canvas.setColor(j % 2 == 0 ? Helpers.SECOND_BOARD_COLOR : Helpers.FIRST_BOARD_COLOR);
                }

                Point clickedPoint = chessState.getClickedPoint();
                if (clickedPoint != null) {
                    Point currentBoardPosition = new Point(i, j);
                    if (clickedPoint.equals(currentBoardPosition)) {
                        canvas.setColor(Helpers.GOLD);
                    }

                    if (chessRules.isLegalMove(clickedPoint, currentBoardPosition, chessState.getBoardDeepCopy())) {
                        canvas.setColor(Color.green);
                        canvas.fillRect(
                                i * scaleDim + (scaleDim / 32),
                                j * scaleDim + (scaleDim / 32),
                                15 * scaleDim / 16,
                                15 * scaleDim / 16
                        );
                        continue;
                    }
                }

                canvas.fillRect(i * scaleDim, j * scaleDim, scaleDim, scaleDim);
            }
        }
    }

    private void drawPieces(Graphics canvas) {
        for (Map.Entry<Point, Piece> entry : chessState.getBoardDeepCopy().entrySet()) {
            Point position = entry.getKey();
            Piece piece = entry.getValue();

            canvas.setColor(piece.getPlayer() == 0 ? Color.black : Color.white);

            canvas.setFont(new Font("Serif", Font.PLAIN, scaleDim));
            canvas.drawString(
                    piece.getPieceType().getUnicodeValue(),
                    position.x * scaleDim,
                    position.y * scaleDim + 7 * scaleDim / 8
            );
        }
    }

    private void drawHoverText(Graphics canvas) {
        Point hoverPoint = chessState.getHoverPoint();

        if (hoverPoint == null) {
            return;
        }

        Piece pieceUnderHoverText = chessState.getBoardDeepCopy().get(hoverPoint);

        canvas.setColor(pieceUnderHoverText.getPlayer() == 0 ? Color.white : Color.black);
        canvas.setFont(new Font("Arial", Font.BOLD, (scaleDim / 5)));
        canvas.drawString(
                pieceUnderHoverText.getPieceType().toString(),
                hoverPoint.x * scaleDim + scaleDim / 6,
                hoverPoint.y * scaleDim + (3 * scaleDim / 4)
        );
    }

    @Override
    public void paint(Graphics canvas) {
        super.paint(canvas);

        drawChessBoardWithHighlights(canvas);
        drawPieces(canvas);
        drawHoverText(canvas);
    }

    public int getScaleDim() {
        return scaleDim;
    }

    public void setScaleDim(int scaleDim) {
        this.scaleDim = scaleDim;
    }

    public JFrame getChessGameFrame() {
        return chessGameFrame;
    }
}
