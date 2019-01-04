package chess;

import java.awt.*;

public class Helpers {
    public static int BOARD_WIDTH = 8;

    public static Color FIRST_BOARD_COLOR = new Color(255, 205, 159);
    public static Color SECOND_BOARD_COLOR = new Color(210, 138, 71);
    public static Color GOLD = new Color(255, 215, 0);

    public static boolean positionInBounds(Point position) {
        return position.x >= 0 && position.x < Helpers.BOARD_WIDTH &&
               position.y >= 0 && position.y < Helpers.BOARD_WIDTH;
    }
}
