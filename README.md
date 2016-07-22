# Pool
  This project creates a pool to manage connections to a simulated service. There's no limit in the number of threads 
requesting to connect, however internally idle connections are reassigned without the task being aware. When an idle task 
requests a new connection it may borrow a free one from the pool or it might be put on hold until another becomes available.
  In order to simulate loads a threadpool is being used and tasks have a set of random values to determine number of petitions
to a connection and/or number of times to execute these random jobs- simulating diferent batches of commands per task 
(requestor). Delays are also being randomized to create a more realistic test.

Class rdv.com.pool.util.task contains the set of values that control randomnes
	  private static final int MAX_ACTIONS = 20;
    private static final int MIN_ACTIONS = 2;
    private static final int MAX_DELAY = 2000;
    private static final int MIN_DELAY = 10;
    private static final int REPETITION_SCORE = 2;


Test rdv.com.pool.PoolTest contains one test that runs the whole project.
