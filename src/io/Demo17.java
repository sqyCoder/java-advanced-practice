package io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Scanner;

public class Demo17 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // 1. 让用户输入要搜索的根目录
        System.out.println("请输入要搜索的根目录: ");
        String rootPath = scanner.next();
        // 2. 让用户输入一个要搜索的关键字.
        System.out.println("请输入要搜索的关键字: ");
        String word = scanner.next();

        // 3. 验证用户输入是否合法.
        if (word.isEmpty()) {
            System.out.println("输入的关键字为空!");
            return;
        }
        File rootFile = new File(rootPath);
        if (!rootFile.isDirectory()) {
            System.out.println("输入的根目录有误!");
            return;
        }

        // 4. 进行递归搜索
        search(rootFile, word);
    }

    private static void search(File rootFile, String word) {
        // 1. 列出 rootFile 中所有的内容
        File[] files = rootFile.listFiles();
        if (files == null) {
            return;
        }
        // 2. 遍历每个文件对象
        for (File file : files) {
            if (file.isFile()) {
                // 读取文件内容, 进行进一步判定
                trySearch(file, word);
            } else if (file.isDirectory()) {
                // 递归调用搜索函数
                search(file, word);
            }
        }
    }

    private static void trySearch(File file, String word) {
        // 1. 先判定文件名是否包含
        if (file.getName().contains(word)) {
            System.out.println("找到了匹配的文件! 文件名匹配! " + file.getAbsolutePath());
            return;
        }

        // 2. 在判定文件内容是否包含
        //    把文件完整的读出来, 进行字符串查找.
        //    由于是拿着 word 字符串进行匹配, 按照字符的方式读取文件比较合适.
        //    把字符串二进制数据中进行匹配意义不大.
        try (Reader reader = new FileReader(file)) {
            // 读取出来的文件结果
            StringBuilder result = new StringBuilder();

            while (true) {
                char[] chars = new char[1024];
                int n = reader.read(chars);
                if (n == -1) {
                    break;
                }
                result.append(chars, 0, n);
            }

            // 判定 StringBuilder 中是否包含 word
            if (result.indexOf(word) >= 0) {
                // 找到了
                System.out.println("找到了匹配的文件! 内容匹配! " + file.getAbsolutePath());
            } else {
                // 没找到
                return;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}