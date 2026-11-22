package thread;

public class MyDemo3  {
    public static void main(String[] args) throws InterruptedException {
    int[] count = {0};
    Object lock = new Object();
    Thread t1 = new Thread(() -> {
        for (int i = 0; i < 10000; i++) {
            synchronized (lock) {
                count[0]++;
            }
            
        }
    });

    Thread t2 = new Thread(() -> {
        for (int i = 0; i < 10000; i++) {
            synchronized (lock) {
                count[0]++;
            }
        }
    });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Final count: " + count[0]);
}
}
