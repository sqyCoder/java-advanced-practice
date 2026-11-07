package thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// 固定线程数目的线程池.
class MyThreadPool {
    private BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

    // 参数 n 表示线程池的线程数目.
    public MyThreadPool(int n) {
        for (int i = 0; i < n; i++) {
            Thread t = new Thread(() -> {
                try {
                    while (true) {
                        Runnable task = queue.take();
                        task.run();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            // 其他前台线程结束, 线程池也随着结束.
            t.setDaemon(true);
            t.start();
        }
    }

    // 往线程池中添加新的任务.
    public void submit(Runnable task) throws InterruptedException {
        queue.put(task);
    }
}

public class Demo15 {
    public static void main(String[] args) throws InterruptedException {
        MyThreadPool pool = new MyThreadPool(4);
        for (int i = 0; i < 1000; i++) {
            int id = i;
            pool.submit(() -> {
                Thread cur = Thread.currentThread();
                System.out.println("hello" + cur.getName() + ", " + id);
            });
        }
        // 加 sleep 确保线程池的线程有足够的时间执行完任务.
        Thread.sleep(1000);
    }
}