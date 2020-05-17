package chess.gui

import chess.Constants.NEW_GAME_MENU_TEXT
import chess.Constants.OPEN_GAME_MENU_TEXT
import chess.Constants.SAVE_GAME_MENU_TEXT
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

        val fileMenu = JMenu("File")
        val chessMenuBarListener = ChessMenuBarListener(mainGui)

        listOf(NEW_GAME_MENU_TEXT, OPEN_GAME_MENU_TEXT, SAVE_GAME_MENU_TEXT).forEach {
            val menuItem = JMenuItem(it)
            menuItem.addActionListener(chessMenuBarListener)
            fileMenu.add(menuItem)
        }

        // Add menu to the main menubar.
        this.menuBar = JMenuBar()
        this.menuBar.add(fileMenu)
    }
}
