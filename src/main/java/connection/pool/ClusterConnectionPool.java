package connection.pool;

import connection.pool.policy.Policy;
import connection.statistic.MyStatistic;
import connection.validation.ConnectionValidation;
import db.MySQLDatabase;
import db.OracleDatabase;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ClusterConnectionPool {

    private List<ComponentConnectionPool> componentConnectionPoolList = new ArrayList<>();

    private Policy policy;
    public ClusterConnectionPool(Policy policy) {
        this.policy = policy;
        init();
    }

    private void init() {
        ComponentConnectionPool mysqlConnectionPool = new ComponentConnectionPool(new MySQLDatabase(),new ConnectionValidation());
        mysqlConnectionPool.initialize();
        componentConnectionPoolList.add(mysqlConnectionPool);

        ComponentConnectionPool oracleConnectionPool = new ComponentConnectionPool(new OracleDatabase(), new ConnectionValidation());
        oracleConnectionPool.initialize();
        componentConnectionPoolList.add(oracleConnectionPool);
    }

    public Connection getConnection() {
      return policy.implement(componentConnectionPoolList);
    }

}
