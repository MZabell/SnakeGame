import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Snake extends Ellipse2D.Double {

    public Snake(int x, int y) {
        super(x, y, 50, 50);
    }

    public int getDirectionX() {
        return directionX;
    }

    public void setDirectionX(int directionX) {
        this.directionX = directionX;
    }

    public int getDirectionY() {
        return directionY;
    }

    public void setDirectionY(int directionY) {
        this.directionY = directionY;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    private int directionX;
    private int directionY;
    private Color color = Color.BLUE;

}
