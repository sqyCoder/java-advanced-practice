package thread;
import java.util.Random;

public class MyDemo1 {
    public static void main(String[] args) throws InterruptedException {
        long[] arr = new long[10000000];
        Random random = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(100) + 1; // 生成1到100之间的随机整数   
        }
        long[] sum1 = {0};
        long[] sum2 = {0};

        long startTime = System.currentTimeMillis();
        //子线程1
        Thread t1 = new Thread(() -> {
                for (int i = 0; i < arr.length; i += 2) {
                    sum1[0] += arr[i];
                }
        });

        //子线程2
        Thread t2 = new Thread(() -> {
                for (int i = 1; i < arr.length; i += 2) {
                    sum2[0] += arr[i];
                }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        long sum = sum1[0] + sum2[0];
        System.out.println("sum = " + sum);
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken = " + (endTime - startTime) + " ms");
    }
}