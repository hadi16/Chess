import javax.swing.*;

/**
 * This program allows a user to move pieces around on a chess board. It
 * restricts moves to only legal chess moves and highlights these moves when the
 * user clicks on a piece. All rules of chess are implemented, including
 * castling and pawn promotion.
 * 
 * This class stores the alerts pane and updates the text on the alerts pane.
 * 
 * @author Alex Hadi
 * @version May 5, 2017
 */
public class AlertBoard extends JPanel {
	private JTextArea alerts = new JTextArea(2, 88);

	/**
	 * This constructor adds the JTextArea to the JPanel. It also prevents the
	 * user from being able to edit the JTextArea.
	 */
	public AlertBoard() {
		alerts.setEditable(false);
		this.add(alerts);
	}

	/**
	 * This method updates the alerts pane based on the value of option. Option
	 * 3 allows the message to be set to a custom string s.
	 */
	public void updateAlerts(int option, String s) {
		// The switch statement determines what to set the alert text to.
		switch (option) {
		// This is the welcome message.
		case 0:
			alerts.setText("Welcome to Chess. This is the alerts pane. Alerts will print here.");
			break;

		// This alert is printed when a user attempts to take their own piece.
		case 1:
			alerts.setText("You can't take your own piece!");
			break;

		// This alert is printed when the user attempts to make an invalid move.
		case 2:
			alerts.setText("This is not a valid move!");
			break;

		// This alert tells the user which piece is selected. It is based on the
		// passed in piece identifier.
		case 3:
			String pieceName = "";
			if (s.equals("1")) {
				pieceName = "pawn";
			} else if (s.equals("2")) {
				pieceName = "rook";
			} else if (s.equals("3")) {
				pieceName = "knight";
			} else if (s.equals("4")) {
				pieceName = "bishop";
			} else if (s.equals("5")) {
				pieceName = "queen";
			} else if (s.equals("6")) {
				pieceName = "king";
			}
			alerts.setText("A " + pieceName + " is selected.");
			break;

		// This is the custom alert messsage.
		case 4:
			alerts.setText(s);
			break;
		}
	}
}