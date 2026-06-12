package parkinglot;

/**
 * Strategy that finds spots closest to the entrance (assumes lower spot IDs are closer).
 * Good for customer convenience.
 */
public class NearestEntranceStrategy implements IParkingStrategy {

    @Override
    public ParkingSpot findSpot(Vehicle vehicle, Floor floor) {
        ParkingSpot nearestSpot = null;

        for (ParkingSpot spot : floor.getSpots()) {
            if (spot.canFitVehicle(vehicle)) {
                if (nearestSpot == null ||
                    spot.getSpotId().compareTo(nearestSpot.getSpotId()) < 0) {
                    nearestSpot = spot;
                }
            }
        }
        return nearestSpot;
    }
}
