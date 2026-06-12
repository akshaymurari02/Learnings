package logging;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Async wrapper for any log sink.
 * Uses a background thread to write logs without blocking the main thread.
 * Thread-safe and suitable for high-throughput logging.
 */
public class AsyncLogSink implements ILogSink {

    private final ILogSink delegate;
    private final BlockingQueue<LogEntry> queue;
    private final Thread workerThread;
    private final AtomicBoolean running;
    private final int batchSize;

    public AsyncLogSink(ILogSink delegate) {
        this(delegate, 10000, 100);
    }

    public AsyncLogSink(ILogSink delegate, int queueCapacity, int batchSize) {
        this.delegate = delegate;
        this.queue = new LinkedBlockingQueue<>(queueCapacity);
        this.running = new AtomicBoolean(true);
        this.batchSize = batchSize;

        this.workerThread = new Thread(this::processLogs, "AsyncLogSink-Worker");
        this.workerThread.setDaemon(true);
        this.workerThread.start();
    }

    @Override
    public void write(LogEntry entry) {
        if (!running.get()) {
            return;
        }

        // Non-blocking offer - drops log if queue is full
        if (!queue.offer(entry)) {
            System.err.println("Log queue full, dropping log entry");
        }
    }

    private void processLogs() {
        while (running.get() || !queue.isEmpty()) {
            try {
                LogEntry entry = queue.poll(100, TimeUnit.MILLISECONDS);
                if (entry != null) {
                    delegate.write(entry);

                    // Batch processing for efficiency
                    int processed = 1;
                    while (processed < batchSize) {
                        entry = queue.poll();
                        if (entry == null) break;
                        delegate.write(entry);
                        processed++;
                    }

                    // Flush after batch
                    delegate.flush();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("Error in async log worker: " + e.getMessage());
            }
        }
    }

    @Override
    public void flush() {
        // Wait for queue to drain
        while (!queue.isEmpty()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        delegate.flush();
    }

    @Override
    public void close() {
        running.set(false);

        try {
            // Wait for worker to finish processing remaining logs
            workerThread.join(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        delegate.close();
    }

    /**
     * Returns the number of pending log entries in the queue.
     */
    public int getPendingCount() {
        return queue.size();
    }
}
