/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdv.com.pool.connection.driver;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import rdv.com.pool.connection.Connection;

/**
 *
 * @author rbn
 */
public class ServiceDriver implements Connection {

    private static final AtomicInteger SERIAL = new AtomicInteger(0);
    private static int MAX_DELAY = 2000;
    private static int MIN_DELAY = 10;

    private int id;

    public ServiceDriver() {
        id = SERIAL.getAndIncrement();
    }

    @Override
    public void retrieve(StringBuilder msg) {
        printService(msg);
//        synchronized (this) {
//            try {
//                this.wait(ThreadLocalRandom.current().nextInt(MIN_DELAY, MAX_DELAY));
//            } catch (InterruptedException ex) {
////            Logger.getLogger(ServiceDriver.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
    }

    @Override
    public void clear() {
    }

    @Override
    public int getId() {
        return id;
    }

    public StringBuilder getLog(StringBuilder msg) {
        return msg.append("Service-" + id + "|");
    }

    public void printService(StringBuilder msg) {
        System.out.println(new StringBuilder().append("|").append(System.nanoTime()).append("|").append(getLog(msg)));
    }

}
