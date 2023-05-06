public class Main {
    public static void main(String[] args) {

        Menu menu = new Menu();
        View view = new View();
        Frame frame = new Frame();
        new Controller(frame, view, menu);
    }
}