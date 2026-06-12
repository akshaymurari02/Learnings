package parkinglot;

/**
 * Enum representing different types of vehicles.
 * Each type has a size that determines which spots it can use.
 */
public enum VehicleType {
    MOTORCYCLE(1),  // Smallest - can fit in any spot
    CAR(2),         // Medium - needs medium or large spot
    TRUCK(3);       // Largest - needs large spot

    private final int size;

    VehicleType(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
