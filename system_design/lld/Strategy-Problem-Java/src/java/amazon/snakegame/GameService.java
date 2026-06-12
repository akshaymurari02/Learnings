package amazon.snakegame;

import java.util.HashSet;

public class GameService {

    private final Board board;
    private final Snake snake;
    private GameStatus status;
    private int score;

    public GameService(int rows, int cols) {
        this.board = new Board(rows, cols);
        Position start = new Position(rows / 2, cols / 2);
        this.snake = new Snake(start);
        this.status = GameStatus.IN_PROGRESS;
        this.score = 0;
        board.spawnFood(new HashSet<>(snake.getBody()));
    }

    public GameStatus changeDirection(Direction direction) {
        if (status == GameStatus.GAME_OVER) {
            throw new RuntimeException("Game is over. Score: " + score);
        }
        snake.setDirection(direction);
        return status;
    }

    public GameStatus move() {
        if (status == GameStatus.GAME_OVER) {
            throw new RuntimeException("Game is over. Score: " + score);
        }

        Position nextHead = snake.getNextHead();

        // Wall collision
        if (board.isOutOfBounds(nextHead)) {
            status = GameStatus.GAME_OVER;
            return status;
        }

        // Self collision (check before moving, but exclude current tail
        // since tail will move away — unless eating)
        boolean eating = nextHead.equals(board.getFood());

        if (eating) {
            // When eating, tail stays — so check full body
            if (snake.collidesWithSelf(nextHead)) {
                status = GameStatus.GAME_OVER;
                return status;
            }
            snake.grow(nextHead);
            score++;
            board.spawnFood(new HashSet<>(snake.getBody()));
        } else {
            // Tail will move, so temporarily remove it for collision check
            // But simpler: move first removes tail, then check won't include old tail
            if (snake.collidesWithSelf(nextHead)) {
                // But nextHead might equal current tail (which will be removed) — that's OK
                // Re-check: is nextHead in body excluding last element?
                Position tail = snake.getBody().peekLast();
                if (!nextHead.equals(tail)) {
                    status = GameStatus.GAME_OVER;
                    return status;
                }
            }
            snake.move(nextHead);
        }

        return status;
    }

    public int getScore() { return score; }
    public GameStatus getStatus() { return status; }
    public Snake getSnake() { return snake; }
    public Board getBoard() { return board; }

    public void printBoard() {
        HashSet<Position> bodySet = new HashSet<>(snake.getBody());
        for (int r = 0; r < board.getRows(); r++) {
            for (int c = 0; c < board.getCols(); c++) {
                Position p = new Position(r, c);
                if (p.equals(snake.getHead())) {
                    System.out.print("@ ");
                } else if (bodySet.contains(p)) {
                    System.out.print("O ");
                } else if (p.equals(board.getFood())) {
                    System.out.print("* ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
        System.out.println("Score: " + score);
    }
}

