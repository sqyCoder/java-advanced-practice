package thread;

// 先创建一个 Thread 的子类
class MyThread extends Thread {
    // 重写 run 方
    public void run() {
        // 让新线程执行的逻辑就在这个 run 中进行编写了.
        while (true) {
            System.out.println("hello thread");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

public class Demo1 {
    public static void main(String[] args) {
        // 创建实例, 只是准备工作.
        Thread t = new MyThread();

        // 真正创建出线程, 还需要通过另一个方法
        t.start();
        // t.run();
        while (true) {
            System.out.println("hello world");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}