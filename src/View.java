import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class View extends JPanel {

    public void setList(LinkedList<Snake> list) {
        this.list = list;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public JLabel getScoreLabel() {
        return scoreLabel;
    }

    public JLabel getPBLabel() {
        return PBLabel;
    }

    public JLabel getGameOverLabel() {
        return gameOverLabel;
    }

    public JLabel getRestartLabel() {
        return restartLabel;
    }

    private LinkedList<Snake> list;
    private Food food;
    private final JLabel scoreLabel;
    private final JLabel PBLabel;
    private final JLabel gameOverLabel;
    private final JLabel restartLabel;

    public View() {
        setPreferredSize(new Dimension(800, 800));
        setLayout(null);
        setVisible(true);
        setFocusable(true);

        scoreLabel = new JLabel("Score: ");
        scoreLabel.setBounds(getWidth() / 2 - 100, 10, 100, 20);
        scoreLabel.setFont(new Font("Roboto", Font.BOLD, 20));
        add(scoreLabel);

        PBLabel = new JLabel("Personal Best: ");
        PBLabel.setBounds(scoreLabel.getX() + scoreLabel.getWidth() + 10, 10, 170, 20);
        PBLabel.setFont(new Font("Roboto", Font.BOLD, 20));
        add(PBLabel);

        gameOverLabel = new JLabel("Game Over!");
        gameOverLabel.setBounds(getWidth() / 2 - 600 / 2, getHeight() / 2 - 100 / 2, 600, 100);
        gameOverLabel.setFont(new Font("Roboto", Font.BOLD, 100));
        gameOverLabel.setVisible(false);
        add(gameOverLabel);

        restartLabel = new JLabel("Press any key to restart");
        restartLabel.setBounds(getWidth() / 2 - 450 / 2, gameOverLabel.getY() + gameOverLabel.getHeight() + 50, 450, 50);
        restartLabel.setFont(new Font("Roboto", Font.ITALIC, 40));
        restartLabel.setVisible(false);
        add(restartLabel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);

        if (list != null) {
            for (Snake s : list) {
                g2d.setColor(s.getColor());
                g2d.fill(s);
            }
        }

        if (food != null) {
            g2d.setColor(food.getColor());
            g2d.fill(food);
        }
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
