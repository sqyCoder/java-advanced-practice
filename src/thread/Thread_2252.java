package thread;

/**
 * 作业 2252：顺序打印（进阶版）
 *
 * 要求：三个线程，分别只打印 A、B、C，按 ABC 的顺序打印 10 轮。
 *
 * 【和 join 版最大的区别】
 *   join 版：每轮都 new 三个新线程，10 轮下来创建了 30 个线程，干完就死。
 *   这个版本：只创建 3 个线程，从开头活到结尾，"睡—醒—打一次—再睡"，循环 10 次。
 *
 * 【三个零件】
 *   COUNTER        ：叫号机。记录"已经打到第几号了"，从 0 数到 29。
 *                    除以 3 的余数 0/1/2，正好对应 A/B/C。
 *   lock           ：一把锁。保证同一时刻只有一个线程在"看号 + 打印 + 改号"。
 *   wait/notifyAll ：排队机制。不是我的号就睡；打完把号 +1，再喊所有人重新看号。
 */
public class Thread_2252 {

    // 叫号机：当前轮到第几号打印。
    // 0 → A，1 → B，2 → C，3 → A，4 → B，5 → C ……
    // volatile 保证多线程读到的是最新值。
    // （实际上所有读写都在 synchronized 里，不加也正确，老师加上属于防御性写法）
    private static volatile int COUNTER = 0;

    // 锁对象。起个"门锁"的作用，里面不装任何数据。
    private static Object lock = new Object();

    public static void main(String[] args) {

        // ==================== 线程 A（名字叫 "A"） ====================
        Thread t1 = new Thread(() -> {

            // 这个线程一共要打 10 次 A
            for (int i = 0; i < 10; i++) {

                // ① 拿锁进门。同一时刻只允许一个人在这对大括号里
                synchronized (lock) {

                    // ② 看号：只有 0、3、6、9…… 这些"除以 3 余 0"的号才轮到 A
                    //    不是我的号，就一直待在里面循环（每次醒来都重新检查一遍）
                    while (COUNTER % 3 != 0) {
                        try {
                            // ③ 放下锁，去旁边等着。
                            //    注意：必须放锁，不然别人进不来，号永远不会变
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    // 能走到这里，说明 COUNTER % 3 == 0，轮到我 A 了

                    // ④ 打印自己的线程名 "A"。
                    //    用 print 而不是 println：不换行，让 A、B、C 连在同一行
                    System.out.print(Thread.currentThread().getName());

                    // ⑤ 号数 +1，把下一个号让出去
                    COUNTER++;

                    // ⑥ 喊一嗓子："号变了，都醒来看看是不是自己"
                    lock.notifyAll();
                }
                // ⑦ 走出大括号 → 自动释放锁 → 进入下一轮 for 循环，继续抢锁
            }
        }, "A");   // 第二个参数是线程名，所以 getName() 拿到的是 "A"

        // ==================== 线程 B ====================
        // 结构和 A 完全一样，唯一区别：③ 那里判断的余数是 1
        Thread t2 = new Thread(() -> {

            for (int i = 0; i < 10; i++) {
                synchronized (lock) {

                    // ② B 只认"除以 3 余 1"的号：1、4、7、10……
                    while (COUNTER % 3 != 1) {
                        try {
                            lock.wait();        // 不是我的号 → 放锁睡觉
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    // ④ 还是 print，不换行
                    System.out.print(Thread.currentThread().getName());
                    COUNTER++;
                    lock.notifyAll();
                }
            }
        }, "B");

        // ==================== 线程 C ====================
        // 结构也一样，区别：③ 判断余数是 2；④ 换成 println（负责换行）
        Thread t3 = new Thread(() -> {

            for (int i = 0; i < 10; i++) {
                synchronized (lock) {

                    // ② C 只认"除以 3 余 2"的号：2、5、8、11……
                    while (COUNTER % 3 != 2) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    // ④ 用 println！因为 C 是一组的最后一个，
                    //    打完要换行，下一组 ABC 才能另起一行
                    System.out.println(Thread.currentThread().getName());

                    COUNTER++;
                    lock.notifyAll();
                }
            }
        }, "C");

        // ==================== 启动三个线程 ====================
        // start 只是"让他们开跑"，谁先抢到锁由 CPU 决定，不影响结果
        // 因为能不能打印不看谁先抢到，只看自己的号对不对
        t1.start();
        t2.start();
        t3.start();
    }
}

/*
 * ==================== 完整执行过程 ====================
 *
 * 初始：COUNTER = 0，三个线程都还没进锁
 *
 * 假设 A 先抢到锁：
 *   A：0 % 3 = 0，是我的号 → 打印 "A" → COUNTER 变 1 → notifyAll → 出门
 *
 * 假设这次 B 抢到锁：
 *   B：1 % 3 = 1，是我的号 → 打印 "B" → COUNTER 变 2 → notifyAll → 出门
 *
 * 假设 C 抢到锁：
 *   C：2 % 3 = 2，是我的号 → 打印 "C" + 换行 → COUNTER 变 3 → notifyAll → 出门
 *
 * 第二轮：
 *   A：3 % 3 = 0，又轮到我 → 打印 "A" → COUNTER 变 4
 *   B：4 % 3 = 1 → 打印 "B"
 *   C：5 % 3 = 2 → 打印 "C" + 换行
 *
 * ……直到 COUNTER 到 30，三个线程各自的 for 循环也都跑完 10 次，全部结束。
 *
 *
 * ==================== 如果抢错了号会怎样 ====================
 *
 * 比如 A 刚打完，COUNTER = 1，这时候 C 抢到了锁：
 *   C：1 % 3 = 1 ≠ 2，不是我的号 → 执行 lock.wait()
 *      → 放下锁，进入等待队列
 *      → B 才有机会进来，看到 1 % 3 = 1，是我的号，打印 B
 *
 * 所以就算 C 抢到锁也没关系，它自己会让开。
 * 这就是 while + wait 的作用：抢到锁 ≠ 能打印，还得号对。
 *
 *
 * ==================== 三个必看的细节 ====================
 *
 * 1. wait() 如果不放锁会怎样？
 *    → 别人进不来，COUNTER 永远不变，所有人一起睡死（死锁）。
 *    → wait 的核心就是"把锁交出去再睡"，这是它和 sleep 的根本区别。
 *
 * 2. 为什么是 while 不是 if？
 *    → 被 notifyAll 叫醒后，号可能又被别人改了，必须重新检查一遍。
 *    → 用 if 只检查一次，可能打出多余的字符。
 *
 * 3. 为什么 A、B 用 print，只有 C 用 println？
 *    → A 和 B 打完不能换行，否则会变成三行。
 *    → C 是这一组的最后一个，打完换行，下一组 ABC 才能另起一行。
 */
