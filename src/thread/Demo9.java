package thread;

class Counter {
    public int count = 0;

//    synchronized public void add() {
//        count++;
//    }

    public void add() {
        synchronized (this) {
            count++;
        }
    }

//    synchronized public static void func() {
//
//    }

    public static void func() {
        synchronized (Counter.class) {

        }
    }
}

public class Demo9 {
    private static Object locker = new Object();

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 50000; i++)  {
                counter.add();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 50000; i++)  {
                counter.add();
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println(counter.count);
    }
}