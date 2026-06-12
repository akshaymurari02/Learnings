package logging;

import java.time.format.DateTimeFormatter;

/**
 * Default log formatter that produces human-readable output.
 */
public class DefaultLogFormatter implements ILogFormatter {

    private static final DateTimeFormatter TIME_FORMAT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public String format(LogEntry entry) {
        return String.format("[%s] [%-5s] [%s] %s",
            entry.getTimestamp().format(TIME_FORMAT),
            entry.getLevel(),
            entry.getThreadName(),
            entry.getMessage());
    }
}
