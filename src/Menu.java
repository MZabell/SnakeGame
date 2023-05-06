import javax.swing.*;
import java.awt.*;

public class Menu extends JPanel {

    public JButton getPlayButton() {
        return playButton;
    }

    private final JButton playButton;

    public Menu() {
        setPreferredSize(new Dimension(800, 800));
        setLayout(null);
        setVisible(true);

        JLabel menuLabel = new JLabel("Snake!");
        menuLabel.setBounds(getWidth() / 2 - 350 / 2 + 20, getHeight() / 4, 350, 100);
        menuLabel.setFont(new Font("Roboto", Font.ITALIC, 100));
        add(menuLabel);

        playButton = new JButton("Play!");
        playButton.setFont(new Font("Roboto", Font.BOLD, 50));
        playButton.setBounds(getWidth() / 2 - 200 / 2, menuLabel.getY() + menuLabel.getHeight() + 50, 200, 100);
        add(playButton);
    }

    @Override
    public int getWidth() {
        return (int) getPreferredSize().getWidth();
    }

    @Override
    public int getHeight() {
        return (int) getPreferredSize().getHeight();
    }
}
