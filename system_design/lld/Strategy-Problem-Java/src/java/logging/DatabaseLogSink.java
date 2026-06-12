package logging;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Log sink that writes to a database.
 * Requires a connection provider to obtain database connections.
 */
public class DatabaseLogSink implements ILogSink {

    private final IConnectionProvider connectionProvider;
    private final String tableName;
    private final String insertSql;

    public DatabaseLogSink(IConnectionProvider connectionProvider) {
        this(connectionProvider, "application_logs");
    }

    public DatabaseLogSink(IConnectionProvider connectionProvider, String tableName) {
        this.connectionProvider = connectionProvider;
        this.tableName = tableName;
        this.insertSql = String.format(
            "INSERT INTO %s (timestamp, level, thread_name, message) VALUES (?, ?, ?, ?)",
            tableName
        );
    }

    @Override
    public void write(LogEntry entry) {
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertSql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(entry.getTimestamp()));
            stmt.setString(2, entry.getLevel().name());
            stmt.setString(3, entry.getThreadName());
            stmt.setString(4, entry.getMessage());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to write log to database: " + e.getMessage());
        }
    }

    /**
     * Returns the SQL to create the log table.
     * Use this to set up your database schema.
     */
    public String getCreateTableSql() {
        return String.format(
            "CREATE TABLE IF NOT EXISTS %s (" +
            "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
            "timestamp TIMESTAMP NOT NULL, " +
            "level VARCHAR(10) NOT NULL, " +
            "thread_name VARCHAR(100), " +
            "message TEXT" +
            ")", tableName
        );
    }
}
