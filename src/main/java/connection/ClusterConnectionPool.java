package connection;

import db.MySQLDatabase;
import db.OracleDatabase;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ClusterConnectionPool {

    private List<ComponentConnectionPool> componentConnectionPoolList = new ArrayList<>();
    private String policy = "RR_with_Failover";

    private int index = 0;

    public ClusterConnectionPool() {
        ComponentConnectionPool mysqlConnectionPool = new ComponentConnectionPool(new MySQLDatabase());
        mysqlConnectionPool.initialize();
        componentConnectionPoolList.add(mysqlConnectionPool);

        ComponentConnectionPool oracleConnectionPool = new ComponentConnectionPool(new OracleDatabase());
        oracleConnectionPool.initialize();
        componentConnectionPoolList.add(oracleConnectionPool);
    }

    public Connection getConnection() {

        if (policy.equals("RR_with_Failover")) {
            return roundRobin();
        }
        else {
            return otherPolicy();
        }
    }

    private Connection otherPolicy() {
        return null;
    }

    private Connection roundRobin() {

        while(true) {
            ComponentConnectionPool componentConnectionPool = componentConnectionPoolList.get(index);
            index++;
            if( index == componentConnectionPoolList.size())
                index = index % componentConnectionPoolList.size();
            Connection connection = componentConnectionPool.getLogicalConnection();
            if (connection != null)
                return connection;
        }
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }
}
