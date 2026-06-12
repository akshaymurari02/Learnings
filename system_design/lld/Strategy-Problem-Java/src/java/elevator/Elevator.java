package elevator;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents a single elevator with its state and destination queue.
 */
public class Elevator {

    private final int id;
    private int currentFloor;
    private ElevatorState state;
    private Direction direction;
    private final int minFloor;
    private final int maxFloor;
    private final int capacity;
    private int currentLoad;

    // Floors to visit (sorted for efficient processing)
    private final TreeSet<Integer> upStops;
    private final TreeSet<Integer> downStops;

    public Elevator(int id, int minFloor, int maxFloor, int capacity) {
        this.id = id;
        this.currentFloor = 0; // Start at ground floor
        this.state = ElevatorState.IDLE;
        this.direction = Direction.IDLE;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.capacity = capacity;
        this.currentLoad = 0;
        this.upStops = new TreeSet<>();
        this.downStops = new TreeSet<>();
    }

    /**
     * Add a stop to the elevator's schedule.
     */
    public synchronized void addStop(int floor) {
        if (floor < minFloor || floor > maxFloor) {
            throw new IllegalArgumentException("Floor out of range: " + floor);
        }

        if (floor == currentFloor) {
            return; // Already at this floor
        }

        if (floor > currentFloor) {
            upStops.add(floor);
        } else {
            downStops.add(floor);
        }
    }

    /**
     * Move the elevator one step.
     * Returns true if moved, false if idle.
     */
    public synchronized boolean move() {
        if (state == ElevatorState.MAINTENANCE) {
            return false;
        }

        // Determine next action based on current state
        if (state == ElevatorState.STOPPED) {
            state = ElevatorState.IDLE;
            return false;
        }

        // Check if we need to stop at current floor
        if (shouldStopAtCurrentFloor()) {
            stop();
            return true;
        }

        // Determine direction and move
        if (direction == Direction.UP || (direction == Direction.IDLE && !upStops.isEmpty())) {
            moveUp();
            return true;
        } else if (direction == Direction.DOWN || (direction == Direction.IDLE && !downStops.isEmpty())) {
            moveDown();
            return true;
        } else {
            // No more stops
            state = ElevatorState.IDLE;
            direction = Direction.IDLE;
            return false;
        }
    }

    private void moveUp() {
        if (currentFloor < maxFloor) {
            currentFloor++;
            state = ElevatorState.MOVING_UP;
            direction = Direction.UP;
        }
    }

    private void moveDown() {
        if (currentFloor > minFloor) {
            currentFloor--;
            state = ElevatorState.MOVING_DOWN;
            direction = Direction.DOWN;
        }
    }

    private void stop() {
        state = ElevatorState.STOPPED;

        // Remove this floor from stops
        upStops.remove(currentFloor);
        downStops.remove(currentFloor);

        // Check if we need to change direction
        if (direction == Direction.UP && upStops.isEmpty() && !downStops.isEmpty()) {
            direction = Direction.DOWN;
        } else if (direction == Direction.DOWN && downStops.isEmpty() && !upStops.isEmpty()) {
            direction = Direction.UP;
        } else if (upStops.isEmpty() && downStops.isEmpty()) {
            direction = Direction.IDLE;
            state = ElevatorState.IDLE;
        }
    }

    private boolean shouldStopAtCurrentFloor() {
        if (direction == Direction.UP) {
            return upStops.contains(currentFloor);
        } else if (direction == Direction.DOWN) {
            return downStops.contains(currentFloor);
        }
        return upStops.contains(currentFloor) || downStops.contains(currentFloor);
    }

    /**
     * Calculate distance to a floor considering current direction.
     */
    public int getDistanceToFloor(int floor, Direction requestDirection) {
        if (state == ElevatorState.MAINTENANCE) {
            return Integer.MAX_VALUE;
        }

        if (currentFloor == floor) {
            return 0;
        }

        int distance = Math.abs(currentFloor - floor);

        // Penalty if elevator is moving in opposite direction
        if (direction == Direction.UP && floor < currentFloor) {
            distance += getTotalStops() * 2;
        } else if (direction == Direction.DOWN && floor > currentFloor) {
            distance += getTotalStops() * 2;
        }

        return distance;
    }

    public boolean isFull() {
        return currentLoad >= capacity;
    }

    public void addPassenger() {
        if (currentLoad < capacity) {
            currentLoad++;
        }
    }

    public void removePassenger() {
        if (currentLoad > 0) {
            currentLoad--;
        }
    }

    public int getTotalStops() {
        return upStops.size() + downStops.size();
    }

    public boolean hasStops() {
        return !upStops.isEmpty() || !downStops.isEmpty();
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public ElevatorState getState() {
        return state;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getCurrentLoad() {
        return currentLoad;
    }

    public int getCapacity() {
        return capacity;
    }

    public Set<Integer> getUpStops() {
        return new HashSet<>(upStops);
    }

    public Set<Integer> getDownStops() {
        return new HashSet<>(downStops);
    }

    public void setState(ElevatorState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return String.format("Elevator[id=%d, floor=%d, state=%s, direction=%s, load=%d/%d, stops=%d]",
            id, currentFloor, state, direction, currentLoad, capacity, getTotalStops());
    }
}
