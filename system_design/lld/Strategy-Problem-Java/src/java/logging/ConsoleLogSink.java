package logging;

/**
 * Log sink that writes to the console (stdout).
 */
public class ConsoleLogSink implements ILogSink {

    private final ILogFormatter formatter;

    public ConsoleLogSink() {
        this(new DefaultLogFormatter());
    }

    public ConsoleLogSink(ILogFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public void write(LogEntry entry) {
        System.out.println(formatter.format(entry));
    }
}
