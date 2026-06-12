package logging;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface for providing database connections.
 * Allows flexibility in connection pooling strategies.
 */
public interface IConnectionProvider {
    Connection getConnection() throws SQLException;
}
