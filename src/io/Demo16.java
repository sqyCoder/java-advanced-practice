package io;

import java.io.*;
import java.util.Scanner;

public class Demo16 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // 1. 让用户输入, 要复制的文件路径, 和要复制到的目标路径.
        System.out.println("请输入要复制的源文件路径: ");
        String srcPath = scanner.next();
        System.out.println("请输入要复制到的目标文件路径: ");
        String destPath = scanner.next();

        // 2. 验证用户输入的路径是否合法.
        File srcFile = new File(srcPath);
        if (!srcFile.isFile()) {
            System.out.println("源文件路径错误!");
            return;
        }
        // 不是判定 destPath 本身是否存在, 而是要判定 destPath 的 parent 是否存在且是目录.
        File destFile = new File(destPath);
        File destParent = destFile.getParentFile();
        if (!destParent.isDirectory()) {
            System.out.println("目标文件路径错误!");
            return;
        }

        // 3. 复制. 把两个文件打开, 读取 srcFile 的内容, 写入 destFile
        //    此处需要打开文件, 参数直接使用 File 对象构造流对象.
        try (InputStream inputStream = new FileInputStream(srcFile);
             OutputStream outputStream = new FileOutputStream(destFile)) {
            while (true) {
                byte[] bytes = new byte[1024];
                int n = inputStream.read(bytes);
                if (n == -1) {
                    break;
                }
                outputStream.write(bytes, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("复制完成!");
    }
}