package amazon.snakegame;

import java.util.Random;
import java.util.Set;

public class Board {
    private final int rows;
    private final int cols;
    private Position food;
    private final Random random;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.random = new Random();
    }

    public void spawnFood(Set<Position> occupied) {
        Position pos;
        do {
            pos = new Position(random.nextInt(rows), random.nextInt(cols));
        } while (occupied.contains(pos));
        this.food = pos;
    }

    public boolean isOutOfBounds(Position pos) {
        return pos.getRow() < 0 || pos.getRow() >= rows
            || pos.getCol() < 0 || pos.getCol() >= cols;
    }

    public Position getFood() { return food; }
    public int getRows() { return rows; }
    public int getCols() { return cols; }
}

