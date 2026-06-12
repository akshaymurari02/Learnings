package amazon.diningphilosopher;

/**
 * Dining Philosophers Problem - demonstrates deadlock prevention using resource ordering.
 *
 * Problem: 5 philosophers sit at a round table with 5 forks. Each needs 2 forks to eat.
 * Challenge: Avoid deadlock (all hold 1 fork waiting for the other).
 *
 * Solution: Resource Ordering — always pick up the lower-numbered fork first.
 * This breaks the circular wait condition (one of the 4 necessary conditions for deadlock).
 *
 * Other solutions (interview discussion):
 * 1. Arbitrator (Semaphore) — limit to N-1 philosophers eating at once
 * 2. Chandy/Misra — message-passing with dirty/clean forks
 * 3. tryLock with timeout — back off and retry
 */
public class DiningPhilosopherDemo {

    public static void main(String[] args) throws InterruptedException {

        int numPhilosophers = 5;
        int mealsPerPhilosopher = 3;

        Fork[] forks = new Fork[numPhilosophers];
        for (int i = 0; i < numPhilosophers; i++) {
            forks[i] = new Fork(i);
        }

        String[] names = {"Socrates", "Plato", "Aristotle", "Descartes", "Kant"};

        Thread[] threads = new Thread[numPhilosophers];
        Philosopher[] philosophers = new Philosopher[numPhilosophers];

        System.out.println("=== Dining Philosophers (Resource Ordering Solution) ===\n");
        System.out.println("Philosophers: " + numPhilosophers + " | Meals each: " + mealsPerPhilosopher + "\n");

        for (int i = 0; i < numPhilosophers; i++) {
            Fork leftFork = forks[i];
            Fork rightFork = forks[(i + 1) % numPhilosophers];
            philosophers[i] = new Philosopher(i, names[i], leftFork, rightFork, mealsPerPhilosopher);
            threads[i] = new Thread(philosophers[i], names[i]);
        }

        // Start all philosophers
        for (Thread t : threads) {
            t.start();
        }

        // Wait for all to finish
        for (Thread t : threads) {
            t.join();
        }

        System.out.println("\n=== All philosophers have finished eating! No deadlock! ===");
        for (Philosopher p : philosophers) {
            System.out.println("  " + p.getName() + " ate " + p.getMealsEaten() + " meals");
        }
    }
}

