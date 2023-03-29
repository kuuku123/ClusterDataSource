package connection.pool;

import connection.validation.ConnectionValidation;
import connection.validation.PeriodCheckQuery;
import db.Database;

import javax.sql.PooledConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ComponentConnectionPool {

    private Database database;
    private Queue<PooledConnection> pool = new LinkedBlockingDeque();
    private ConnectionValidation connectionValidation;

    private volatile AtomicBoolean working = new AtomicBoolean(true);
    private ScheduledExecutorService scheduledExecutorService;

    public ComponentConnectionPool(Database database, ConnectionValidation connectionValidation) {
        this.database = database;
        this.connectionValidation = connectionValidation;
        this.scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
    }

    public void initialize() {
        for (int i = 0; i < 5; i++) {
            PooledConnection physicalConnection = database.createPhysicalConnection();
            pool.add(physicalConnection);
        }
        scheduledExecutorService.scheduleAtFixedRate(new PeriodCheckQuery(this), 1000, 5000, TimeUnit.MILLISECONDS);
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
            boolean available = validateConnection(physicalConnection, connection);
            if (!available) {
                closeInvalidConnection(physicalConnection);
                setNotWorking();
                return null;
            }
        } catch (SQLException e) {
            System.out.println("e = " + e);
            closeInvalidConnection(physicalConnection);
            setNotWorking();
            return null;
        }
        return new MyWrapperLogicalConnection(connection, physicalConnection, this);
    }

    public boolean validateConnection(PooledConnection physicalConnection, Connection connection) {
        boolean available = connectionValidation.checkLogicalConnection(connection, physicalConnection);
        return available;
    }


    public void returnPhysicalConnection(PooledConnection physicalConnection) {
        pool.add(physicalConnection);
    }

    public void closeInvalidConnection(PooledConnection physicalConnection) {
        try {
            physicalConnection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isWorking() {
        return working.get();
    }

    public void setNotWorking() {
        this.working.compareAndSet(true,false);
    }
    public void setNowWorking() {
        this.working.compareAndSet(false,true);
        System.out.println("database = "+database.toString() + " working = " + working.get());
    }

    public Database getDatabase() {
        return database;
    }
}
