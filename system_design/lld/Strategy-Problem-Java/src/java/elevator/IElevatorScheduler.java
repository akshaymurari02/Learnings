package elevator;

/**
 * Strategy interface for elevator scheduling algorithms.
 */
public interface IElevatorScheduler {

    /**
     * Select the best elevator for a request.
     * @param elevators Array of available elevators
     * @param request The elevator request
     * @return The selected elevator, or null if none available
     */
    Elevator selectElevator(Elevator[] elevators, ElevatorRequest request);
}
