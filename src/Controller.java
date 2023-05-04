import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

public class Controller {

    private final int REFRESH_RATE = 144;
    private Frame frame;
    private View view;
    private Snake snake;
    private Food food;
    private Direction direction;
    private boolean isMoveBlocking;
    private int queuedAction;
    boolean isFoodAvailable;

    private LinkedList<Snake> list = new LinkedList<>();
    public Controller(Frame frame, View view) {
        this.frame = frame;
        this.view = view;

        this.frame.setContentPane(this.view);
        this.frame.pack();

        view.addKeyListener(new KeyManager());

        isMoveBlocking = false;
        isFoodAvailable = false;

        createInitialBoard();
        gameLoop();
    }

    private void gameLoop() {
        while (true) {
            long startTime = System.currentTimeMillis();

            if (!isFoodAvailable) {
                spawnFood();
            }

            moveSnake();
            checkCollisions();
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
        int posX = 100, posY = 100;
        snake = new Snake(posX, posY);
        list.add(snake);

        for (int i = 0; i < 2; i++) {
            posX -= 40;
            list.add(new SnakeTail(posX, posY));
        }

        direction = Direction.RIGHT;

        view.setList(list);
    }

    private void moveSnake() {
            switch (direction) {
                case LEFT -> {
                    snake.setDirectionX(-1);
                    snake.setDirectionY(0);
                }
                case RIGHT -> {
                    snake.setDirectionX(1);
                    snake.setDirectionY(0);
                }
                case UP -> {
                    snake.setDirectionX(0);
                    snake.setDirectionY(-1);
                }
                case DOWN -> {
                    snake.setDirectionX(0);
                    snake.setDirectionY(1);
                }
            }
            snake.setFrame(snake.getX() + snake.getDirectionX(), snake.getY() + snake.getDirectionY(), snake.getWidth(), snake.getHeight());


            ListIterator<Snake> iterator = list.listIterator(1);
            while (iterator.hasNext()) {
                Snake prev = iterator.previous();
                iterator.next();
                Snake next = iterator.next();
                if (next.getX() == prev.getX() || next.getY() == prev.getY()) {
                    next.setDirectionX(prev.getDirectionX());
                    next.setDirectionY(prev.getDirectionY());
                    if (list.indexOf(next) == 1) {
                        isMoveBlocking = false;
                        performAction(queuedAction);
                        queuedAction = 0;
                    }
                }
                next.setFrame(next.getX() + next.getDirectionX(), next.getY() + next.getDirectionY(), next.getWidth(), next.getHeight());
            }
    }

    private enum Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN;
    }

    private void performAction(int keyCode) {
        if (!isMoveBlocking) {
            switch (keyCode) {
                case KeyEvent.VK_LEFT -> {
                    if (direction != Direction.RIGHT)
                        direction = Direction.LEFT;
                }
                case KeyEvent.VK_RIGHT -> {
                    if (direction != Direction.LEFT)
                        direction = Direction.RIGHT;
                }
                case KeyEvent.VK_UP -> {
                    if (direction != Direction.DOWN)
                        direction = Direction.UP;
                }
                case KeyEvent.VK_DOWN -> {
                    if (direction != Direction.UP)
                        direction = Direction.DOWN;
                }
                default -> {
                }
            }
            isMoveBlocking = true;
        } else
            queuedAction = keyCode;
    }

    private void spawnFood() {
        Random random = new Random();
        food = new Food();
        food.setFrame(random.nextInt(view.getWidth() - 40), random.nextInt(view.getHeight() - 40), 40, 40);
        view.setFood(food);
        isFoodAvailable = true;
    }

    private void checkCollisions() {
        if (snake.intersects(food.getBounds2D())) {
            switch (list.getLast().getDirectionX()) {
                case 1 -> list.addLast(new SnakeTail((int) list.getLast().getX() - 40, (int) list.getLast().getY()));
                case -1 -> list.addLast(new SnakeTail((int) list.getLast().getX() + 40, (int) list.getLast().getY()));
            }
            switch (list.getLast().getDirectionY()) {
                case 1 -> list.addLast(new SnakeTail((int) list.getLast().getX(), (int) list.getLast().getY() - 40));
                case -1 -> list.addLast(new SnakeTail((int) list.getLast().getX(), (int) list.getLast().getY() + 40));
            }
            isFoodAvailable = false;
        }

        if (snake.getX() < 0 || snake.getX() + snake.getWidth() > view.getWidth() || snake.getY() < 0 || snake.getY() + snake.getHeight() > view.getHeight()) {
            System.out.println("Game Over!");
        }
    }

    private class KeyManager implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            // ignore
        }

        @Override
        public void keyPressed(KeyEvent e) {
           performAction(e.getKeyCode());
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // ignore
        }
    }
}
