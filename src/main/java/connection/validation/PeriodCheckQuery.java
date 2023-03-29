package connection.validation;

import connection.pool.ComponentConnectionPool;
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
        System.out.println("======= running check query connectionPool = " + connectionPool.getDatabase().toString());
        if(!connectionPool.isWorking()) {
            Database database = connectionPool.getDatabase();
            PooledConnection physicalConnection =null;
            Connection connection = null;
            try {
                physicalConnection = database.createPhysicalConnection();
                connection = physicalConnection.getConnection();
            } catch (Exception e) {
                System.out.println("e = " + e);
                System.out.println("check query Failed connectionPool = " + connectionPool.getDatabase().toString());
            }
            if (physicalConnection != null && connection != null) {
                boolean available = connectionPool.validateConnection(physicalConnection,connection);
                if (available)
                    connectionPool.setNowWorking();
            }
        }
    }
}
