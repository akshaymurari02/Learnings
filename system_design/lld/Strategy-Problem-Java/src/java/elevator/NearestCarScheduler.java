package elevator;

/**
 * Nearest Car scheduler - selects the closest elevator.
 */
public class NearestCarScheduler implements IElevatorScheduler {

    @Override
    public Elevator selectElevator(Elevator[] elevators, ElevatorRequest request) {
        Elevator bestElevator = null;
        int minDistance = Integer.MAX_VALUE;

        for (Elevator elevator : elevators) {
            if (elevator.getState() == ElevatorState.MAINTENANCE) {
                continue;
            }

            int distance = elevator.getDistanceToFloor(request.getFromFloor(), request.getDirection());

            if (distance < minDistance) {
                minDistance = distance;
                bestElevator = elevator;
            }
        }

        return bestElevator;
    }
}
