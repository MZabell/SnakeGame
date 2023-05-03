import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class View extends JPanel {

    public void setList(LinkedList<Snake> list) {
        this.list = list;
    }

    private LinkedList<Snake> list;

    public View() {
        setPreferredSize(new Dimension(800, 800));
        setLayout(null);
        setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);

        for (Snake s : list) {
            g2d.setColor(s.getColor());
            g2d.fill(s);
        }
    }
}
