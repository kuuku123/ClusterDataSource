import connection.pool.ClusterConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) throws SQLException {
        ClusterConnectionPool clusterConnectionPool = new ClusterConnectionPool();
        ArrayList<Connection> connectionArrayList = new ArrayList<>();

        for (int i = 0; i<100; i++) {
            Connection connection = clusterConnectionPool.getConnection();
            System.out.println("adding LogicalConnection = " + connection);
            connectionArrayList.add(connection);
        }
        for (Connection connection : connectionArrayList) {
            System.out.println("closing LogicalConnection = " + connection);
            connection.close();
        }
    }
}