package logging;

public interface ILogger {

    void log(LogLevel level, String message);

    void trace(String message);

    void debug(String message);

    void info(String message);

    void warn(String message);

    void error(String message);

    void fatal(String message);
}
