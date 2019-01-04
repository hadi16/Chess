package chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class ChessIO {
    // TODO: Finish the FILE IO.
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
                //chessGui.repaint();

                break;
            }
            case "New Game":
                //setDefaultState();
                //chessGui.repaint();
                break;
        }
    }
}
