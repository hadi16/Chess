package chess.gui;

import chess.Constants;
import chess.gui.listeners.ChessMenuBarListener;

import javax.swing.*;

/**
 * Class: ChessMenuBar
 * Creates the menu bar for the game's GUI.
 *
 * @author Alex Hadi
 * @version January 2019
 */
public class ChessMenuBar {
    private final JMenuBar menuBar;

    /**
     * Constructor: ChessMenuBar
     * Creates the menu bar and sets it to the private instance variable.
     *
     * @param mainGui The reference to the game's main GUI.
     */
    public ChessMenuBar(ChessGui mainGui) {
        // Required for macOS menu bar functionality
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        // Create a basic "File" menu.
        JMenu fileMenu = new JMenu("File");

        // Create the items in this file menu.
        JMenuItem newMenuItem = new JMenuItem(Constants.NEW_GAME_MENU_TEXT);
        JMenuItem openMenuItem = new JMenuItem(Constants.OPEN_GAME_MENU_TEXT);
        JMenuItem saveMenuItem = new JMenuItem(Constants.SAVE_GAME_MENU_TEXT);

        // Sets each menu item's listener to the same instance of ChessMenuBarListener.
        ChessMenuBarListener chessMenuBarListener = new ChessMenuBarListener(mainGui);
        newMenuItem.addActionListener(chessMenuBarListener);
        openMenuItem.addActionListener(chessMenuBarListener);
        saveMenuItem.addActionListener(chessMenuBarListener);

        // Adds each menu item to the main file menu.
        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);

        // Add menu to the main menubar.
        menuBar = new JMenuBar();
        menuBar.add(fileMenu);
    }

    /**
     * Getter: getMenuBar
     * Gets the reference to the game's menu bar.
     *
     * @return The JMenuBar reference.
     */
    public JMenuBar getMenuBar() {
        return menuBar;
    }
}