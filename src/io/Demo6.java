package io;

import java.io.File;
import java.io.IOException;

public class Demo6 {
    public static void main(String[] args) throws IOException {
        File file = new File("d:/test.txt");
        // 如果当前的目录是空的目录, 返回的结果是 null
//        String[] fileNames = file.list();
//        if (fileNames == null) {
//            System.out.println("空目录");
//            return;
//        }
//        for (String fileName : fileNames) {
//            System.out.println(fileName);
//        }

        File[] files = file.listFiles();
        if (files == null) {
            System.out.println("空目录");
            return;
        }
        for (File f : files) {
            System.out.println(f.getCanonicalPath());
        }
    }
}