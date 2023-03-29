package db;

import javax.sql.PooledConnection;

public interface Database {

    PooledConnection createPhysicalConnection();
}
