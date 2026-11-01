package thread;

public class Demo7 {
    private static int result1 = 0;
    private static int result2 = 0;

    public static void main(String[] args) throws InterruptedException {
        // 希望计算 1 + 2 + 3 .... + 2000
        // 此时使用 t1 线程计算 1 + ... + 1000
        // 使用 t2 线程计算 1001 + ... + 2000
        // 再在主线程中, 汇总结果

        Thread t1 = new Thread(() -> {
            for (int i = 1; i <= 1000; i++) {
                result1 += i;
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 1001; i <= 2000; i++) {
                result2 += i;
            }
        });

        t1.start();
        t2.start();

        // 添加上线程等待
        t1.join();
        t2.join();

        System.out.println(result1 + result2);
    }
}