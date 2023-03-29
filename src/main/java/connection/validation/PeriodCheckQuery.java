package connection.validation;

import connection.ComponentConnectionPool;
import db.Database;

import javax.sql.PooledConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class PeriodCheckQuery implements Runnable{

    private ComponentConnectionPool connectionPool;

    public PeriodCheckQuery(ComponentConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void run() {
        Database database = connectionPool.getDatabase();
        PooledConnection physicalConnection = database.createPhysicalConnection();
        Connection connection = null;
        try {
            connection = physicalConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (physicalConnection != null && connection != null) {
            boolean available = connectionPool.validateConnection(physicalConnection,connection);
            if (available)
                connectionPool.setNowWorking();
        }
    }
}
