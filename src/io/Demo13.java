package io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Demo13 {
    public static void main(String[] args) {
        try (InputStream inputStream = new FileInputStream("d:/test.txt")) {

            Scanner scanner = new Scanner(inputStream);
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            int c = scanner.nextInt();
            int d = scanner.nextInt();
            System.out.println(a + ", " + b + ", " + c + ", " + d);

        } catch (IOException e) {
            e.printStackTrace();
        }
//        Scanner scanner = new Scanner(System.in);
//        scanner.next();
//        scanner.nextInt();
//        scanner.nextDouble();
        // ....
    }
}