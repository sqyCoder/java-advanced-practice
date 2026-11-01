# Java 进阶学习（并发 → 网络 → 框架）

> **刚起步的仓库。** 目前只有 1 个文件：多线程的第一课。
>
> 这里会按「多线程 → 网络编程 → Servlet / JSP → Spring 全家桶」的顺序推进，每一阶段都留代码和笔记。
> 不追求一次写完，只保证**每一行代码都是自己敲的**。

<p>
  <img alt="Java" src="https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk&logoColor=white">
  <img alt="状态" src="https://img.shields.io/badge/状态-持续更新中-blue?style=flat-square">
</p>

---

## 当前进度

| 阶段 | 主题 | 状态 |
| --- | --- | --- |
| ① | **多线程基础** | 🚧 进行中 |
| ② | 线程同步与锁 | ⬜ 待开始 |
| ③ | 线程池与并发工具 | ⬜ 待开始 |
| ④ | 网络编程（Socket / NIO） | ⬜ 待开始 |
| ⑤ | Servlet / JSP | ⬜ 待开始 |
| ⑥ | Spring / Spring MVC / MyBatis | ⬜ 待开始 |
| ⑦ | Spring Boot 综合项目 | ⬜ 待开始 |

---

## 已有内容

### [`src/thread/Demo1.java`](src/thread/Demo1.java) —— 创建线程的两种方式

```java
class MyThread extends Thread {
    @Override
    public void run() {
        while (true) {
            System.out.println("hello thread");
            Thread.sleep(1000);
        }
    }
}

public class Demo1 {
    public static void main(String[] args) {
        Thread t = new MyThread();
        t.start();   // ✅ 真正创建线程
        // t.run();  // ⚠️ 只是普通方法调用，不会开新线程

        while (true) {
            System.out.println("hello world");
            Thread.sleep(1000);
        }
    }
}
```

运行效果：`hello thread` 和 `hello world` 交替打印，各每秒一次——**两个线程在同时跑**。

#### 这个文件真正要记的是 `start()` 和 `run()` 的区别

这是多线程的第一个坑，也是面试第一问：

| | `t.start()` | `t.run()` |
| --- | --- | --- |
| 做了什么 | 让 JVM 调用系统 API 创建**新线程**，新线程里执行 `run()` | 在当前线程里**直接调用** `run()`，就是一个普通方法 |
| 线程数 | 2 个 | 1 个（还是 main） |
| 执行结果 | 两个循环并行，输出交替 | 单线程死循环，**`run()` 永远不返回**，`hello world` 一行都不会打印 |

> 把 `start()` 改成 `run()` 跑一次，看到「只有 hello thread 没有 hello world」，就再也忘不掉了。

#### 第二个细节：`Thread.sleep` 的异常为什么不能直接抛

`sleep` 抛的是 `InterruptedException`（受检异常）。在 `run()` 里不能往外抛——因为 `Runnable.run()` 的签名没声明任何异常，重写时**只能缩小不能扩大**异常范围。所以只能 `try/catch` 包起来。

> 这是新手第一次撞上「方法重写时异常不能扩大」这条规则的地方。

---

## 为什么单独开一个仓库

因为 `java-se-practice` 那个仓库已经 109 个文件了，从基础语法一路堆到面向对象，再加多线程和框架会很乱。

分开之后：

- **`java-se-practice`** = 语言本身（语法、类、集合、IO）
- **`java-advanced-practice`** = 往上走（并发、网络、Web、框架）

两个仓库的边界很清楚：**「写一个类」在左边，「让这个类被别人调用」在右边。**

---

## 怎么跑

```bash
cd src
javac thread/Demo1.java
java thread.Demo1
```

> 这是个死循环程序，用 `Ctrl + C` 停。

---

## 接下来要写的

按顺序，每完成一项就更新这个 README：

- [ ] `Runnable` 与 `Callable` 的区别（为什么推荐用 Runnable）
- [ ] `synchronized` 与 `volatile`（可见性、原子性、有序性）
- [ ] `wait` / `notify` 与生产者消费者模型
- [ ] `ThreadPoolExecutor` 七个参数各是什么意思
- [ ] `ConcurrentHashMap` 为什么比 `Hashtable` 快
- [ ] Socket 手写一个能收发消息的服务端
- [ ] 手写一个最简单的 Servlet 并用 Tomcat 跑起来
- [ ] Spring IoC 到底解决了什么问题

---

## 学习方式说明

这个仓库的价值不在代码本身（`Demo1` 这种代码网上有十万份），在于**每一份代码旁边都写了「为什么」**。

多线程是 Java 里最容易「看得懂但写不对」的部分——因为它的错误不是编译错误，是**运行时的竞态**。所以每个例子都要能复现问题，也要能看出修好了没有。

---

## License

个人学习存档，代码可自由参考。
