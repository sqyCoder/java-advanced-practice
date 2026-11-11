package io;

import java.io.File;

public class Demo8 {
    public static void main(String[] args) {
        File source = new File("d:/install/test2.txt");
        File destination = new File("d:/test.txt");
        source.renameTo(destination);
    }
}