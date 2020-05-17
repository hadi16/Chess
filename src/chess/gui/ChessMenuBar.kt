package chess.gui

import chess.Constants
import chess.gui.listeners.ChessMenuBarListener
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem

/**
 * Class: ChessMenuBar
 * Creates the menu bar for the game's GUI.
 *
 * @author Alex Hadi
 * @version May 2020
 */
class ChessMenuBar(mainGui: ChessGui) {
    val menuBar: JMenuBar

    init {
        // Required for macOS menu bar functionality
        System.setProperty("apple.laf.useScreenMenuBar", "true")

        // Create a basic "File" menu.
        val fileMenu = JMenu("File")

        // Create the items in this file menu.
        val newMenuItem = JMenuItem(Constants.NEW_GAME_MENU_TEXT)
        val openMenuItem = JMenuItem(Constants.OPEN_GAME_MENU_TEXT)
        val saveMenuItem = JMenuItem(Constants.SAVE_GAME_MENU_TEXT)

        // Sets each menu item's listener to the same instance of ChessMenuBarListener.
        val chessMenuBarListener = ChessMenuBarListener(mainGui)
        newMenuItem.addActionListener(chessMenuBarListener)
        openMenuItem.addActionListener(chessMenuBarListener)
        saveMenuItem.addActionListener(chessMenuBarListener)

        // Adds each menu item to the main file menu.
        fileMenu.add(newMenuItem)
        fileMenu.add(openMenuItem)
        fileMenu.add(saveMenuItem)

        // Add menu to the main menubar.
        menuBar = JMenuBar()
        menuBar.add(fileMenu)
    }
}
