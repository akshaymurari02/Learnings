package logging;

import java.time.LocalDateTime;

/**
 * Represents a structured log entry with metadata.
 */
public class LogEntry {
    private final LocalDateTime timestamp;
    private final LogLevel level;
    private final String message;
    private final String threadName;
    private final String className;

    public LogEntry(LogLevel level, String message) {
        this(level, message, null);
    }

    public LogEntry(LogLevel level, String message, String className) {
        this.timestamp = LocalDateTime.now();
        this.level = level;
        this.message = message;
        this.threadName = Thread.currentThread().getName();
        this.className = className;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public LogLevel getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public String getThreadName() {
        return threadName;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        return String.format("[%s] [%s] [%s] %s",
            timestamp, level, threadName, message);
    }
}
