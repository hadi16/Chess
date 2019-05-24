package chess;

import javax.annotation.Nonnull;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.Objects;
import java.util.Optional;

/**
 * Class: ChessIO
 * Allows a chess state to be saved or opened.
 * Called by the ChessGame class.
 *
 * @author Alex Hadi
 * @version May 2019
 */
public class ChessIO {
    /**
     * Helper Method: getFileChooser
     * Gets a new JFileChooser with a filter for text files.
     *
     * @return The JFileChooser instance.
     */
    @Nonnull
    private JFileChooser getFileChooser() {
        JFileChooser fileChooser = new JFileChooser();

        // Can only open or save a chess state as a .txt file.
        fileChooser.setFileFilter(
                new FileNameExtensionFilter("Text Files: *.txt", "txt")
        );

        return fileChooser;
    }

    /**
     * Method: saveGame
     * Saves the passed in ChessState to a file.
     * JFileChooser used to allow the user to choose the file location.
     *
     * @param stateToSave The ChessState to save to a file.
     */
    public void saveGame(@Nonnull ChessState stateToSave) {
        Objects.requireNonNull(stateToSave);

        // Create the save dialog.
        JFrame saveDialog = new JFrame();
        saveDialog.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Initialize the JFileChooser.
        JFileChooser fileChooser = getFileChooser();
        fileChooser.setDialogTitle("Please choose a save location and name.");

        File filename;
        try {
            // When the user clicks the "OK" button.
            if (fileChooser.showSaveDialog(saveDialog) == JFileChooser.APPROVE_OPTION) {
                filename = fileChooser.getSelectedFile();
                System.out.println(
                        "The file was successfully created: " + filename.getAbsolutePath()
                );
            // If the user clicked the cancel option, notify the user.
            } else {
                System.out.println("The save operation was cancelled.");
                return;
            }
        // If there is an error, print the stack trace and return.
        } catch (HeadlessException e) {
            e.printStackTrace();
            return;
        }

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    new FileOutputStream(filename)
            );

            // GameState object is serialized.
            objectOutputStream.writeObject(stateToSave);

            objectOutputStream.close();
            System.out.println("The game state was successfully saved: " + filename.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method: openGame
     * Opens a previously saved ChessState from file.
     * JFileChooser used to allow the user to choose the file location.
     *
     * @return The de-serialized ChessState object.
     */
    @Nonnull
    public Optional<ChessState> openGame() {
        // Create the open dialog.
        JFrame openDialog = new JFrame();
        openDialog.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Initialize the JFileChooser
        JFileChooser fileChooser = getFileChooser();
        fileChooser.setDialogTitle("Please choose a saved game file to open.");

        // Gets the filename if the user selected "OK". Otherwise, returns.
        File filename;
        if (fileChooser.showOpenDialog(openDialog) == JFileChooser.APPROVE_OPTION) {
            filename = fileChooser.getSelectedFile();

            System.out.println("File successfully opened: " + filename.getAbsolutePath());
        } else {
            System.out.println("The open operation was cancelled.");
            return Optional.empty();
        }

        // Attempts to de-serialize the file as a ChessState object.
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(
                    new FileInputStream(filename)
            );

            ChessState chessState = (ChessState) objectInputStream.readObject();
            objectInputStream.close();
            System.out.println("The game state was successfully restored!");

            return Optional.of(chessState);
        } catch (IOException | ClassNotFoundException e) {
            // If an error occurred, print it to the console and return null.
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
