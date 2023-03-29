package connection;

import db.Database;

import javax.sql.PooledConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class ComponentConnectionPool {

    private Database database;
    private Queue<PooledConnection> pool = new LinkedBlockingDeque();

    public ComponentConnectionPool(Database database) {
        this.database = database;
    }


    public void initialize() {
        for (int i = 0; i < 5; i++) {
            PooledConnection physicalConnection = database.createPhysicalConnection();
            pool.add(physicalConnection);
        }
    }

    public Connection getLogicalConnection() {
        PooledConnection physicalConnection = pool.poll();
        if (physicalConnection == null){
            return null;
        }

        return getMyWrapperLogicalConnection(physicalConnection);
    }

    private MyWrapperLogicalConnection getMyWrapperLogicalConnection(PooledConnection physicalConnection) {
        Connection connection = null;
        try {
            connection = physicalConnection.getConnection();
        } catch (SQLException e) {
            System.out.println("e = " + e);
            return null;
        }
        return new MyWrapperLogicalConnection(connection, physicalConnection, this);
    }

    public void returnPhysicalConnection(PooledConnection physicalConnection) {
        pool.add(physicalConnection);
    }
}
