package elevator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Elevator System Service that manages multiple elevators.
 * Handles request scheduling, elevator movement simulation, and system monitoring.
 */
public class ElevatorService {

    private final Elevator[] elevators;
    private final int minFloor;
    private final int maxFloor;
    private final IElevatorScheduler scheduler;
    private final List<ElevatorRequest> pendingRequests;
    private final ScheduledExecutorService executorService;
    private volatile boolean running;

    public ElevatorService(int numElevators, int minFloor, int maxFloor, int capacity) {
        this(numElevators, minFloor, maxFloor, capacity, new NearestCarScheduler());
    }

    public ElevatorService(int numElevators, int minFloor, int maxFloor, int capacity, IElevatorScheduler scheduler) {
        if (numElevators <= 0) {
            throw new IllegalArgumentException("Number of elevators must be positive");
        }
        if (minFloor >= maxFloor) {
            throw new IllegalArgumentException("minFloor must be less than maxFloor");
        }

        this.elevators = new Elevator[numElevators];
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.scheduler = scheduler;
        this.pendingRequests = new CopyOnWriteArrayList<>();
        this.running = false;
        this.executorService = Executors.newScheduledThreadPool(1);

        // Initialize elevators
        for (int i = 0; i < numElevators; i++) {
            elevators[i] = new Elevator(i + 1, minFloor, maxFloor, capacity);
        }
    }

    // ==================== REQUEST HANDLING ====================

    /**
     * Request an elevator from a floor going in a direction.
     */
    public void requestElevator(int floor, Direction direction) {
        validateFloor(floor);
        ElevatorRequest request = new ElevatorRequest(floor, direction);
        processRequest(request);
    }

    /**
     * Request an elevator to go from one floor to another.
     */
    public void requestElevator(int fromFloor, int toFloor) {
        validateFloor(fromFloor);
        validateFloor(toFloor);

        if (fromFloor == toFloor) {
            return; // No need to move
        }

        ElevatorRequest request = new ElevatorRequest(fromFloor, toFloor);
        processRequest(request);
    }

    private void processRequest(ElevatorRequest request) {
        // Select best elevator using scheduler
        Elevator selectedElevator = scheduler.selectElevator(elevators, request);

        if (selectedElevator == null) {
            // All elevators busy or in maintenance, queue the request
            pendingRequests.add(request);
            System.out.println("Request queued: " + request);
            return;
        }

        // Assign stops to elevator
        selectedElevator.addStop(request.getFromFloor());
        if (request.getToFloor() != -1) {
            selectedElevator.addStop(request.getToFloor());
        }

        System.out.println("Assigned " + request + " to Elevator " + selectedElevator.getId());
    }

    /**
     * Press a button inside an elevator to go to a floor.
     */
    public void pressFloorButton(int elevatorId, int destinationFloor) {
        validateFloor(destinationFloor);
        Elevator elevator = getElevator(elevatorId);

        if (elevator == null) {
            throw new IllegalArgumentException("Invalid elevator ID: " + elevatorId);
        }

        elevator.addStop(destinationFloor);
        System.out.println("Elevator " + elevatorId + " button pressed for floor " + destinationFloor);
    }

    // ==================== SYSTEM CONTROL ====================

    /**
     * Start the elevator system simulation.
     */
    public void start() {
        if (running) {
            return;
        }

        running = true;
        executorService.scheduleAtFixedRate(this::step, 0, 1000, TimeUnit.MILLISECONDS);
        System.out.println("Elevator system started with " + elevators.length + " elevators");
    }

    /**
     * Stop the elevator system.
     */
    public void stop() {
        running = false;
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.out.println("Elevator system stopped");
    }

    /**
     * Execute one simulation step - move all elevators.
     */
    private void step() {
        for (Elevator elevator : elevators) {
            boolean moved = elevator.move();

            if (moved && elevator.getState() == ElevatorState.STOPPED) {
                System.out.println("Elevator " + elevator.getId() + " stopped at floor " + elevator.getCurrentFloor());
            }
        }

        // Process pending requests
        processPendingRequests();
    }

    private void processPendingRequests() {
        List<ElevatorRequest> toRemove = new ArrayList<>();

        for (ElevatorRequest request : pendingRequests) {
            Elevator elevator = scheduler.selectElevator(elevators, request);
            if (elevator != null && !elevator.isFull()) {
                elevator.addStop(request.getFromFloor());
                if (request.getToFloor() != -1) {
                    elevator.addStop(request.getToFloor());
                }
                toRemove.add(request);
                System.out.println("Processed queued request: " + request + " -> Elevator " + elevator.getId());
            }
        }

        pendingRequests.removeAll(toRemove);
    }

    // ==================== ELEVATOR MANAGEMENT ====================

    /**
     * Put an elevator into maintenance mode.
     */
    public void setMaintenance(int elevatorId, boolean maintenance) {
        Elevator elevator = getElevator(elevatorId);
        if (elevator != null) {
            elevator.setState(maintenance ? ElevatorState.MAINTENANCE : ElevatorState.IDLE);
            System.out.println("Elevator " + elevatorId + " maintenance mode: " + maintenance);
        }
    }

    /**
     * Get an elevator by ID.
     */
    public Elevator getElevator(int elevatorId) {
        for (Elevator elevator : elevators) {
            if (elevator.getId() == elevatorId) {
                return elevator;
            }
        }
        return null;
    }

    /**
     * Get all elevators.
     */
    public Elevator[] getAllElevators() {
        return elevators;
    }

    // ==================== STATUS & MONITORING ====================

    /**
     * Get system status.
     */
    public String getSystemStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Elevator System Status ===\n");
        sb.append("Total Elevators: ").append(elevators.length).append("\n");
        sb.append("Floor Range: ").append(minFloor).append(" to ").append(maxFloor).append("\n");
        sb.append("Pending Requests: ").append(pendingRequests.size()).append("\n\n");

        for (Elevator elevator : elevators) {
            sb.append(elevator.toString()).append("\n");
        }

        return sb.toString();
    }

    /**
     * Display current status of all elevators.
     */
    public void displayStatus() {
        System.out.println(getSystemStatus());
    }

    /**
     * Get statistics about the system.
     */
    public ElevatorSystemStats getStats() {
        int totalStops = 0;
        int activeElevators = 0;
        int idleElevators = 0;
        int maintenanceElevators = 0;

        for (Elevator elevator : elevators) {
            totalStops += elevator.getTotalStops();

            switch (elevator.getState()) {
                case IDLE:
                    idleElevators++;
                    break;
                case MAINTENANCE:
                    maintenanceElevators++;
                    break;
                case MOVING_UP:
                case MOVING_DOWN:
                case STOPPED:
                    activeElevators++;
                    break;
            }
        }

        return new ElevatorSystemStats(
            elevators.length,
            activeElevators,
            idleElevators,
            maintenanceElevators,
            totalStops,
            pendingRequests.size()
        );
    }

    private void validateFloor(int floor) {
        if (floor < minFloor || floor > maxFloor) {
            throw new IllegalArgumentException("Floor " + floor + " is out of range [" + minFloor + ", " + maxFloor + "]");
        }
    }

    // ==================== STATS CLASS ====================

    public static class ElevatorSystemStats {
        public final int totalElevators;
        public final int activeElevators;
        public final int idleElevators;
        public final int maintenanceElevators;
        public final int totalPendingStops;
        public final int queuedRequests;

        public ElevatorSystemStats(int totalElevators, int activeElevators, int idleElevators,
                                   int maintenanceElevators, int totalPendingStops, int queuedRequests) {
            this.totalElevators = totalElevators;
            this.activeElevators = activeElevators;
            this.idleElevators = idleElevators;
            this.maintenanceElevators = maintenanceElevators;
            this.totalPendingStops = totalPendingStops;
            this.queuedRequests = queuedRequests;
        }

        @Override
        public String toString() {
            return String.format(
                "Stats[total=%d, active=%d, idle=%d, maintenance=%d, stops=%d, queued=%d]",
                totalElevators, activeElevators, idleElevators, maintenanceElevators,
                totalPendingStops, queuedRequests
            );
        }
    }
}
