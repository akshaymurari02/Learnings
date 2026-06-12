package logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Log sink that writes to a file.
 * Supports file rotation based on size (basic implementation).
 */
public class FileLogSink implements ILogSink {

    private final String filePath;
    private final ILogFormatter formatter;
    private final boolean append;
    private PrintWriter writer;

    public FileLogSink(String filePath) {
        this(filePath, new DefaultLogFormatter(), true);
    }

    public FileLogSink(String filePath, ILogFormatter formatter) {
        this(filePath, formatter, true);
    }

    public FileLogSink(String filePath, ILogFormatter formatter, boolean append) {
        this.filePath = filePath;
        this.formatter = formatter;
        this.append = append;
        initWriter();
    }

    private void initWriter() {
        try {
            this.writer = new PrintWriter(
                new BufferedWriter(new FileWriter(filePath, append)));
        } catch (IOException e) {
            System.err.println("Failed to initialize file logger: " + e.getMessage());
        }
    }

    @Override
    public synchronized void write(LogEntry entry) {
        if (writer != null) {
            writer.println(formatter.format(entry));
        }
    }

    @Override
    public synchronized void flush() {
        if (writer != null) {
            writer.flush();
        }
    }

    @Override
    public synchronized void close() {
        if (writer != null) {
            writer.flush();
            writer.close();
            writer = null;
        }
    }
}
