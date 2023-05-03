import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Timer;

public class Controller {

    private final int REFRESH_RATE = 60;
    private Frame frame;
    private View view;
    private Snake snake;
    private Food food;

    private LinkedList<Snake> list = new LinkedList<>();
    public Controller(Frame frame, View view, Snake snake, Food food) {
        this.frame = frame;
        this.snake = snake;
        this.food = food;
        this.view = view;

        this.frame.setContentPane(this.view);
        this.frame.pack();

        createInitialBoard();
        gameLoop();
    }

    private void gameLoop() {
        while (true) {
            long startTime = System.currentTimeMillis();

            moveSnake();

            frame.repaint();

            long timeElapsed = System.currentTimeMillis() - startTime;
            if (timeElapsed < (int) (1f / REFRESH_RATE * 1000) - timeElapsed) {
                try {
                    Thread.sleep((int) (1f / REFRESH_RATE * 1000) - timeElapsed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void createInitialBoard() {
        list.add(snake);

        for (int i = 0; i < 2; i++) {
            list.add(new SnakeTail());
        }

        int posX = 100, posY = 100;

        for (Snake s : list) {
            s.setFrame(posX, posY, 50, 50);
            posX -= s.getWidth() - 1;
        }

        view.setList(list);
    }

    private void moveSnake() {
        for (Snake s : list) {
            s.setDirectionX(5);
            s.setFrame(s.getX() + s.getDirectionX(), s.getY(), s.getWidth(), s.getHeight());
        }
    }

    private enum Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }
}
