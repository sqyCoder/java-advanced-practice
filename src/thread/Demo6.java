package thread;

import java.util.Scanner;

public class Demo6 {
    private static boolean isRunning = true;

    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            while (isRunning) {
                System.out.println("hello thread");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("t 线程终止");
        });
        t.start();

        // 在需要的时候, 把 isRunning 设为 false
        Scanner scanner = new Scanner(System.in);
        scanner.next();

        isRunning = false;
    }
}