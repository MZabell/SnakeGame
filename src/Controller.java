import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

public class Controller {

    private static final int REFRESH_RATE = 144;
    private final Frame frame;
    private final View view;
    private final Menu menu;
    private Snake snake;
    private Food food;
    private Direction direction;
    private boolean isRunning, isMoveBlocking, isFoodAvailable;
    private int queuedAction, score, PBScore;
    private LinkedList<Snake> list;
    private Thread gameThread;

    public Controller(Frame frame, View view, Menu menu) {
        this.frame = frame;
        this.view = view;
        this.menu = menu;

        this.frame.getContentPane().add(this.menu);
        this.frame.pack();

        PBScore = 0;

        this.view.addKeyListener(new KeyManager());
        this.menu.getPlayButton().addActionListener(e -> {
            gameThread = new Thread(() -> {
                frame.getContentPane().removeAll();
                frame.getContentPane().add(view);
                view.requestFocusInWindow();
                isRunning = true;
                createInitialBoard();
                gameLoop();
            });
            gameThread.start();
        });
    }

    private void gameLoop() {
        while (isRunning) {
            long startTime = System.currentTimeMillis();

            if (!isFoodAvailable) {
                spawnFood();
            }

            moveSnake();
            checkCollisions();
            updateScore();
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
        list = new LinkedList<>();
        list.add(snake);

        for (int i = 0; i < 2; i++) {
            posX -= 40;
            list.add(new SnakeTail(posX, posY));
        }

        isMoveBlocking = false;
        isFoodAvailable = false;
        score = 0;
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

    private synchronized void performAction(int keyCode) {
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
        if (!isRunning) {
            restart();
        }
    }

    private void spawnFood() {
        Random random = new Random();
        food = new Food(random.nextInt(view.getWidth() - 40), random.nextInt(view.getHeight() - 40));

        for (Snake s : list) {
            if (food.intersects(s.getBounds2D()))
                spawnFood();
        }

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
            score++;
        }

        if (snake.getX() < 0 || snake.getX() + snake.getWidth() > view.getWidth() || snake.getY() < 0 || snake.getY() + snake.getHeight() > view.getHeight()) {
            gameOver();
        }

        ListIterator<Snake> iterator = list.listIterator(3);
        while (iterator.hasNext()) {
            if (snake.intersects(iterator.next().getBounds2D())) {
                gameOver();
            }
        }
    }

    private void updateScore() {
        if (score > PBScore)
            PBScore = score;

        view.getScoreLabel().setText("Score: " + score);
        view.getPBLabel().setText("Personal Best: " + PBScore);
    }

    private void gameOver() {
        isRunning = false;
        view.getGameOverLabel().setVisible(true);
        view.getRestartLabel().setVisible(true);
    }

    private void restart() {
        view.getGameOverLabel().setVisible(false);
        view.getRestartLabel().setVisible(false);

        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        gameThread = new Thread(() -> {
            isRunning = true;
            createInitialBoard();
            gameLoop();
        });
        gameThread.start();
    }

    private enum Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN
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
