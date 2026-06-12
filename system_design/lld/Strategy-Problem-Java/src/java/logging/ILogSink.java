package logging;

/**
 * Strategy interface for log destinations (sinks).
 * Different implementations handle writing logs to different targets.
 */
public interface ILogSink {

    /**
     * Write a log entry to the destination.
     * @param entry The log entry to write
     */
    void write(LogEntry entry);

    /**
     * Flush any buffered logs.
     */
    default void flush() {}

    /**
     * Close the sink and release resources.
     */
    default void close() {}
}
