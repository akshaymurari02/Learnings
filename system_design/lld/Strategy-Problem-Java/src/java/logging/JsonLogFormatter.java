package logging;

/**
 * JSON formatter for structured logging (useful for log aggregation systems).
 */
public class JsonLogFormatter implements ILogFormatter {

    @Override
    public String format(LogEntry entry) {
        return String.format(
            "{\"timestamp\":\"%s\",\"level\":\"%s\",\"thread\":\"%s\",\"message\":\"%s\"}",
            entry.getTimestamp(),
            entry.getLevel(),
            entry.getThreadName(),
            escapeJson(entry.getMessage())
        );
    }

    private String escapeJson(String text) {
        if (text == null) return "";
        return text
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
    }
}
