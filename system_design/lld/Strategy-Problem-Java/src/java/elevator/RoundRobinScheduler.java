package elevator;

/**
 * Round Robin scheduler - distributes load evenly across elevators.
 */
public class RoundRobinScheduler implements IElevatorScheduler {

    private int lastUsedIndex = -1;

    @Override
    public Elevator selectElevator(Elevator[] elevators, ElevatorRequest request) {
        if (elevators.length == 0) {
            return null;
        }

        // Try to find next available elevator in round-robin fashion
        for (int i = 0; i < elevators.length; i++) {
            lastUsedIndex = (lastUsedIndex + 1) % elevators.length;
            Elevator elevator = elevators[lastUsedIndex];

            if (elevator.getState() != ElevatorState.MAINTENANCE && !elevator.isFull()) {
                return elevator;
            }
        }

        // If all full or in maintenance, return the one with least stops
        Elevator bestElevator = null;
        int minStops = Integer.MAX_VALUE;

        for (Elevator elevator : elevators) {
            if (elevator.getState() != ElevatorState.MAINTENANCE) {
                int stops = elevator.getTotalStops();
                if (stops < minStops) {
                    minStops = stops;
                    bestElevator = elevator;
                }
            }
        }

        return bestElevator;
    }
}
