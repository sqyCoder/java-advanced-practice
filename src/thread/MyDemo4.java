package thread;

public class MyDemo4 {
    private static Object locker1 = new Object();
    private static Object locker2 = new Object();

    public static void main(String[] args) {
        // 线程 t1：先拿锁1，再拿锁2
        Thread t1 = new Thread(() -> {
            synchronized (locker1) {
                System.out.println("t1 拿到了 locker1，准备去拿 locker2");
                try { Thread.sleep(1000); } catch (InterruptedException e) {}
                
                synchronized (locker2) {
                    System.out.println("t1 成功拿到了 locker2");
                }
            }
        });

        // 线程 t2：先拿锁2，再拿锁1（刚好跟 t1 反过来！）
        Thread t2 = new Thread(() -> {
            synchronized (locker2) {
                System.out.println("t2 拿到了 locker2，准备去拿 locker1");
                try { Thread.sleep(1000); } catch (InterruptedException e) {}
                
                synchronized (locker1) {
                    System.out.println("t2 成功拿到了 locker1");
                }
            }
        });

        t1.start();
        t2.start();
    }
}