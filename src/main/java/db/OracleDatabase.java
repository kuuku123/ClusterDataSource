package db;

import oracle.jdbc.pool.OracleConnectionPoolDataSource;

import javax.sql.PooledConnection;
import java.sql.SQLException;

public class OracleDatabase implements Database{
    static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    static final String USER = "c##tony";
    static final String PASSWORD = "1234";

    public PooledConnection createPhysicalConnection() {
        try {
            // Create a ConnectionPoolDataSource object
            OracleConnectionPoolDataSource dataSource = new OracleConnectionPoolDataSource();
            // Set the database URL, username, and password
            dataSource.setURL(JDBC_URL);
            dataSource.setUser(USER);
            dataSource.setPassword(PASSWORD);

            // Get a physical connection from the pool
            PooledConnection pooledConnection = dataSource.getPooledConnection();
            System.out.println("pooledConnection = " + pooledConnection);

            return pooledConnection;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
