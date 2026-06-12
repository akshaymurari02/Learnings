package logging;

/**
 * Improved logging service with pluggable sinks for different destinations.
 * Supports console, file, database, and async logging through composition.
 *
 * Usage examples:
 *
 * 1. Simple console logging:
 *    LoggingService logger = new LoggingService(LogLevel.INFO);
 *
 * 2. File logging:
 *    LoggingService logger = new LoggingService(LogLevel.DEBUG,
 *        new FileLogSink("app.log"));
 *
 * 3. Multiple destinations:
 *    LoggingService logger = new LoggingService(LogLevel.INFO,
 *        new CompositeLogSink(
 *            new ConsoleLogSink(),
 *            new FileLogSink("app.log")
 *        ));
 *
 * 4. Async logging to file:
 *    LoggingService logger = new LoggingService(LogLevel.DEBUG,
 *        new AsyncLogSink(new FileLogSink("app.log")));
 */
public class LoggingService implements ILogger {

    private final LogLevel minimumLevel;
    private final ILogSink sink;

    public LoggingService(LogLevel minimumLevel) {
        this(minimumLevel, new ConsoleLogSink());
    }

    public LoggingService(LogLevel minimumLevel, ILogSink sink) {
        this.minimumLevel = minimumLevel;
        this.sink = sink;
    }

    @Override
    public void log(LogLevel level, String message) {
        if (level.getSeverity() >= minimumLevel.getSeverity()) {
            LogEntry entry = new LogEntry(level, message);
            sink.write(entry);
        }
    }

    @Override
    public void trace(String message) {
        log(LogLevel.TRACE, message);
    }

    @Override
    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    @Override
    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    @Override
    public void warn(String message) {
        log(LogLevel.WARN, message);
    }

    @Override
    public void error(String message) {
        log(LogLevel.ERROR, message);
    }

    @Override
    public void fatal(String message) {
        log(LogLevel.FATAL, message);
    }

    /**
     * Flush any buffered logs to the sink.
     */
    public void flush() {
        sink.flush();
    }

    /**
     * Close the logger and release resources.
     */
    public void close() {
        sink.close();
    }

    /**
     * Factory method to create a simple console logger.
     */
    public static LoggingService createConsoleLogger(LogLevel level) {
        return new LoggingService(level, new ConsoleLogSink());
    }

    /**
     * Factory method to create a file logger.
     */
    public static LoggingService createFileLogger(LogLevel level, String filePath) {
        return new LoggingService(level, new FileLogSink(filePath));
    }

    /**
     * Factory method to create an async file logger.
     */
    public static LoggingService createAsyncFileLogger(LogLevel level, String filePath) {
        return new LoggingService(level,
            new AsyncLogSink(new FileLogSink(filePath)));
    }

    /**
     * Factory method to create a logger that writes to both console and file.
     */
    public static LoggingService createConsoleAndFileLogger(LogLevel level, String filePath) {
        return new LoggingService(level,
            new CompositeLogSink(
                new ConsoleLogSink(),
                new FileLogSink(filePath)
            ));
    }
}
