/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdv.com.pool.util.task;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import rdv.com.pool.connection.Connection;
import rdv.com.pool.connection.ConnectionPool;

/**
 *
 * @author rbn
 */
public class Task implements Runnable {

    private static final AtomicInteger SERIAL = new AtomicInteger(0);
    private static final int MAX_ACTIONS = 20;
    private static final int MIN_ACTIONS = 2;
    private static final int MAX_DELAY = 2000;
    private static final int MIN_DELAY = 10;
    private static final int REPETITION_SCORE = 2;
    private static final ConcurrentHashMap<Integer, TaskStatus> map;

    static {
        map = new ConcurrentHashMap<Integer, TaskStatus>();
        for (TaskStatus status : TaskStatus.values()) {
            map.put(status.ordinal(), status);
        }
    }

    private final int ID;
    private Connection connection;

    public Task() {
        ID = SERIAL.getAndIncrement();
    }

    private boolean processStatus(TaskStatus status) {

        boolean successful = false;
        switch (status) {
            case INIT:
                printAction("Initializing");
                work();
                successful = true;
                break;
            case READY:
                printAction("AcquiringConnection");
                connection = ConnectionPool.INSTANCE.acquireConnection();
                successful = true;
                break;
            case BUSY:
                printAction("Retrieve");
                Busy();
                successful = true;
                break;
            case DONE:
                ConnectionPool.INSTANCE.releaseConnection(connection);
                return true;
            default:
                return false;

        }

        return successful ? processStatus(getNext(status)) : successful;
    }

    synchronized private void work() {
        try {
            this.wait(ThreadLocalRandom.current().nextInt(MIN_DELAY, MAX_DELAY));
        } catch (InterruptedException ex) {
//            Logger.getLogger(ServiceDriver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public StringBuilder getLog(String action) {
        StringBuilder msg = new StringBuilder();
        return msg.append(Thread.currentThread().getName()).append("|Task-").append(ID).append("|Action-").append(action).append("|");
    }

    public StringBuilder getQuery(int job) {
        StringBuilder msg = getLog("Retrieve");
        return msg.append("Job-").append(job).append("|ConnectionId-").append(connection.getId()).append("|");
    }

    public void printAction(String action) {
        System.out.println(new StringBuilder().append("|").append(System.nanoTime()).append("|").append(getLog(action)));
    }

    public void printQuery(Long Job) {
        System.out.println(new StringBuilder().append("|").append(System.nanoTime()).append("|").append(getQuery(ID)));
    }

    private void Busy() {
        int jobs = ThreadLocalRandom.current().nextInt(MIN_ACTIONS, MAX_ACTIONS);
        for (int i = 0; i < jobs; i++) {
            connection.retrieve(getQuery(i));
        }

        if (REPETITION_SCORE > ThreadLocalRandom.current().nextInt(1, 10)) {
            work();
            processStatus(TaskStatus.BUSY);
        }
    }

    @Override
    public void run() {
        processStatus(TaskStatus.INIT);
    }

    private TaskStatus getNext(TaskStatus status) {

        if (map.containsKey(status.ordinal() + 1)) {
            return map.get(status.ordinal() + 1);
        }
        return null;
    }

    public static enum TaskStatus {
        INIT,
        READY,
        BUSY,
        DONE;
    }

}
