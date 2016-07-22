/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdv.com.pool.connection;

import rdv.com.pool.connection.Connection;
import rdv.com.pool.connection.ConnectionPool;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rbn
 */
public class ConnectionPoolTest {

    public ConnectionPoolTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        ConnectionPool.INSTANCE.availableConnections();
    }

    @AfterClass
    public static void tearDownClass() {
        ConnectionPool.INSTANCE.reset(20);
    }

    @Before
    public void setUp() {
        ConnectionPool.INSTANCE.reset(20);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getInstance method, of class ConnectionPool.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        ConnectionPool result = ConnectionPool.INSTANCE;
        assertNotNull(result);
    }

    /**
     * Test of acquireConnection method, of class ConnectionPool.
     */
    @Test
    public void testAcquireConnection() {
        System.out.println("acquireConnection");
        ConnectionPool instance = ConnectionPool.INSTANCE;
        Connection result = instance.acquireConnection();
        assertNotNull(result);
    }

    /**
     * Test of releaseConnection method, of class ConnectionPool.
     */
    @Test
    public void testReleaseConnection() {
        System.out.println("releaseConnection");
        ConnectionPool instance = ConnectionPool.INSTANCE;
        Connection connection = instance.acquireConnection();
        connection.retrieve(new StringBuilder("release"));
        instance.releaseConnection(connection);
        int connectionsAfter = instance.availableConnections();
        assertEquals("releaseConnection result", 1, connectionsAfter);
    }

    /**
     * Test of sleepConnection method, of class ConnectionPool.
     */
    @Test
    public void testSleepConnection() {
        System.out.println("sleepConnection");
        ConnectionPool instance = ConnectionPool.INSTANCE;
        int connectionsBefore = instance.availableConnections();
        Connection connection = instance.acquireConnection();
        int connectionsAfter = instance.availableConnections();
        assertEquals(connectionsBefore, connectionsAfter);
    }

    /**
     * Test of freeConnection method, of class ConnectionPool.
     */
    @Test
    public void testFreeConnection() {
        System.out.println("FreeConnection");
        ConnectionPool instance = ConnectionPool.INSTANCE;
        Connection connection = instance.acquireConnection();
        connection.retrieve(new StringBuilder("free"));
        connection.retrieve(new StringBuilder("free2"));
        connection.retrieve(new StringBuilder("free3"));
        int connectionsAfter = instance.availableConnections();
        assertEquals(1, connectionsAfter);
    }

    /**
     * Test of wakeUp method, of class ConnectionPool.
     */
    @Test
    public void testWakeUp() throws InterruptedException {
        System.out.println("wakeUp");
        ConnectionPool instance = ConnectionPool.INSTANCE;
        Connection connection = instance.acquireConnection();
        connection.retrieve(new StringBuilder("wake"));
        Thread.sleep(1000);
        connection.retrieve(new StringBuilder("wake2"));
        Thread.sleep(1000);
        int connectionsAfter = instance.availableConnections();
        assertEquals(1, connectionsAfter);
    }

}
