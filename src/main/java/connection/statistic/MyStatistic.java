package connection.statistic;

import connection.pool.ComponentConnectionPool;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class MyStatistic {

    private HashMap<ComponentConnectionPool ,Integer> countMap = new HashMap<>();

    public synchronized void update(ComponentConnectionPool connectionPool) {
        Integer count = countMap.getOrDefault(connectionPool, 0);
        count += 1;
        countMap.put(connectionPool,count);
    }

    public void printUsage() {
        for (ComponentConnectionPool componentConnectionPool : countMap.keySet()) {
            System.out.println("componentConnectionPool = " + countMap.get(componentConnectionPool));
        }
    }
}
