/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdv.com.pool.connection;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import rdv.com.pool.connection.driver.ServiceDriver;

/**
 *
 * @author rbn
 */
public enum ConnectionPool {
    INSTANCE();

    private static ConcurrentLinkedQueue<Connection> freeConnections;
    private static ConcurrentLinkedDeque<ConnectionContext> awakeContext;
    private static Map<Integer, ConnectionContext> asleepContext;
    private static ConcurrentLinkedQueue<ConnectionContext> freeContext;

    ConnectionPool() {
        initialize(20);
    }

    public Connection acquireConnection() {
        Connection connection = freeContext.poll();
        if (connection != null) {
            return connection;
        }
        connection = freeConnections.poll();
        if (connection != null) {
            ConnectionContext conn = new ConnectionContext(connection);
            sleepConnection(conn);
            return conn;
        }
        while (freeConnections.isEmpty() && freeContext.isEmpty()) {
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException ex) {
                }
            }
        }

        return acquireConnection();

    }

    public void releaseConnection(Connection connection) {
        ConnectionContext connectionContext = (ConnectionContext) connection;
        freeConnection(connectionContext);
        synchronized (this) {
            this.notify();
        }
    }

    void sleepConnection(ConnectionContext context) {
        freeConnections.add(context.sleep());
        asleepContext.put(context.getId(), context);
        wake();
    }

    void freeConnection(ConnectionContext context) {
        asleepContext.remove(context.getId());
        freeContext.add(context);
        wake();
    }

    void wakeUp(ConnectionContext context) {
        asleepContext.remove(context.getId());
        awakeContext.add(context);
        wake();
    }

    private void wake() {
        Runnable retrieve = () -> {
            ConnectionContext context = awakeContext.pollFirst();
            Connection connection = freeConnections.poll();
            if (context != null && connection != null) {
                synchronized (context) {
                    context.wakeUp(connection);
                    context.notify();
                }
            } else {
                if (connection != null) {
                    freeConnections.add(connection);
                }
                if (context != null) {
                    awakeContext.addFirst(context);
//                    context.waitForConnection();
                }
            }
        };
        new Thread(retrieve).start();
    }

    private void initialize(int size) {
        freeConnections = new ConcurrentLinkedQueue<>();
        freeContext = new ConcurrentLinkedQueue<>();
        awakeContext = new ConcurrentLinkedDeque<>();
        asleepContext = new ConcurrentHashMap<>();
        for (int i = 0; i < size; i++) {
            freeContext.add(new ConnectionContext(new ServiceDriver()));
        }
    }

    public void reset(int size) {
        initialize(size);
    }

    public int availableConnections() {
        return freeConnections.size();
    }

}
