import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Food extends Ellipse2D.Double {

    public Food(int x, int y) {
        super(x, y, 40, 40);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    private Color color = Color.GREEN;


}
