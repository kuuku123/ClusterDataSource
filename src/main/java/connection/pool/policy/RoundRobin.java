package connection.pool.policy;

import connection.pool.ComponentConnectionPool;
import connection.statistic.MyStatistic;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobin implements Policy{


    private AtomicInteger index = new AtomicInteger(0);

    private MyStatistic myStatistic;

    public RoundRobin(MyStatistic myStatistic) {
        this.myStatistic = myStatistic;
    }

    @Override
    public Connection implement(List<ComponentConnectionPool> componentConnectionPoolList) {

        while(true) {
//            sleep(1000);
            ComponentConnectionPool componentConnectionPool = componentConnectionPoolList.get(index.get());

            updateIndex(componentConnectionPoolList);

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

    private void updateIndex(List<ComponentConnectionPool> componentConnectionPoolList) {
        synchronized (index) {
            index.getAndIncrement();
            if( index.get() == componentConnectionPoolList.size())
                index = new AtomicInteger(index.get() % componentConnectionPoolList.size());
        }
    }

    private void sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
