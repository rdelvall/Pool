/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdv.com.pool.connection;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import static rdv.com.pool.catalogs.Process.*;

/**
 *
 * @author rbn
 */
class ConnectionContext implements Connection {

    private static final AtomicInteger SERIAL = new AtomicInteger(0);
    private Connection driver;
    private int id;
    private Status status;

    public ConnectionContext(Connection driver) {
        this.driver = driver;
        init();
    }

    @Override
    public void retrieve(StringBuilder msg) {
        switch (status) {
            case FREE:
            case WAITING:
                status = Status.BUSY;
                driver.retrieve(msg);
                status = status.WAITING;
                if (driver == null) {
                    status = status.IDLE;
                    break;
                }
                ConnectionPool.INSTANCE.sleepConnection(this);
                break;
            case IDLE:
                ConnectionPool.INSTANCE.wakeUp(this);
                waitForConnection();
                retrieve(msg);
                break;
            default:
                break;

        }
    }

    synchronized public Connection sleep() {
        Connection drv = this.driver;
        this.driver = null;
        status = Status.IDLE;
        return drv;
    }

    synchronized public void wakeUp(Connection connection) {
        driver = connection;
        status = Status.WAITING;
    }

    public int getId() {
        return id;
    }

    public void waitForConnection() {
        synchronized (this) {
            while (status.equals(Status.IDLE)) {
                try {
                    this.wait();
                } catch (InterruptedException ex) {
                }
            }
        }
    }

    @Override
    public void clear() {
        init();
        if (driver != null) {
            driver.clear();
        }
    }

    private void init() {
        status = Status.FREE;
        id = SERIAL.getAndIncrement();
    }

}
