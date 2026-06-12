package amazon.diningphilosopher;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents a fork (shared resource) on the table.
 */
public class Fork {
    private final int id;
    private final Lock lock = new ReentrantLock();

    public Fork(int id) {
        this.id = id;
    }

    public boolean pickUp() {
        return lock.tryLock();
    }

    public void putDown() {
        lock.unlock();
    }

    public int getId() { return id; }
}

