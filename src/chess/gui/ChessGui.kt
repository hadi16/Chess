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
import kotlin.properties.Delegates

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
    private val currentPlayerLegalMoves: HashMap<Point, List<Point>> = hashMapOf()

    // The current state of the game.
    var chessState: ChessState = chessState
        get() = ChessState(field)
        private set

    // The clicked point (for moving pieces) and hover point
    // (for displaying text while the user hovers)
    var clickedPoint: Point? = null
        get() {
            field?.let { return Point(it) }

            return null
        }

    var hoverPoint: Point? = null
        get() {
            field?.let { return Point(it) }

            return null
        }

    // The current scaling factor of the game.
    var scaleDim by Delegates.notNull<Int>()

    init {
        // Add the mouse listeners (for clicking and hovering)
        this.addMouseListener(ChessMouseListener(this))
        this.addMouseMotionListener(ChessMouseMotionListener(this))

        // Create the main JFrame with the helper method.
        this.initChessGameFrame()
        this.repaint()
    }

    /**
     * Method: initChessGameFrame
     * Initializes the JFrame for the game.
     */
    private fun initChessGameFrame() {
        // Sets some basic qualities of the frame.
        this.chessGameFrame.add(this)
        this.chessGameFrame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        this.chessGameFrame.setSize(1000, 1000)

        // Creates & sets the game's menu bar.
        val chessMenuBar = ChessMenuBar(this)
        this.chessGameFrame.jMenuBar = chessMenuBar.menuBar

        // Add the component listener for resizing functionality
        this.chessGameFrame.addComponentListener(ChessComponentListener(this))

        this.chessGameFrame.isVisible = true
    }

    fun updateCurrentPlayerLegalMoves() {
        // Resets the legal end points for the GUI.
        this.currentPlayerLegalMoves.clear()
        val currentPlayerPieces = this.chessState.getPlayerPieces(this.chessState.currentTurn)
        for (entry in currentPlayerPieces.entries) {
            val point = entry.key
            val chessRules = ChessRules(point, this.chessState)

            this.currentPlayerLegalMoves[point] = chessRules.getLegalEndPointsForPosition()
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
        when (info) {
            // An illegal move was attempted.
            is IllegalMoveInfo -> {
                System.err.println("You attempted to make an illegal move!")
                return
            }

            // Tried to play out of turn.
            is NotYourTurnInfo -> {
                System.err.println("It is not your turn!")
                return
            }
        }

        this.chessState = if (info is ChessState) info else return

        // Update the current player's legal moves.
        this.updateCurrentPlayerLegalMoves()

        // Will show which player is in check (if applicable).
        this.displayCheckedPlayerMessage()

        this.repaint()
    }

    /**
     * Method: displayCheckedPlayerMessage
     * Displays a message dialog to the user about who is in check (if applicable).
     */
    private fun displayCheckedPlayerMessage() {
        this.chessState.maybeCheckedPlayer?.let {
            val MESSAGE = "Player $it is checked!"
            JOptionPane.showMessageDialog(JFrame(), MESSAGE, "Warning", JOptionPane.WARNING_MESSAGE)
        }
    }

    /**
     * Method: sendActionToGame
     * Sends the given action to the main chess game.
     *
     * @param actionToSend The action to send to the game.
     */
    fun sendActionToGame(actionToSend: ChessAction) = this.chessGame.receiveAction(actionToSend)

    /**
     * Method: drawLegalMoves
     * Draws all the legal moves on the board (if the player has clicked a valid point).
     *
     * @param canvas The Graphics reference.
     */
    private fun drawLegalMoves(canvas: Graphics) {
        // Check for legal points to draw.
        val legalPoints = this.currentPlayerLegalMoves[this.clickedPoint] ?: return

        // Draw each legal end point as a green square
        // (slightly smaller than the chess board squares).
        for (point in legalPoints) {
            canvas.color = Color.green

            canvas.fillRect(
                    point.x * this.scaleDim + (this.scaleDim / 32),
                    point.y * this.scaleDim + (this.scaleDim / 32),
                    15 * this.scaleDim / 16,
                    15 * this.scaleDim / 16
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
        for (i in 0 until Constants.BOARD_WIDTH) {
            for (j in 0 until Constants.BOARD_WIDTH) {
                // Sets the color of the square to draw.
                canvas.color = when {
                    Point(i, j) == this.clickedPoint -> Constants.GOLD

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
                canvas.fillRect(i * this.scaleDim, j * this.scaleDim, this.scaleDim, this.scaleDim)
            }
        }
    }

    private fun getTextPoint(canvas: Graphics, pieceSquare: Rectangle, text: String): Point? {
        val graphics2D = canvas as? Graphics2D ?: return null
        val fontMetrics = graphics2D.fontMetrics

        return Point(
                pieceSquare.x + (pieceSquare.width - fontMetrics.stringWidth(text)) / 2,
                pieceSquare.y + (pieceSquare.height - fontMetrics.height) / 2 + fontMetrics.ascent
        )
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
        canvas.font = Font("Arial", Font.BOLD, (this.scaleDim / 4))
        canvas.color = if (piece.player == 0) Color.white else Color.black

        // Gets the text to draw and draws it on the piece square.
        val pieceText = piece.pieceType.toString()
        this.getTextPoint(canvas, pieceSquare, pieceText)?.let { canvas.drawString(pieceText, it.x, it.y) }
    }

    /**
     * Method: drawPieces
     * Draws all of the pieces on the canvas (using Unicode values).
     *
     * @param canvas The Graphics reference.
     */
    private fun drawPieces(canvas: Graphics) {
        // Gets a copy of the board, which contains the pieces to draw.
        val board = this.chessState.board
        for (entry in board.entries) {
            // Gets the position and piece of the HashMap entry.
            val position = entry.key
            val piece = entry.value

            // The square to draw the piece on.
            val pieceSquare = Rectangle(
                    position.x * this.scaleDim,
                    position.y * this.scaleDim,
                    this.scaleDim,
                    this.scaleDim
            )

            canvas.font = Font("Serif", Font.PLAIN, this.scaleDim)
            canvas.color = if (piece.player == 0) Color.black else Color.white

            // Gets the text to draw and centers it on the piece square.
            val pieceText = piece.pieceType.unicodeValue
            this.getTextPoint(canvas, pieceSquare, pieceText)?.let { canvas.drawString(pieceText, it.x, it.y) }

            // Draws the hover text for this square (if applicable).
            if (position == this.hoverPoint) {
                this.drawHoverText(canvas, pieceSquare, piece)
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
        this.drawChessBoardWithSelectedSquare(canvas)
        this.drawLegalMoves(canvas)
        this.drawPieces(canvas)
    }

    /**
     * Getter: getGameFrameBounds
     * Gets the dimensions of the JFrame (as a deep copy).
     *
     * @return A deep copy of the JFrame dimensions.
     */
    fun gameFrameBounds(): Rectangle = Rectangle(this.chessGameFrame.bounds)
}
