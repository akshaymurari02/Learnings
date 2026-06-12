package logging;

/**
 * Interface for formatting log entries.
 */
public interface ILogFormatter {
    String format(LogEntry entry);
}
