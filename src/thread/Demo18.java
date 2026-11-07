package thread;

public class Demo18 {
    // 此处线程级变量, 就是一个 Integer
    // ThreadLocal 通常会定义成 static final 形式的~~
    // 确保 tl 指向的实例是一个 "单例的"
    private static final ThreadLocal<Integer> tl = new ThreadLocal<Integer>();
    private static final ThreadLocal<Integer> tl2 = new ThreadLocal<Integer>();
    private static final ThreadLocal<Integer> tl3 = new ThreadLocal<Integer>();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            // t1 线程就可以使用 tl 中的变量的内容.
            // 当 t1 使用 tl 的时候, 就会把 tl 里包裹的 Integer 的值, 往 t1 线程中
            // 复制一个副本, t1 后续的操作, 都是针对自己的副本进行的.
            for (int i = 0; i < 100; i++) {
                tl.set(i);
            }
            System.out.println("t1: " + tl.get());
            // 销毁当前线程的副本, 不会影响到其他线程的副本, 也不会影响本尊~~
            tl.remove();
        });

        Thread t2 = new Thread(() -> {
            // t2 线程使用 tl 的变量, 也会把副本拷贝到 t2 内部.
            // 此时 t2 操作的 tl 的值和 t1 毫不相关
            for (int i = 0; i < 1000; i++) {
                tl.set(i);
            }
            System.out.println("t2: " + tl.get());
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        // 主线程中没 set 过, get 得到的就是 null
        System.out.println("main: " + tl.get());
    }
}