package chess.gui

import chess.*
import chess.action.ChessAction
import chess.gui.listeners.ChessComponentListener
import chess.gui.listeners.ChessMouseListener
import chess.gui.listeners.ChessMouseMotionListener
import chess.info.ChessGameInfo
import chess.info.IllegalMoveInfo
import chess.info.NotYourTurnInfo
import java.awt.*
import java.util.*
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.WindowConstants

/**
 * Class: ChessGui
 * The GUI in the game.
 *
 * @author Alex Hadi
 * @version May 2020
 */
class ChessGui(private val chessGame: ChessGame, chessState: ChessState) : JPanel() {
    private val chessGameFrame = JFrame("Chess")

    // Maps each Point to a list of legal end points.
    private val currentPlayerLegalMoves: HashMap<Point, ArrayList<Point>> = hashMapOf()

    // The current state of the game.
    var chessState: ChessState = chessState
        get() = ChessState(field)
        private set

    // The clicked point (for moving pieces) and hover point
    // (for displaying text while the user hovers)
    var clickedPoint: Point? = null
        get() {
            field?.let {
                return Point(it)
            }

            return null
        }

    var hoverPoint: Point? = null
        get() {
            field?.let {
                return Point(it)
            }

            return null
        }

    // The current scaling factor of the game.
    var scaleDim: Int = 1

    init {
        // Add the mouse listeners (for clicking and hovering)
        addMouseListener(ChessMouseListener(this))
        addMouseMotionListener(ChessMouseMotionListener(this))

        // Create the main JFrame with the helper method.
        initChessGameFrame()
        repaint()
    }

    /**
     * Method: initChessGameFrame
     * Initializes the JFrame for the game.
     */
    private fun initChessGameFrame() {
        // Sets some basic qualities of the frame.
        chessGameFrame.add(this)
        chessGameFrame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        chessGameFrame.setSize(1000, 1000)

        // Creates & sets the game's menu bar.
        val chessMenuBar = ChessMenuBar(this)
        chessGameFrame.jMenuBar = chessMenuBar.menuBar

        // Add the component listener for resizing functionality
        chessGameFrame.addComponentListener(ChessComponentListener(this))

        chessGameFrame.isVisible = true
    }

    fun updateCurrentPlayerLegalMoves() {
        // Resets the legal end points for the GUI.
        currentPlayerLegalMoves.clear()
        val currentPlayerPieces = chessState.getPlayerPieces(chessState.currentTurn)
        for (entry in currentPlayerPieces.entries) {
            val point = entry.key
            val chessRules = ChessRules(point, chessState)

            currentPlayerLegalMoves[point] = chessRules.getLegalEndPointsForPosition()
        }
    }

    /**
     * Method: receiveInfo
     * Receives information from the game about illegal moves, etc.
     * Uses this to get an updated ChessState.
     *
     * @param info The info to receive (as a ChessGameInfo object).
     */
    fun receiveInfo(info: ChessGameInfo) {
        // An illegal move was attempted.
        if (info is IllegalMoveInfo) {
            System.err.println("You attempted to make an illegal move!")
            return
        }

        // Tried to play out of turn.
        if (info is NotYourTurnInfo) {
            System.err.println("It is not your turn!")
            return
        }

        // Makes sure that a ChessState was sent.
        if (info !is ChessState) {
            return
        }

        chessState = info

        // Update the current player's legal moves.
        updateCurrentPlayerLegalMoves()

        // Will show which player is in check (if applicable).
        displayCheckedPlayerMessage()

        repaint()
    }

    /**
     * Method: displayCheckedPlayerMessage
     * Displays a message dialog to the user about who is in check (if applicable).
     */
    private fun displayCheckedPlayerMessage() {
        val checkedPlayer = chessState.checkedPlayer

        // Make sure that there is a player in check.
        if (checkedPlayer == -1) {
            return
        }

        // Show the checked player dialog to the user.
        JOptionPane.showMessageDialog(
                JFrame(),
                String.format("Player %d is checked!", checkedPlayer),
                "Warning",
                JOptionPane.WARNING_MESSAGE
        )
    }

    /**
     * Method: sendActionToGame
     * Sends the given action to the main chess game.
     *
     * @param actionToSend The action to send to the game.
     */
    fun sendActionToGame(actionToSend: ChessAction) {
        chessGame.receiveAction(actionToSend)
    }

    /**
     * Method: drawLegalMoves
     * Draws all the legal moves on the board (if the player has clicked a valid point).
     *
     * @param canvas The Graphics reference.
     */
    private fun drawLegalMoves(canvas: Graphics) {
        // Check for legal points to draw.
        val legalPoints = currentPlayerLegalMoves[clickedPoint] ?: return

        // Draw each legal end point as a green square
        // (slightly smaller than the chess board squares).
        for (point in legalPoints) {
            canvas.color = Color.green

            canvas.fillRect(
                    point.x * scaleDim + (scaleDim / 32),
                    point.y * scaleDim + (scaleDim / 32),
                    15 * scaleDim / 16,
                    15 * scaleDim / 16
            )
        }
    }

    /**
     * Method: drawChessBoardWithSelectedSquare
     * Draws the basic chess board, along with the selected square (if applicable).
     *
     * @param canvas The Graphics reference.
     */
    private fun drawChessBoardWithSelectedSquare(canvas: Graphics) {
        Objects.requireNonNull(canvas)

        for (i in 0 until Constants.BOARD_WIDTH) {
            for (j in 0 until Constants.BOARD_WIDTH) {
                // Sets the color of the square to draw.
                canvas.color = when {
                    Point(i, j) == clickedPoint -> {
                        Constants.GOLD
                    }

                    i % 2 == 0 -> {
                        if (j % 2 == 0) {
                            Constants.FIRST_BOARD_COLOR
                        } else {
                            Constants.SECOND_BOARD_COLOR
                        }
                    }

                    else -> {
                        if (j % 2 == 0) {
                            Constants.SECOND_BOARD_COLOR
                        } else {
                            Constants.FIRST_BOARD_COLOR
                        }
                    }
                }

                // Draws each square (uses the scaling factor)
                canvas.fillRect(i * scaleDim, j * scaleDim, scaleDim, scaleDim)
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
    private fun drawHoverText(canvas: Graphics, pieceSquare: Rectangle, piece: Piece) {
        Objects.requireNonNull(canvas)
        Objects.requireNonNull(pieceSquare)
        Objects.requireNonNull(piece)

        // Set the font and color of the font.
        canvas.font = Font("Arial", Font.BOLD, (scaleDim / 4))

        val canvasColor = if (piece.player == 0) Color.white else Color.black
        canvas.color = canvasColor

        // Get the current FontMetrics (to center the font on the piece).
        val graphics2D: Graphics2D = canvas as Graphics2D
        val fontMetrics = graphics2D.fontMetrics

        // Gets the text to draw and draws it on the piece square.
        val pieceTypeText = piece.pieceType.toString()
        canvas.drawString(
                pieceTypeText,
                pieceSquare.x + ((pieceSquare.width - fontMetrics.stringWidth(pieceTypeText)) / 2),
                pieceSquare.y + (((pieceSquare.height - fontMetrics.height) / 2) + fontMetrics.ascent)
        )
    }

    /**
     * Method: drawPieces
     * Draws all of the pieces on the canvas (using Unicode values).
     *
     * @param canvas The Graphics reference.
     */
    private fun drawPieces(canvas: Graphics) {
        // Needed for getting the FontMetrics object.
        val graphics2D = canvas as Graphics2D

        // Gets a copy of the board, which contains the pieces to draw.
        val board = chessState.board
        for (entry in board.entries) {
            // Gets the position and piece of the HashMap entry.
            val position = entry.key
            val piece = entry.value

            // The square to draw the piece on.
            val pieceSquare = Rectangle(
                    position.x * scaleDim,
                    position.y * scaleDim,
                    scaleDim,
                    scaleDim
            )

            // Sets the font and font color.
            canvas.setFont(Font("Serif", Font.PLAIN, scaleDim))

            val canvasColor = if (piece.player == 0) Color.black else Color.white
            canvas.setColor(canvasColor)

            // Needed to center the piece on the square.
            val fontMetrics = graphics2D.fontMetrics

            // Gets the text to draw and centers it on the piece square.
            val pieceText = piece.pieceType.unicodeValue
            graphics2D.drawString(
                    pieceText,
                    pieceSquare.x + ((pieceSquare.width - fontMetrics.stringWidth(pieceText)) / 2),
                    pieceSquare.y + (((pieceSquare.height - fontMetrics.height) / 2) + fontMetrics.ascent)
            )

            // Draws the hover text for this square (if applicable).
            if (position == hoverPoint) {
                drawHoverText(canvas, pieceSquare, piece)
            }
        }
    }

    /**
     * Overridden method: paint
     * Called when the canvas needs to repaint. Draws all elements of the GUI.
     *
     * @param canvas The Graphics reference.
     */
    override fun paint(canvas: Graphics) {
        super.paint(canvas)

        // Draws the board, then the legal moves,
        // and finally the pieces (must be in that order)
        drawChessBoardWithSelectedSquare(canvas)
        drawLegalMoves(canvas)
        drawPieces(canvas)
    }

    /**
     * Getter: getGameFrameBounds
     * Gets the dimensions of the JFrame (as a deep copy).
     *
     * @return A deep copy of the JFrame dimensions.
     */
    fun gameFrameBounds(): Rectangle {
        // Uses Rectangle's copy constructor.
        return Rectangle(chessGameFrame.bounds)
    }
}
