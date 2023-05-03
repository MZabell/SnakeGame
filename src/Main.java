public class Main {
    public static void main(String[] args) {

        Snake snake = new Snake();
        Food food = new Food();
        View view = new View();
        Frame frame = new Frame();
        Controller controller = new Controller(frame, view, snake, food);
    }
}