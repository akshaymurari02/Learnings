package elevator;

/**
 * Elevator state.
 */
public enum ElevatorState {
    IDLE,           // Not moving, no requests
    MOVING_UP,      // Moving upward
    MOVING_DOWN,    // Moving downward
    STOPPED,        // Stopped at a floor (door open)
    MAINTENANCE     // Out of service
}
