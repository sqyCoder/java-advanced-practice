package thread;

import java.util.Random;


public class MyDemo7 {
    // 定义一个存放数据的数组，指定大小为1000
    private int[] item = new int[1000];
    // 记录当前使用的大小
    private int size = 0;
    // 队列头
    private int head = 0;
    // 队列尾
    private int tail = 0;

    // 存入
    public void put(int val) throws InterruptedException {
        // 保证写入操作的原子性
        synchronized (this) {
            // 这里使用while循环，使每次唤醒后都重新去做一次判断
            while (size == item.length) {
                // 如果队列已满，就等待
//                System.out.println("队列满了，暂停生产...");
                this.wait();
            }
            // 插入尾部
            item[tail] = val;
            // 更新尾标的值，如果大于数组大小，从头开始计数
            tail = (tail + 1) % item.length;
            // 更新size的值
            size++;
            // 唤醒其他线程
            notifyAll();
        }

    }

    // 取出
    public int take() throws InterruptedException {
        int result = 0;
        // 保证原子性
        synchronized (this) {
            // 这里使用while循环，使每次唤醒后都重新去做一次判断
            while (size == 0) {
                // 如果队列为空，就等待
//                System.out.println("队列空了，暂停消费...");
                this.wait();
            }
            // 取出队列头的值
            result = item[head];
            // 超出数组大小从头开始计数
            head = (head + 1) % item.length;
            // 更新size的大小
            size--;
            // 唤醒其他线程
            this.notifyAll();
        }
        return result;
    }

    // 保证原子性和内存可见性
    public synchronized int getSize() {
        return size;
    }

    public static void main(String[] args) {
        // 实例化自定义的阻塞队列
        MyBlockingQueue blockingQueue = new MyBlockingQueue();

        // 定义消费者线程
        Thread customer = new Thread(() -> {
            while (true) {
                try {
                    // 取出数据进行消费
                    int num = blockingQueue.take();
                    System.out.println("消费了：" + num + ", 队列大小: \t" + blockingQueue.getSize());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "customer");
        // 启动消费者
        customer.start();

        // 定义生产线程
        Thread producer = new Thread(() -> {
            Random random = new Random();
            while (true) {
                try {
                    // 生产数据并存入队列
                    int num = random.nextInt(10000);
                    blockingQueue.put(num);
                    System.out.println("生产了: \t" + num + ", 队列大小: \t" + blockingQueue.getSize());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "producer");
        // 启动生产者
        producer.start();
    }
}
