package amazon.diningphilosopher;

import java.util.Random;

/**
 * Philosopher thread.
 * 
 * Deadlock prevention: Resource ordering — always pick up the lower-id fork first.
 * This breaks the circular wait condition.
 */
public class Philosopher implements Runnable {

    private final int id;
    private final String name;
    private final Fork leftFork;
    private final Fork rightFork;
    private final int mealsToEat;
    private int mealsEaten;
    private final Random random = new Random();

    public Philosopher(int id, String name, Fork leftFork, Fork rightFork, int mealsToEat) {
        this.id = id;
        this.name = name;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.mealsToEat = mealsToEat;
        this.mealsEaten = 0;
    }

    @Override
    public void run() {
        try {
            while (mealsEaten < mealsToEat) {
                think();
                eat();
            }
            System.out.println("✅ " + name + " finished all " + mealsToEat + " meals.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void think() throws InterruptedException {
        System.out.println("🤔 " + name + " is thinking...");
        Thread.sleep(random.nextInt(100) + 50);
    }

    private void eat() throws InterruptedException {
        // Resource ordering: always pick lower-id fork first to prevent deadlock
        Fork first = leftFork.getId() < rightFork.getId() ? leftFork : rightFork;
        Fork second = leftFork.getId() < rightFork.getId() ? rightFork : leftFork;

        // Try to pick up both forks
        while (true) {
            if (first.pickUp()) {
                if (second.pickUp()) {
                    // Got both forks — eat!
                    mealsEaten++;
                    System.out.println("🍝 " + name + " is eating meal #" + mealsEaten
                            + " (forks " + first.getId() + " & " + second.getId() + ")");
                    Thread.sleep(random.nextInt(100) + 50);

                    // Put down forks
                    second.putDown();
                    first.putDown();
                    return;
                } else {
                    // Couldn't get second fork, release first (avoid hold-and-wait)
                    first.putDown();
                }
            }
            // Back off before retrying
            Thread.sleep(random.nextInt(20));
        }
    }

    public String getName() { return name; }
    public int getMealsEaten() { return mealsEaten; }
}

