package connection.pool.policy;

import connection.pool.ComponentConnectionPool;

import java.sql.Connection;
import java.util.List;

public interface Policy {

    Connection implement(List<ComponentConnectionPool> componentConnectionPoolList);
}
