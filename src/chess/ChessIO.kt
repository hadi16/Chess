package chess

import java.awt.HeadlessException
import java.io.*
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.WindowConstants
import javax.swing.filechooser.FileNameExtensionFilter

/**
 * Object: ChessIO
 * Allows a chess state to be saved or opened.
 * Called by the ChessGame class.
 *
 * @author Alex Hadi
 * @version May 2020
 */
object ChessIO {
    /**
     * Helper Method: getFileChooser
     * Gets a new JFileChooser with a filter for text files.
     *
     * @return The JFileChooser instance.
     */
    private fun getFileChooser(): JFileChooser = JFileChooser().apply {
        // Can only open or save a chess state as a .txt file.
        this.fileFilter = FileNameExtensionFilter("Text Files: *.txt", "txt")
    }

    /**
     * Method: saveGame
     * Saves the passed in ChessState to a file.
     * JFileChooser used to allow the user to choose the file location.
     *
     * @param stateToSave The ChessState to save to a file.
     */
    fun saveGame(stateToSave: ChessState) {
        // Create the save dialog.
        val saveDialog = JFrame().apply {
            this.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        }

        // Initialize the JFileChooser.
        val fileChooser = this.getFileChooser().apply {
            this.dialogTitle = "Please choose a save location and name."
        }

        val filename: File
        try {
            // When the user clicks the "OK" button.
            if (fileChooser.showSaveDialog(saveDialog) == JFileChooser.APPROVE_OPTION) {
                filename = fileChooser.selectedFile
                println("The file was successfully created: ${filename.absolutePath}")
                // If the user clicked the cancel option, notify the user.
            } else {
                println("The save operation was cancelled.")
                return
            }
            // If there is an error, print the stack trace and return.
        } catch (e: HeadlessException) {
            e.printStackTrace()
            return
        }

        try {
            val objectOutputStream = ObjectOutputStream(FileOutputStream(filename))

            // GameState object is serialized.
            objectOutputStream.writeObject(stateToSave)

            objectOutputStream.close()
            println("The game state was successfully saved: ${filename.absolutePath}")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Method: openGame
     * Opens a previously saved ChessState from file.
     * JFileChooser used to allow the user to choose the file location.
     *
     * @return The de-serialized ChessState object.
     */
    fun openGame(): ChessState? {
        // Create the open dialog.
        val openDialog = JFrame().apply {
            this.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        }

        // Initialize the JFileChooser
        val fileChooser = this.getFileChooser().apply {
            this.dialogTitle = "Please choose a saved game file to open."
        }

        // Gets the filename if the user selected "OK". Otherwise, returns.
        val filename: File
        if (fileChooser.showOpenDialog(openDialog) == JFileChooser.APPROVE_OPTION) {
            filename = fileChooser.selectedFile

            println("File successfully opened: ${filename.absolutePath}")
        } else {
            println("The open operation was cancelled.")
            return null
        }

        // Attempts to de-serialize the file as a ChessState object.
        try {
            val objectInputStream = ObjectInputStream(FileInputStream(filename))

            val chessState = objectInputStream.readObject() as? ChessState ?: return null

            objectInputStream.close()
            println("The game state was successfully restored!")

            return chessState
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

        return null
    }
}
