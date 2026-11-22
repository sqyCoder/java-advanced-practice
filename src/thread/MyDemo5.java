package thread;

public class MyDemo5 {
    public static void main(String[] args) {

        Thread c = new Thread(() -> {
            System.out.println(Thread.currentThread().getName());
        }, "c");

        Thread b = new Thread(() -> {
            try {
                c.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
        }, "b");

        Thread a = new Thread(() -> {
            try {
                b.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName());
        }, "a");
        a.start();
        b.start();
        c.start();
    }
}
