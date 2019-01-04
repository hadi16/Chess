import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ChessSetState extends JPanel
        implements ActionListener, MouseListener, MouseMotionListener, ComponentListener {

    private static JFrame mainFrame;
    private ChessRules chessRules = new ChessRules();

    private Map<Point, Piece> board = new HashMap<>();
    private int scaleDim;

    private Point hoverPoint;
    private Point clickedPoint;

    // Counters determine whether to move piece and whose turn it is.
    private int currentTurn;

    public static void main(String[] args) {
        // These commands set up the JFrame size and behavior.
        mainFrame = new JFrame("Chess Game");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 1000);

        ChessSetState theGame = new ChessSetState();
        theGame.setGameSize();

        // game instance is passed into Buttons.
        Buttons gameButtons = new Buttons(theGame);

        mainFrame.setLayout(new BorderLayout());

        mainFrame.add(theGame, BorderLayout.CENTER);
        mainFrame.add(gameButtons, BorderLayout.SOUTH);

        theGame.resetBoard();

        // Displays the canvas for the user.
        mainFrame.setVisible(true);
    }

    public ChessSetState() {
        mainFrame.addComponentListener(this);

        // Mouse listeners are added.
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void setGameSize() {
        // Finds shorter dimension.
        int height = mainFrame.getBounds().height;
        int width = mainFrame.getBounds().width;
        int frameDim = height < width ? height : width;

        // Resets scaleDim and repaints.
        scaleDim = frameDim / (Constants.BOARD_WIDTH + 1);
        for (Map.Entry<Point, Piece> entry : board.entrySet()) {
            Point position = entry.getKey();
            Piece piece = entry.getValue();
            piece.setPosition(new Point(position.x * scaleDim, position.y * scaleDim));

            board.put(entry.getKey(), piece);
        }
        repaint();
    }

    public void componentResized(ComponentEvent c) {
        setGameSize();
    }

    public void componentShown(ComponentEvent c) {
    }

    public void componentMoved(ComponentEvent c) {
    }

    public void componentHidden(ComponentEvent c) {
    }

    private Point getValidPoint(MouseEvent mouseEvent) {
        Point pointClicked = new Point(
                mouseEvent.getX() / scaleDim,
                mouseEvent.getY() / scaleDim
        );

        boolean inBounds = pointClicked.x >= 0 && pointClicked.x <= 7
                        && pointClicked.y >= 0 && pointClicked.y <= 7;

        return inBounds ? pointClicked : null;
    }

    public void mouseMoved(MouseEvent mouseEvent) {
        Point pointClicked = getValidPoint(mouseEvent);
        if (pointClicked == null || !board.containsKey(pointClicked)) {
            hoverPoint = null;
        }
        else {
            hoverPoint = pointClicked;
        }
        repaint();
    }

    public void mouseDragged(MouseEvent mouseEvent) {
    }

    public void actionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()) {
            case "Save": {
                // JFileChooser is utilized to allow the user to choose whichever
                // save location they prefer.
                JFrame saveDialog = new JFrame();
                saveDialog.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Please choose a save location and name.");

                File saveFile;
                // If user clicks "Save", the file path is updated accordingly.
                // Otherwise, the save operation is aborted.
                try {
                    // JFileChooser.APPROVE_OPTION signifies that the user selected a save location.
                    if (fileChooser.showSaveDialog(saveDialog) == JFileChooser.APPROVE_OPTION) {
                        saveFile = fileChooser.getSelectedFile();
                        // CSV file extension is added to ensure file format remains
                        // the same throughout all save files.

                        String path = saveFile.getAbsolutePath();
                        if (!path.endsWith(".csv")) {
                            saveFile = new File(path + ".csv");
                        }

                        System.out.println(
                                "The file was successfully created in the directory: " + saveFile.getAbsolutePath()
                        );
                    }
                } catch (HeadlessException e) {
                    e.printStackTrace();
                }

                // TODO: Finish save method.

                break;
            }
            case "Open": {
                // JFileChooser is utilized to allow the user to choose to open the
                // file from wherever they prefer.
                JFrame openDialog = new JFrame();
                openDialog.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Please choose a CSV file to open.");

                File saveFile;
                // If user selects a file, this is executed.
                if (fileChooser.showOpenDialog(openDialog) == JFileChooser.APPROVE_OPTION) {
                    String path = fileChooser.getSelectedFile().getAbsolutePath();

                    // File is checked to ensure that it has a CSV extension.
                    if (!path.endsWith(".csv")) {
                        System.err.println("The file must have a CSV extension.");
                        return;
                    }

                    saveFile = fileChooser.getSelectedFile();
                    System.out.println("The file was successfully opened: " + saveFile.getAbsolutePath());
                }

                // TODO: Finish open method.

                System.out.println("The game state was successfully restored!");
                repaint();

                break;
            }
            case "New Game":
                resetBoard();
                repaint();
                break;
        }
    }

    public void mouseEntered(MouseEvent m) {
    }

    public void mouseExited(MouseEvent m) {
    }

    public void mouseReleased(MouseEvent m) {
    }

    public void mousePressed(MouseEvent m) {
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        Point clickedPoint = getValidPoint(mouseEvent);
        if (clickedPoint == null || this.clickedPoint == clickedPoint) {
            this.clickedPoint = null;
            repaint();
            return;
        }

        if (this.clickedPoint == null) {
            Piece clickedPiece = board.get(clickedPoint);
            if (clickedPiece == null || currentTurn != clickedPiece.getPlayer()) {
                return;
            }
            this.clickedPoint = clickedPoint;
        } else {
            if (chessRules.isLegalMove(this.clickedPoint, clickedPoint, board)) {
                movePiece(this.clickedPoint, clickedPoint);
            }
            this.clickedPoint = null;
        }

        repaint();
    }

    public void resetBoard() {
        board = new HashMap<>();

        // Player 0's pieces are set.
        for (int i = 0; i < Constants.BOARD_WIDTH; i++) {
            board.put(new Point(i, 1), new Piece(PieceType.PAWN, new Point(i, 1), 0, scaleDim));
        }
        board.put(new Point(0, 0), new Piece(PieceType.ROOK, new Point(0, 0), 0, scaleDim));
        board.put(new Point(7, 0), new Piece(PieceType.ROOK, new Point(7, 0), 0, scaleDim));
        board.put(new Point(1, 0), new Piece(PieceType.KNIGHT, new Point(1, 0), 0, scaleDim));
        board.put(new Point(6, 0), new Piece(PieceType.KNIGHT, new Point(6, 0), 0, scaleDim));
        board.put(new Point(2, 0), new Piece(PieceType.BISHOP, new Point(2, 0), 0, scaleDim));
        board.put(new Point(5, 0), new Piece(PieceType.BISHOP, new Point(5, 0), 0, scaleDim));
        board.put(new Point(3, 0), new Piece(PieceType.QUEEN, new Point(3, 0), 0, scaleDim));
        board.put(new Point(4, 0), new Piece(PieceType.KING, new Point(4, 0), 0, scaleDim));

        // Player 1's pieces are set.
        for (int i = 0; i < Constants.BOARD_WIDTH; i++) {
            board.put(new Point(i, 6), new Piece(PieceType.PAWN, new Point(i, 6), 1, scaleDim));
        }
        board.put(new Point(0, 7), new Piece(PieceType.ROOK, new Point(0, 7), 1, scaleDim));
        board.put(new Point(7, 7), new Piece(PieceType.ROOK, new Point(7, 7), 1, scaleDim));
        board.put(new Point(1, 7), new Piece(PieceType.KNIGHT, new Point(1, 7), 1, scaleDim));
        board.put(new Point(6, 7), new Piece(PieceType.KNIGHT, new Point(6, 7), 1, scaleDim));
        board.put(new Point(2, 7), new Piece(PieceType.BISHOP, new Point(2, 7), 1, scaleDim));
        board.put(new Point(5, 7), new Piece(PieceType.BISHOP, new Point(5, 7), 1, scaleDim));
        board.put(new Point(3, 7), new Piece(PieceType.QUEEN, new Point(3, 7), 1, scaleDim));
        board.put(new Point(4, 7), new Piece(PieceType.KING, new Point(4, 7), 1, scaleDim));

        clickedPoint = null;
        currentTurn = 1;
    }

    private void paintHighlights(Graphics canvas, Board theBoard) {
        if (clickedPoint == null) {
            return;
        }

        theBoard.drawGoldSquare(canvas, clickedPoint);

        for (int i = 0; i < Constants.BOARD_WIDTH; i++) {
            for (int j = 0; j < Constants.BOARD_WIDTH; j++) {
                Point point = new Point(i, j);
                if (chessRules.isLegalMove(clickedPoint, point, board)) {
                    theBoard.drawGreenSquare(canvas, point);
                }
            }
        }
    }

    public void paint(Graphics canvas) {
        // This is essential for the proper painting of the board.
        super.paint(canvas);

        // The board is drawn.
        Board theBoard = new Board(
                new Color[]{
                        new Color(255, 205, 159),
                        new Color(210, 138, 71)
                },
                scaleDim
        );

        theBoard.drawChessBoard(canvas);
        paintHighlights(canvas, theBoard);

        for (Piece piece : board.values()) {
            piece.drawPiece(canvas, scaleDim);
        }

        if (hoverPoint != null) {
            board.get(hoverPoint).drawHoverText(canvas, scaleDim);
        }
    }

    public void movePiece(Point start, Point end) {
        Piece pieceToMove = board.remove(start);
        pieceToMove.setMoved(true);
        pieceToMove.setPosition(new Point(
                end.x * scaleDim, end.y * scaleDim
        ));
        board.put(end, pieceToMove);

        // Checks for pawn promotion.
        int player = pieceToMove.getPlayer();
        if (pieceToMove.getPieceType() == PieceType.PAWN) {
            if ((player == 0 && end.y == 7) || (player == 1 && end.y == 0)) {
                new PawnPromotion(pieceToMove);
            }
        }

        // Change the turn.
        currentTurn = 1 - player;
    }
}