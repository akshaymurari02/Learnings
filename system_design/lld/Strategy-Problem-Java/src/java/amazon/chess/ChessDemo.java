package amazon.chess;

public class ChessDemo {

    public static void main(String[] args) {
        Player white = new Player("Alice", Color.WHITE);
        Player black = new Player("Bob", Color.BLACK);

        GameService game = new GameService(white, black);
        game.getBoard().printBoard();

        // Example: Scholar's Mate
        System.out.println("\n--- Scholar's Mate Demo ---\n");

        game.makeMove(new Position(6, 4), new Position(4, 4)); // e2->e4
        game.makeMove(new Position(1, 4), new Position(3, 4)); // e7->e5
        game.makeMove(new Position(7, 5), new Position(4, 2)); // Bf1->c4
        game.makeMove(new Position(1, 1), new Position(3, 1)); // b7->b5 (blunder)
        game.makeMove(new Position(7, 3), new Position(3, 7)); // Qd1->h5
        game.makeMove(new Position(1, 2), new Position(2, 2)); // c7->c6 (blunder)

        GameStatus status = game.makeMove(new Position(3, 7), new Position(1, 5)); // Qh5->f7#

        game.getBoard().printBoard();
        System.out.println("Game Status: " + status);
        System.out.println("Winner: " + (status == GameStatus.CHECKMATE ?
            (game.getCurrentPlayer().getColor() == Color.WHITE ? "Black" : "White") : "None"));
    }
}

