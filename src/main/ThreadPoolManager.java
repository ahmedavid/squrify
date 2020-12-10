package main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// Use Java Executor API to manage pool of threads
// In order to avoid overhead of creating and destroying a new thread for every task
public class ThreadPoolManager {
    private ExecutorService pool;

    public ThreadPoolManager() {
        // Find out how many cores we have
        int cores = Runtime.getRuntime().availableProcessors();
        // Intitialize thread pool size of our cores
        System.out.printf("Your computer seems to have %d cores\n", cores);
        // If we have more than 2 cores , leave one for main thread
        if(cores > 2) cores--;
        pool = Executors.newFixedThreadPool(cores);
    }

    // Comment / Uncomment introduceArtificialDelay() to introduce artificial delay
    // for slower progress for better observation
    public void submitTask(Runnable task) {
        pool.execute(task);
    }

    public void shutdown() {
        this.pool.shutdown();
    }

    public boolean awaitTermination() {
        try {
            return pool.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("Taking too long, abort!");
            e.printStackTrace();
        }
        return false;
    }
}
