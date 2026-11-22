package thread;

import java.util.concurrent.*;

/**
 * Demo20: ThreadPoolExecutor seven parameters explanation
 * 
 * ThreadPoolExecutor's seven parameters:
 * 1. corePoolSize: core thread pool size
 * 2. maximumPoolSize: maximum thread pool size
 * 3. keepAliveTime: keep-alive time
 * 4. unit: time unit for keep-alive time
 * 5. workQueue: work queue
 * 6. threadFactory: thread factory
 * 7. handler: rejection policy
 * 
 * Example: Create a fixed thread pool and demonstrate its seven parameter configuration
 */
public class Demo20 {

    public static void main(String[] args) {
        // Demonstrate ThreadPoolExecutor's seven parameters
        // corePoolSize=5, maximumPoolSize=10, keepAliveTime=0L, unit=TimeUnit.SECONDS,
        // workQueue=new LinkedBlockingQueue<>(100), threadFactory=new DefaultThreadFactory(),
        // handler=new DiscardPolicy()
        
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                5,                    // corePoolSize: core thread pool size
                10,                   // maximumPoolSize: maximum thread pool size
                0L,                   // keepAliveTime: keep-alive time
                TimeUnit.SECONDS,     // unit: time unit
                new LinkedBlockingQueue<>(100), // workQueue: work queue
                new DefaultThreadFactory(), // threadFactory: thread factory
                new DiscardPolicy()   // handler: rejection policy
        );
        
        System.out.println("ThreadPoolExecutor created with 7 parameters");
        System.out.println("Core pool size: 5");
        System.out.println("Maximum pool size: 10");
        System.out.println("Work queue capacity: 100");
        
        // Submit tasks for demonstration
        for (int i = 0; i < 15; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Task " + taskId + " running by thread: " + Thread.currentThread().getName());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        // Shutdown the thread pool
        executor.shutdown();
        
        // Wait for all tasks to complete
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
        
        System.out.println("All tasks completed");
    }
    
    /**
     * Custom thread factory
     */
    static class DefaultThreadFactory implements ThreadFactory {
        private final AtomicInteger counter = new AtomicInteger(1);
        
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "Demo20-Worker-" + counter.getAndIncrement());
            System.out.println("Created thread: " + t.getName());
            return t;
        }
    }
    
    /**
     * Custom rejection policy: discard the task
     */
    static class DiscardPolicy implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println("Task rejected - discarded");
        }
    }
}