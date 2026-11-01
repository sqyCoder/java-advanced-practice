package thread;

import java.util.Vector;

public class Demo11 {
    private static Object locker1 = new Object();
    private static Object locker2 = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            synchronized (locker1) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                synchronized (locker2) {
                    System.out.println("t1");
                }
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (locker1) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                synchronized (locker2) {
                    System.out.println("t2");
                }
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        Vector<Integer> v = new Vector<>();
        v.add(1);
        v.size();
    }
}