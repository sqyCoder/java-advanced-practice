package thread;

import java.util.PriorityQueue;

// =========================================================================
// 第一个类：MyTimerTask（任务类）
// 作用：用来包装你要执行的任务，并记录这个任务应该在什么时间点被执行。
// =========================================================================
class MyTimerTask implements Comparable<MyTimerTask> {
    // 存放你要执行的具体代码（比如打印一句话）
    private Runnable runnable;
    // 存放任务要执行的绝对时间点（是一个毫秒级的时间戳）
    private long time;

    // 构造方法：创建任务时，传入任务内容和延迟时间
    public MyTimerTask(Runnable runnable, long delay) {
        this.runnable = runnable;
        // 核心逻辑：
        // System.currentTimeMillis() 获取当前时刻的时间戳。
        // 加上 delay（延迟的毫秒数），就等于任务最终要执行的那个绝对时间点。
        // 例如：现在是 10:00:00 (时间戳10000)，延迟 3000 毫秒，执行时间就是 13000 这个时间戳。
        this.time = System.currentTimeMillis() + delay;
    }

    // 提供给外部获取这个任务的执行时间
    public long getTime() {
        return time;
    }

    // 执行任务：调用传入的 runnable 的 run 方法
    public void run() {
        runnable.run();
    }

    // 实现 Comparable 接口，重写 compareTo 方法。
    // 作用：告诉 Java 的优先队列（PriorityQueue），该怎么给这些任务排序。
    @Override
    public int compareTo(MyTimerTask o) {
        // 返回值规则：
        // 如果返回负数，说明当前任务（this）排在参数任务（o）前面。
        // 如果返回正数，说明当前任务排在参数任务后面。
        // 如果返回 0，说明两个任务时间一样，谁前谁后无所谓。

        // 因为我们要让时间越早的任务越先执行，所以用当前任务的执行时间 减去 另一个任务的执行时间。
        // 谁的时间小（越早），减出来的结果就是负数，就会排到队头。
        // 注意：这里强转成 int 会有精度丢失风险（如果时间相差极小），但一般业务中毫秒差足够大，没问题。
        return (int) (this.time - o.time);
    }
}

// =========================================================================
// 第二个类：MyTimer（定时器核心类）
// 作用：接收任务，并在后台默默计时，时间一到就执行任务。
// =========================================================================
class MyTimer {
    // 使用优先队列（PriorityQueue）来保存所有任务。
    // 为什么用它？因为它底层是一个小顶堆，每次 add 进去任务时，它会自动把执行时间最早的任务放到队列的最前面（队头）。
    // 这样我们每次只要看队头，就知道下一个该执行谁了。
    private PriorityQueue<MyTimerTask> queue = new PriorityQueue<>();

    // 创建一个普通的 Object 对象，专门用来当锁（因为要在 synchronized 中使用）。
    private Object locker = new Object();

    // 构造方法：定时器一旦创建，就在内部启动一个后台线程，专门用来检查时间。
    public MyTimer() {
        Thread t = new Thread(() -> {
            try {
                // 这是一个死循环，后台线程会一直活着，不断检查任务。
                while (true) {
                    // 因为队列是共享资源（别的线程也会往里面加任务），所以操作队列必须加锁。
                    synchronized (locker) {
                        // 1. 查看队头任务（peek 只读取，不把任务从队列里拿走）
                        MyTimerTask task = queue.peek();

                        // 2. 如果队列为空（没有任务）
                        while (task == null) {
                            // 必须让后台线程等待（wait）。
                            // 如果不 wait，而是用 continue 空转，会瞬间吃满 CPU 资源（这叫忙等）。
                            // wait 会释放锁，并让线程休眠，直到有新任务加进来时，被 schedule 方法里的 notify 唤醒。
                            locker.wait();
                            // 被唤醒后，重新读取一下队头任务。
                            task = queue.peek();
                        }

                        // 3. 检查队头任务的时间是否到了。
                        long curTime = System.currentTimeMillis();
                        // 如果当前时间 >= 任务记录的执行时间，说明时间到了，必须马上执行。
                        if (curTime >= task.getTime()) {
                            // 执行任务
                            task.run();
                            // 执行完毕后，必须把任务从队列里踢出去（poll 会把队头移除）。
                            // 如果不移除，下一轮循环还会检查到这个任务，就会无限重复执行。
                            queue.poll();
                        } else {
                            // 4. 如果时间还没到。
                            // 算出还需要等待多久：任务时间 - 当前时间。
                            // 调用带参数的 wait(需要等待的毫秒数)。
                            // 这样线程会休眠对应的时间，把 CPU 让出来。时间一到，它会自动醒来。
                            // 如果这期间有新任务加进来（比如加了一个比当前任务还早的任务），notify 会提前把它叫醒，重新检查。
                            locker.wait(task.getTime() - curTime);
                        }
                    }
                }
            } catch (InterruptedException e) {
                // 如果在 wait 期间线程被强行中断，抛出运行时异常。
                throw new RuntimeException("wait 被终止");
            }
        });
        // 启动后台线程
        t.start();
    }

    // 提供给外部调用的方法：添加任务
    public void schedule(Runnable runnable, long delay) {
        // 因为要操作共享的队列，必须加锁。
        synchronized (locker) {
            // 1. 创建任务对象
            MyTimerTask task = new MyTimerTask(runnable, delay);
            // 2. 把任务放进优先队列，队列会自动按时间给它们排序。
            queue.add(task);
            // 3. 唤醒可能正在 wait 的后台线程。
            // 为什么要唤醒？因为后台线程可能之前看到队列是空的，正在 wait()；
            // 或者它正在睡大觉（等待某个很久之后的任务），现在新加了一个马上要执行的任务，必须把它叫醒重新看一眼。
            locker.notify();
        }
    }
}

// =========================================================================
// 第三个类：Demo34（测试类）
// =========================================================================
public class Demo17 {
    public static void main(String[] args) {
        MyTimer timer = new MyTimer();
        // 传入任务和延迟时间
        timer.schedule(() -> System.out.println("hello 3000"), 3000);
        timer.schedule(() -> System.out.println("hello 2000"), 2000);
        timer.schedule(() -> System.out.println("hello 1000"), 1000);
    }
}