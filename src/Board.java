import java.awt.*;

public class Board {
    private Color[] colors;
    private int scaleDim;

    public Board(Color[] colors, int scaleDim) {
        this.colors = colors;
        this.scaleDim = scaleDim;
    }

    public void drawChessBoard(Graphics canvas) {
        for (int i = 0; i < Constants.BOARD_WIDTH; i++) {
            for (int j = 0; j < Constants.BOARD_WIDTH; j++) {
                // If statement determines the color of the square.
                if (i % 2 == 0) {
                    canvas.setColor(j % 2 == 0 ? colors[0] : colors[1]);
                } else {
                    canvas.setColor(j % 2 == 0 ? colors[1] : colors[0]);
                }

                // Square is printed to the screen.
                canvas.fillRect(i * scaleDim, j * scaleDim, scaleDim, scaleDim);
            }
        }
    }

    public void drawGoldSquare(Graphics canvas, Point position) {
        // Gold squares are painted to highlight pieces that are clicked.
        canvas.setColor(new Color(255, 215, 0));
        canvas.fillRect(position.x * scaleDim, position.y * scaleDim, scaleDim, scaleDim);
    }

    public void drawGreenSquare(Graphics canvas, Point position) {
        // Squares are drawn over in green to show the user where they can legally move.
        canvas.setColor(Color.green);
        canvas.fillRect(
                position.x * scaleDim + (scaleDim / 32),
                position.y * scaleDim + (scaleDim / 32),
                15 * scaleDim / 16,
                15 * scaleDim / 16
        );
    }
}