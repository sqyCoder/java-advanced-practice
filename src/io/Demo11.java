package io;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Demo11 {
    public static void main(String[] args) {
        try (Reader reader = new FileReader("d:/test.txt")) {

//            while (true) {
//                int n = reader.read();
//                if (n == -1) {
//                    break;
//                }
//                char c = (char) n;
//                System.out.println(c);
//            }

            while (true) {
                char[] chars = new char[1024];
                int n = reader.read(chars);
                if (n == -1) {
                    break;
                }
                for (int i = 0; i < n; i++) {
                    System.out.println(chars[i]);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}