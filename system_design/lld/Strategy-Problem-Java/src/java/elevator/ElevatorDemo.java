package elevator;

/**
 * Demo showing how to use the Elevator System.
 */
public class ElevatorDemo {

    public static void main(String[] args) throws InterruptedException {
        // Create elevator system: 3 elevators, floors 0-10, capacity 8 people
        ElevatorService service = new ElevatorService(3, 0, 10, 8);

        // Start the system
        service.start();

        System.out.println("\n=== Initial Status ===");
        service.displayStatus();

        // Simulate requests
        System.out.println("\n=== Making Requests ===");

        // Person on floor 0 wants to go to floor 5
        service.requestElevator(0, 5);
        Thread.sleep(2000);

        // Person on floor 3 wants to go up
        service.requestElevator(3, Direction.UP);
        Thread.sleep(2000);

        // Person on floor 7 wants to go to floor 2
        service.requestElevator(7, 2);
        Thread.sleep(3000);

        System.out.println("\n=== Current Status ===");
        service.displayStatus();

        // Press buttons inside elevator 1
        service.pressFloorButton(1, 8);
        Thread.sleep(3000);

        System.out.println("\n=== Statistics ===");
        System.out.println(service.getStats());

        // Wait for elevators to finish
        Thread.sleep(5000);

        System.out.println("\n=== Final Status ===");
        service.displayStatus();

        // Stop the system
        service.stop();
    }

    /**
     * Example with round-robin scheduler.
     */
    public static void roundRobinExample() throws InterruptedException {
        System.out.println("\n=== Round Robin Scheduler Example ===");

        ElevatorService service = new ElevatorService(2, 0, 5, 10, new RoundRobinScheduler());
        service.start();

        // Multiple requests to see round-robin distribution
        service.requestElevator(0, 5);
        service.requestElevator(1, 4);
        service.requestElevator(2, 3);

        Thread.sleep(5000);
        service.displayStatus();
        service.stop();
    }

    /**
     * Example with maintenance mode.
     */
    public static void maintenanceExample() throws InterruptedException {
        System.out.println("\n=== Maintenance Mode Example ===");

        ElevatorService service = new ElevatorService(3, 0, 10, 8);
        service.start();

        // Put elevator 2 in maintenance
        service.setMaintenance(2, true);

        // Make requests - elevator 2 won't be used
        service.requestElevator(0, 10);
        service.requestElevator(5, 0);

        Thread.sleep(5000);
        service.displayStatus();

        // Take elevator 2 out of maintenance
        service.setMaintenance(2, false);

        service.stop();
    }
}
