package thread;

import java.util.concurrent.atomic.AtomicInteger;

public class Demo19 {
    // 不再使用 int, 使用原子类
    // private static int count = 0;
    private static AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 50000; i++) {
                // count++;
                count.getAndIncrement();
                // ++count;
//                count.incrementAndGet();
//                // count--;
//                count.getAndDecrement();
//                // --count;
//                count.decrementAndGet();
//                // count += n;
//                count.addAndGet();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 50000; i++) {
                // count++;
                count.getAndIncrement();
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("count = " + count.get());
    }
}