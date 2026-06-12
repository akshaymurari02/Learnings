package logging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Composite log sink that writes to multiple sinks.
 * Useful for writing to console AND file simultaneously.
 */
public class CompositeLogSink implements ILogSink {

    private final List<ILogSink> sinks;

    public CompositeLogSink(ILogSink... sinks) {
        this.sinks = new ArrayList<>(Arrays.asList(sinks));
    }

    public void addSink(ILogSink sink) {
        sinks.add(sink);
    }

    public void removeSink(ILogSink sink) {
        sinks.remove(sink);
    }

    @Override
    public void write(LogEntry entry) {
        for (ILogSink sink : sinks) {
            try {
                sink.write(entry);
            } catch (Exception e) {
                System.err.println("Failed to write to sink: " + e.getMessage());
            }
        }
    }

    @Override
    public void flush() {
        for (ILogSink sink : sinks) {
            try {
                sink.flush();
            } catch (Exception e) {
                System.err.println("Failed to flush sink: " + e.getMessage());
            }
        }
    }

    @Override
    public void close() {
        for (ILogSink sink : sinks) {
            try {
                sink.close();
            } catch (Exception e) {
                System.err.println("Failed to close sink: " + e.getMessage());
            }
        }
    }
}
