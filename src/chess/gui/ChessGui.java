package chess.gui;

import chess.*;
import chess.action.ChessAction;
import chess.gui.listeners.ChessComponentListener;
import chess.gui.listeners.ChessMouseListener;
import chess.gui.listeners.ChessMouseMotionListener;
import chess.info.ChessGameInfo;
import chess.info.IllegalMoveInfo;
import chess.info.NotYourTurnInfo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Class: ChessGui
 * The GUI in the game.
 *
 * @author Alex Hadi
 * @version May 2019
 */
public class ChessGui extends JPanel {
    // The reference to ChessGame & JFrame (only assigned once).
    @Nonnull private final ChessGame chessGame;
    @Nonnull private final JFrame chessGameFrame = new JFrame("Chess");

    // Maps each Point to a list of legal end points.
    @Nonnull private Map<Point, ArrayList<Point>> currentPlayerLegalMoves = new HashMap<>();

    // The current state of the game.
    @Nonnull private ChessState chessState;

    // The clicked point (for moving pieces) and hover point
    // (for displaying text while the user hovers)
    @Nullable private Point clickedPoint;
    @Nullable private Point hoverPoint;

    // The current scaling factor of the game.
    private int scaleDim;

    /**
     * Constructor: ChessGui
     * Creates a new GUI for the game.
     *
     * @param chessGame The reference to ChessGame.
     */
    public ChessGui(@Nonnull ChessGame chessGame, @Nonnull ChessState chessState) {
        Objects.requireNonNull(chessGame);
        Objects.requireNonNull(chessState);

        this.chessGame = chessGame;
        this.chessState = chessState;

        // Add the mouse listeners (for clicking and hovering)
        addMouseListener(new ChessMouseListener(this));
        addMouseMotionListener(new ChessMouseMotionListener(this));

        // Create the main JFrame with the helper method.
        initChessGameFrame();
        repaint();
    }

    /**
     * Method: initChessGameFrame
     * Initializes the JFrame for the game.
     */
    private void initChessGameFrame() {
        // Sets some basic qualities of the frame.
        chessGameFrame.add(this);
        chessGameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        chessGameFrame.setSize(1000, 1000);

        // Creates & sets the game's menu bar.
        ChessMenuBar chessMenuBar = new ChessMenuBar(this);
        chessGameFrame.setJMenuBar(chessMenuBar.getMenuBar());

        // Add the component listener for resizing functionality
        chessGameFrame.addComponentListener(new ChessComponentListener(this));

        chessGameFrame.setVisible(true);
    }

    public void updateCurrentPlayerLegalMoves() {
        // Resets the legal end points for the GUI.
        currentPlayerLegalMoves = new HashMap<>();
        Map<Point, Piece> currentPlayerPieces = chessState.getPlayerPieces(chessState.getCurrentTurn());
        for (Map.Entry<Point, Piece> entry : currentPlayerPieces.entrySet()) {
            Point point = entry.getKey();
            ChessRules chessRules = new ChessRules(point, chessState);
            currentPlayerLegalMoves.put(
                    point,
                    chessRules.getLegalEndPointsForPosition()
            );
        }
    }

    /**
     * Method: receiveInfo
     * Receives information from the game about illegal moves, etc.
     * Uses this to get an updated ChessState.
     *
     * @param info The info to receive (as a ChessGameInfo object).
     */
    public void receiveInfo(@Nonnull ChessGameInfo info) {
        Objects.requireNonNull(info);

        // An illegal move was attempted.
        if (info instanceof IllegalMoveInfo) {
            System.err.println("You attempted to make an illegal move!");
            return;
        }

        // Tried to play out of turn.
        if (info instanceof NotYourTurnInfo) {
            System.err.println("It is not your turn!");
            return;
        }

        // Makes sure that a ChessState was sent.
        if (!(info instanceof ChessState)) {
            return;
        }

        chessState = (ChessState) info;

        // Update the current player's legal moves.
        updateCurrentPlayerLegalMoves();

        // Will show which player is in check (if applicable).
        displayCheckedPlayerMessage();

        repaint();
    }

    /**
     * Method: displayCheckedPlayerMessage
     * Displays a message dialog to the user about who is in check (if applicable).
     */
    private void displayCheckedPlayerMessage() {
        int checkedPlayer = chessState.getCheckedPlayer();

        // Make sure that there is a player in check.
        if (checkedPlayer == -1) {
            return;
        }

        // Show the checked player dialog to the user.
        JOptionPane.showMessageDialog(
                new JFrame(),
                String.format("Player %d is checked!", checkedPlayer),
                "Warning",
                JOptionPane.WARNING_MESSAGE
        );
    }

    /**
     * Method: sendActionToGame
     * Sends the given action to the main chess game.
     *
     * @param actionToSend The action to send to the game.
     */
    public void sendActionToGame(@Nonnull ChessAction actionToSend) {
        Objects.requireNonNull(actionToSend);

        chessGame.receiveAction(actionToSend);
    }

    /**
     * Method: drawLegalMoves
     * Draws all the legal moves on the board (if the player has clicked a valid point).
     *
     * @param canvas The Graphics reference.
     */
    private void drawLegalMoves(@Nonnull Graphics canvas) {
        Objects.requireNonNull(canvas);

        // Check for legal points to draw.
        ArrayList<Point> legalPoints = currentPlayerLegalMoves.get(clickedPoint);
        if (legalPoints == null) {
            return;
        }

        // Draw each legal end point as a green square
        // (slightly smaller than the chess board squares).
        for (Point point : legalPoints) {
            canvas.setColor(Color.green);
            canvas.fillRect(
                    point.x * scaleDim + (scaleDim / 32),
                    point.y * scaleDim + (scaleDim / 32),
                    15 * scaleDim / 16,
                    15 * scaleDim / 16
            );
        }
    }

    /**
     * Method: drawChessBoardWithSelectedSquare
     * Draws the basic chess board, along with the selected square (if applicable).
     *
     * @param canvas The Graphics reference.
     */
    private void drawChessBoardWithSelectedSquare(@Nonnull Graphics canvas) {
        Objects.requireNonNull(canvas);

        for (int i = 0; i < Constants.BOARD_WIDTH; i++) {
            for (int j = 0; j < Constants.BOARD_WIDTH; j++) {
                // Sets the color of the square to draw.
                if (new Point(i, j).equals(clickedPoint)) {
                    canvas.setColor(Constants.GOLD);
                } else if (i % 2 == 0) {
                    canvas.setColor(j % 2 == 0 ? Constants.FIRST_BOARD_COLOR : Constants.SECOND_BOARD_COLOR);
                } else {
                    canvas.setColor(j % 2 == 0 ? Constants.SECOND_BOARD_COLOR : Constants.FIRST_BOARD_COLOR);
                }

                // Draws each square (uses the scaling factor)
                canvas.fillRect(i * scaleDim, j * scaleDim, scaleDim, scaleDim);
            }
        }
    }

    /**
     * Method: drawHoverText
     * Draws the text when the user hovers over a piece on the board (each piece's type).
     *
     * @param canvas The Graphics object reference.
     * @param pieceSquare The square to draw the hover text on.
     * @param piece The piece that is occupied by that square (used to get the text to draw).
     */
    private void drawHoverText(@Nonnull Graphics canvas, @Nonnull Rectangle pieceSquare, @Nonnull Piece piece) {
        Objects.requireNonNull(canvas);
        Objects.requireNonNull(pieceSquare);
        Objects.requireNonNull(piece);

        // Set the font and color of the font.
        canvas.setFont(new Font("Arial", Font.BOLD, (scaleDim / 4)));
        canvas.setColor(piece.getPlayer() == 0 ? Color.white : Color.black);

        // Get the current FontMetrics (to center the font on the piece).
        Graphics2D graphics2D = (Graphics2D) canvas;
        FontMetrics fontMetrics = graphics2D.getFontMetrics();

        // Gets the text to draw and draws it on the piece square.
        String pieceTypeText = piece.getPieceType().toString();
        canvas.drawString(
                pieceTypeText,
                pieceSquare.x + ((pieceSquare.width - fontMetrics.stringWidth(pieceTypeText)) / 2),
                pieceSquare.y + (
                        ((pieceSquare.height - fontMetrics.getHeight()) / 2) + fontMetrics.getAscent()
                )
        );
    }

    /**
     * Method: drawPieces
     * Draws all of the pieces on the canvas (using Unicode values).
     *
     * @param canvas The Graphics reference.
     */
    private void drawPieces(@Nonnull Graphics canvas) {
        Objects.requireNonNull(canvas);

        // Needed for getting the FontMetrics object.
        Graphics2D graphics2D = (Graphics2D) canvas;

        // Gets a copy of the board, which contains the pieces to draw.
        Map<Point, Piece> board = chessState.getBoardDeepCopy();
        for (Map.Entry<Point, Piece> entry : board.entrySet()) {
            // Gets the position and piece of the HashMap entry.
            Point position = entry.getKey();
            Piece piece = entry.getValue();

            // The square to draw the piece on.
            Rectangle pieceSquare = new Rectangle(
                    position.x * scaleDim,
                    position.y * scaleDim,
                    scaleDim,
                    scaleDim
            );

            // Sets the font and font color.
            canvas.setFont(new Font("Serif", Font.PLAIN, scaleDim));
            canvas.setColor(piece.getPlayer() == 0 ? Color.black : Color.white);

            // Needed to center the piece on the square.
            FontMetrics fontMetrics = graphics2D.getFontMetrics();

            // Gets the text to draw and centers it on the piece square.
            String pieceText = piece.getPieceType().getUnicodeValue();
            graphics2D.drawString(
                    pieceText,
                    pieceSquare.x + ((pieceSquare.width - fontMetrics.stringWidth(pieceText)) / 2),
                    pieceSquare.y + (
                            ((pieceSquare.height - fontMetrics.getHeight()) / 2) + fontMetrics.getAscent()
                    )
            );

            // Draws the hover text for this square (if applicable).
            if (position.equals(hoverPoint)) {
                drawHoverText(canvas, pieceSquare, piece);
            }
        }
    }

    /**
     * Overridden method: paint
     * Called when the canvas needs to repaint. Draws all elements of the GUI.
     *
     * @param canvas The Graphics reference.
     */
    @Override
    public void paint(Graphics canvas) {
        super.paint(canvas);

        // Draws the board, then the legal moves,
        // and finally the pieces (must be in that order)
        drawChessBoardWithSelectedSquare(canvas);
        drawLegalMoves(canvas);
        drawPieces(canvas);
    }

    /**
     * Getter: getScaleDim
     * Gets the current scaling factor of the GUI.
     *
     * @return The current scaling factor of the game's GUI, as an integer.
     */
    public int getScaleDim() {
        return scaleDim;
    }

    /**
     * Setter: setScaleDim
     * Sets the current scaling factor of the GUI.
     *
     * @param scaleDim The scaling factor to set (integer).
     */
    public void setScaleDim(int scaleDim) {
        this.scaleDim = scaleDim;
    }

    /**
     * Getter: getClickedPoint
     * Gets a copy of the current clicked Point.
     *
     * @return A deep copy of the clicked Point.
     */
    @Nonnull
    public Optional<Point> getClickedPoint() {
        // Necessary, since using the copy constructor below.
        if (clickedPoint == null) {
            return Optional.empty();
        }

        // Uses Point's copy constructor.
        return Optional.of(
                new Point(clickedPoint)
        );
    }

    /**
     * Setter: setClickedPoint
     * Sets the currently clicked Point (uses a deep copy).
     *
     * @param clickedPoint The Point to set the currently clicked Point to.
     */
    public void setClickedPoint(@Nullable Point clickedPoint) {
        // Necessary, since using the copy constructor below.
        if (clickedPoint == null) {
            this.clickedPoint = null;
            return;
        }

        // Sets the clicked Point to a deep copy of the passed in value.
        this.clickedPoint = new Point(clickedPoint);
    }

    /**
     * Setter: setHoverPoint
     * Sets the currently hovered over Point (uses a deep copy).
     *
     * @param hoverPoint The Point to set the currently hovered over Point to.
     */
    public void setHoverPoint(@Nullable Point hoverPoint) {
        // Necessary, since using the copy constructor below.
        if (hoverPoint == null) {
            this.hoverPoint = null;
            return;
        }

        // Sets the hovered Point to a deep copy of the passed in value.
        this.hoverPoint = new Point(hoverPoint);
    }

    /**
     * Getter: getGameFrameBounds
     * Gets the dimensions of the JFrame (as a deep copy).
     *
     * @return A deep copy of the JFrame dimensions.
     */
    @Nonnull
    public Rectangle getGameFrameBounds() {
        // Uses Rectangle's copy constructor.
        return new Rectangle(chessGameFrame.getBounds());
    }

    /**
     * Getter: getChessState
     * Gets a copy of the chess state.
     *
     * @return A copy of the chess state.
     */
    @Nonnull
    public ChessState getChessState() {
        // Uses ChessState's copy constructor.
        return new ChessState(chessState);
    }
}