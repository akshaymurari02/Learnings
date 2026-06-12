package parkinglot;

/**
 * Represents a single parking spot in the parking lot.
 */
public class ParkingSpot {
    private final String spotId;
    private final SpotType spotType;
    private final int floorNumber;
    private Vehicle parkedVehicle;

    public ParkingSpot(String spotId, SpotType spotType, int floorNumber) {
        this.spotId = spotId;
        this.spotType = spotType;
        this.floorNumber = floorNumber;
    }

    public boolean isAvailable() {
        return parkedVehicle == null;
    }

    public boolean canFitVehicle(Vehicle vehicle) {
        return isAvailable() && spotType.canFit(vehicle.getType());
    }

    public void parkVehicle(Vehicle vehicle) {
        if (!canFitVehicle(vehicle)) {
            throw new IllegalStateException("Cannot park this vehicle in this spot");
        }
        this.parkedVehicle = vehicle;
    }

    public Vehicle removeVehicle() {
        Vehicle vehicle = this.parkedVehicle;
        this.parkedVehicle = null;
        return vehicle;
    }

    public String getSpotId() {
        return spotId;
    }

    public SpotType getSpotType() {
        return spotType;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }
}
