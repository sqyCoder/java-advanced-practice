package io;

import java.io.File;
import java.util.Scanner;

public class Demo5 {
    public static void main(String[] args) {
        File file = new File("./test.txt");
        System.out.println(file.exists());
        // file.delete();
        file.deleteOnExit();
        System.out.println(file.exists());

        // 通过 next 阻塞主线程.
        Scanner scanner = new Scanner(System.in);
        scanner.next();
    }
}