package amazon.snakegame;

public class SnakeGameDemo {

    public static void main(String[] args) {
        GameService game = new GameService(10, 10);

        System.out.println("=== Snake & Food Game ===\n");
        game.printBoard();

        // Simulate some moves
        System.out.println("\n--- Moving RIGHT ---");
        game.move();
        game.move();
        game.move();
        game.printBoard();

        System.out.println("\n--- Turning DOWN ---");
        game.changeDirection(Direction.DOWN);
        game.move();
        game.move();
        game.printBoard();

        System.out.println("\n--- Turning LEFT ---");
        game.changeDirection(Direction.LEFT);
        game.move();
        game.move();
        game.printBoard();

        System.out.println("\nGame Status: " + game.getStatus());
        System.out.println("Final Score: " + game.getScore());
    }
}

