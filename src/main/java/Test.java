import connection.pool.ClusterConnectionPool;
import connection.statistic.MyStatistic;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) throws SQLException {
        MyStatistic myStatistic = new MyStatistic();
        ClusterConnectionPool clusterConnectionPool = new ClusterConnectionPool(myStatistic);
        ArrayList<Connection> connectionArrayList = new ArrayList<>();

        for (int i = 0; i<10; i++) {
            Connection connection = clusterConnectionPool.getConnection();
            System.out.println("adding LogicalConnection = " + connection);
            connectionArrayList.add(connection);
        }
        for (Connection connection : connectionArrayList) {
            System.out.println("closing LogicalConnection = " + connection);
            connection.close();
        }

        myStatistic.printUsage();
    }
}