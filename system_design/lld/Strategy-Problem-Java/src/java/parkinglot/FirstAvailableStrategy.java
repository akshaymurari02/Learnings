package parkinglot;

/**
 * Strategy that finds the first available spot that fits the vehicle.
 * Simple and fast approach - no optimization.
 */
public class FirstAvailableStrategy implements IParkingStrategy {

    @Override
    public ParkingSpot findSpot(Vehicle vehicle, Floor floor) {
        for (ParkingSpot spot : floor.getSpots()) {
            if (spot.canFitVehicle(vehicle)) {
                return spot;
            }
        }
        return null;
    }
}
