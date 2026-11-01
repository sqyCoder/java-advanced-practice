package thread;

public class Demo10 {
    private static Object locker = new Object();

    private static void func() {
        synchronized (locker) {

        }
    }

    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            synchronized (locker) {
                func();
            }
        });
        t.start();
    }
}