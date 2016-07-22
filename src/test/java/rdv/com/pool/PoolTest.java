/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdv.com.pool;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import rdv.com.pool.util.task.Task;
import rdv.com.pool.util.task.TaskQueue;

/**
 *
 * @author rbn
 */
public class PoolTest {

    /**
     * Test of main method, of class Pool.
     */
    @Test
    public void testMain() {
        for (int i = 0; i < 1000; i++) {
            TaskQueue.INSTANCE.enqueue(new Task());
        }
        TaskQueue.INSTANCE.shutdown();
    }

}
