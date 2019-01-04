package chess;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ChessState implements ActionListener {
    private Map<Point, Piece> board = new HashMap<>();
    private int currentTurn;

    private Point clickedPoint;
    private Point hoverPoint;

    // TODO: Move??
    private static ChessGui chessGui;

    public static void main(String[] args) {
        ChessState chessState = new ChessState();
        chessState.setDefaultState();
    }

    public ChessState() {
        chessGui = new ChessGui(this);
    }

    // TODO: Finish the FILE IO.
    @Override
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
                chessGui.repaint();

                break;
            }
            case "New Game":
                setDefaultState();
                chessGui.repaint();
                break;
        }
    }

    private void setDefaultState() {
        board = new HashMap<>();

        // Player 0's pieces are set.
        for (int i = 0; i < Helpers.BOARD_WIDTH; i++) {
            board.put(new Point(i, 1), new Piece(PieceType.PAWN, 0));
        }
        board.put(new Point(0, 0), new Piece(PieceType.ROOK, 0));
        board.put(new Point(7, 0), new Piece(PieceType.ROOK, 0));
        board.put(new Point(1, 0), new Piece(PieceType.KNIGHT, 0));
        board.put(new Point(6, 0), new Piece(PieceType.KNIGHT, 0));
        board.put(new Point(2, 0), new Piece(PieceType.BISHOP, 0));
        board.put(new Point(5, 0), new Piece(PieceType.BISHOP, 0));
        board.put(new Point(3, 0), new Piece(PieceType.QUEEN, 0));
        board.put(new Point(4, 0), new Piece(PieceType.KING, 0));

        // Player 1's pieces are set.
        for (int i = 0; i < Helpers.BOARD_WIDTH; i++) {
            board.put(new Point(i, 6), new Piece(PieceType.PAWN, 1));
        }
        board.put(new Point(0, 7), new Piece(PieceType.ROOK, 1));
        board.put(new Point(7, 7), new Piece(PieceType.ROOK, 1));
        board.put(new Point(1, 7), new Piece(PieceType.KNIGHT, 1));
        board.put(new Point(6, 7), new Piece(PieceType.KNIGHT, 1));
        board.put(new Point(2, 7), new Piece(PieceType.BISHOP, 1));
        board.put(new Point(5, 7), new Piece(PieceType.BISHOP, 1));
        board.put(new Point(3, 7), new Piece(PieceType.QUEEN, 1));
        board.put(new Point(4, 7), new Piece(PieceType.KING, 1));

        clickedPoint = null;
        currentTurn = 1;
    }

    public Point getValidPointOrNull(Point mousePosition) {
        Point pointClicked = new Point(
                mousePosition.x / chessGui.getScaleDim(),
                mousePosition.y / chessGui.getScaleDim()
        );

        return Helpers.positionInBounds(pointClicked) ? pointClicked : null;
    }

    public Map<Point, Piece> getBoardDeepCopy() {
        Map<Point, Piece> boardCopy = new HashMap<>();
        for (Map.Entry<Point, Piece> entry : board.entrySet()) {
            Point point = entry.getKey();
            Piece piece = entry.getValue();

            boardCopy.put(
                    new Point(point.x, point.y), new Piece(piece.getPieceType(), piece.getPlayer())
            );
        }
        return boardCopy;
    }

    public boolean positionInBoard(Point position) {
        return board.containsKey(position);
    }

    public void setBoardAtPosition(Point position, Piece piece) {
        board.put(position, piece);
    }

    public Piece removeAndReturnPieceAtPosition(Point position) {
        return board.remove(position);
    }

    public boolean isMyTurn(int player) {
        return currentTurn == player;
    }

    public void changeTurn() {
        currentTurn = 1 - currentTurn;
    }

    public Point getClickedPoint() {
        if (clickedPoint == null) {
            return null;
        }

        return new Point(clickedPoint.x, clickedPoint.y);
    }

    public void setClickedPoint(Point clickedPoint) {
        if (clickedPoint == null) {
            this.clickedPoint = null;
            return;
        }

        this.clickedPoint = new Point(clickedPoint.x, clickedPoint.y);
    }

    public Point getHoverPoint() {
        if (hoverPoint == null) {
            return null;
        }

        return new Point(hoverPoint.x, hoverPoint.y);
    }

    public void setHoverPoint(Point hoverPoint) {
        if (hoverPoint == null) {
            this.hoverPoint = null;
            return;
        }

        this.hoverPoint = new Point(hoverPoint.x, hoverPoint.y);
    }
}