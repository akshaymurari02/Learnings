package parkinglot;

/**
 * Enum representing different sizes of parking spots.
 * Each spot type can accommodate vehicles up to its capacity.
 */
public enum SpotType {
    SMALL(1),   // For motorcycles only
    MEDIUM(2),  // For motorcycles and cars
    LARGE(3);   // For any vehicle including trucks

    private final int capacity;

    SpotType(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean canFit(VehicleType vehicleType) {
        return this.capacity >= vehicleType.getSize();
    }
}
