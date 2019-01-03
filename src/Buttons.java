import javax.swing.*;

public class Buttons extends JPanel {
    public Buttons(ChessSetState game) {
        JButton save = new JButton("Save");
        add(save);
        save.addActionListener(game);

        JButton open = new JButton("Open");
        add(open);
        open.addActionListener(game);

        JButton reset = new JButton("New Game");
        add(reset);
        reset.addActionListener(game);
    }
}