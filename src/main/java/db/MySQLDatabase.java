package db;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import javax.sql.PooledConnection;
import java.sql.SQLException;

public class MySQLDatabase implements Database {

    // Set the JDBC URL and credentials
    static final String JDBC_URL = "jdbc:mysql://localhost:3306/test";
    static final String USER = "root";
    static final String PASSWORD = "1234";


    public PooledConnection createPhysicalConnection() {

        // Create a ConnectionPoolDataSource object
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();

        // Set the database URL, username, and password
        dataSource.setURL(JDBC_URL);
        dataSource.setUser(USER);
        dataSource.setPassword(PASSWORD);

        // Get a physical connection from the pool
        PooledConnection pooledConnection = null;
        try {
            pooledConnection = dataSource.getPooledConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("pooledConnection = " + pooledConnection);

        return pooledConnection;
    }
}