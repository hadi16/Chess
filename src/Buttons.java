import javax.swing.*;
/**
 * This program allows a user to move pieces around on a chess board. It
 * restricts moves to only legal chess moves and highlights these moves when the
 * user clicks on a piece. All rules of chess are implemented, including
 * castling and pawn promotion.
 * 
 * This class holds the buttons for the JPanel and their Action Listeners.
 * 
 * @author Alex Hadi
 * @version May 5, 2017
 */
public class Buttons extends JPanel {
	// These global variables store the buttons.
	private JButton save;
	private JButton open;
	private JButton reset;

	/**
	 * This constructor is executed when a new instance of Buttons is created
	 * within the main method of ChessSetState. It sets the values of the
	 * buttons and adds the action listeners.
	 */
	public Buttons(ChessSetState game) {
		save = new JButton("Save");
		this.add(save);
		save.addActionListener(game);

		open = new JButton("Open");
		this.add(open);
		open.addActionListener(game);

		reset = new JButton("New Game");
		this.add(reset);
		reset.addActionListener(game);
	}
}