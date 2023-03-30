package connection.pool.policy;

import connection.pool.ComponentConnectionPool;
import connection.statistic.MyStatistic;

import java.sql.Connection;
import java.util.List;

public class RoundRobin implements Policy{


    private int index = 0;

    private MyStatistic myStatistic;

    public RoundRobin(MyStatistic myStatistic) {
        this.myStatistic = myStatistic;
    }

    @Override
    public Connection implement(List<ComponentConnectionPool> componentConnectionPoolList) {

        while(true) {
//            sleep(1000);
            ComponentConnectionPool componentConnectionPool = updateIndexAndGetPool(componentConnectionPoolList);

            if (!componentConnectionPool.isWorking()) {
                System.out.println("!!!!!!!!!!!!! " +componentConnectionPool.getDatabase().toString() + " is not working...." + componentConnectionPool.isWorking());
                continue;
            }
            Connection connection = componentConnectionPool.getLogicalConnection();
            if (connection != null){
                myStatistic.update(componentConnectionPool);
                return connection;
            }
        }
    }

    private synchronized ComponentConnectionPool updateIndexAndGetPool(List<ComponentConnectionPool> componentConnectionPoolList) {
            ComponentConnectionPool componentConnectionPool = componentConnectionPoolList.get(index);
            index++;
            if( index == componentConnectionPoolList.size())
                index %= componentConnectionPoolList.size();
            return  componentConnectionPool;
    }

    private void sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
