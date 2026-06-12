package amazon.snakegame;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Snake {
    private final Deque<Position> body; // head is first, tail is last
    private final Set<Position> bodySet; // O(1) collision check
    private Direction direction;

    public Snake(Position start) {
        body = new LinkedList<>();
        bodySet = new HashSet<>();
        body.addFirst(start);
        bodySet.add(start);
        direction = Direction.RIGHT;
    }

    public Position getHead() {
        return body.peekFirst();
    }

    public void setDirection(Direction direction) {
        // Prevent reversing into self
        if (this.direction == Direction.UP && direction == Direction.DOWN) return;
        if (this.direction == Direction.DOWN && direction == Direction.UP) return;
        if (this.direction == Direction.LEFT && direction == Direction.RIGHT) return;
        if (this.direction == Direction.RIGHT && direction == Direction.LEFT) return;
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public Position getNextHead() {
        Position head = getHead();
        switch (direction) {
            case UP:    return new Position(head.getRow() - 1, head.getCol());
            case DOWN:  return new Position(head.getRow() + 1, head.getCol());
            case LEFT:  return new Position(head.getRow(), head.getCol() - 1);
            case RIGHT: return new Position(head.getRow(), head.getCol() + 1);
        }
        throw new RuntimeException("Invalid direction");
    }

    public void grow(Position newHead) {
        body.addFirst(newHead);
        bodySet.add(newHead);
    }

    public void move(Position newHead) {
        Position tail = body.removeLast();
        bodySet.remove(tail);
        body.addFirst(newHead);
        bodySet.add(newHead);
    }

    public boolean collidesWithSelf(Position pos) {
        return bodySet.contains(pos);
    }

    public int getLength() {
        return body.size();
    }

    public Deque<Position> getBody() {
        return body;
    }
}

