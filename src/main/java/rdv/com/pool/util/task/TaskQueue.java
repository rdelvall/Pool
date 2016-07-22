/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdv.com.pool.util.task;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author rbn
 */
public enum TaskQueue {
    INSTANCE();

    private final int threadsAmount = 50;

    private final ExecutorService executor = Executors.newFixedThreadPool(threadsAmount);

    public void enqueue(Task task) {
        executor.execute(task);
    }

    public void shutdown() {
        try {
            executor.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException ex) {
        }
    }

}
