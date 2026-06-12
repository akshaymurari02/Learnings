package elevator;

/**
 * Represents an elevator request from a floor.
 */
public class ElevatorRequest {

    private final int fromFloor;
    private final int toFloor;
    private final Direction direction;
    private final long timestamp;

    public ElevatorRequest(int fromFloor, int toFloor) {
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;
        this.direction = toFloor > fromFloor ? Direction.UP : Direction.DOWN;
        this.timestamp = System.currentTimeMillis();
    }

    public ElevatorRequest(int floor, Direction direction) {
        this.fromFloor = floor;
        this.toFloor = -1; // Unknown destination
        this.direction = direction;
        this.timestamp = System.currentTimeMillis();
    }

    public int getFromFloor() {
        return fromFloor;
    }

    public int getToFloor() {
        return toFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        if (toFloor == -1) {
            return String.format("Request[floor=%d, direction=%s]", fromFloor, direction);
        }
        return String.format("Request[from=%d, to=%d, direction=%s]", fromFloor, toFloor, direction);
    }
}
