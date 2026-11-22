package thread;

// 先创建一个 Thread 的子类，继承 Thread 是创建线程的其中一种方式
class MyThread extends Thread {

    // 重写父类 Thread 的 run 方法
    // 重点：run 方法就是你这个新线程“真正要执行的任务清单”
    @Override
    public void run() {
        // 让新线程执行的逻辑就在这个 run 中进行编写了.
        // 这里写了个死循环，意思是：这个新线程一旦启动，就会一直跑，永远不休息（除非被打断）
        while (true) {
            // 打印这句话，代表是“新线程”在干活
            System.out.println("hello thread");
            
            // Thread.sleep(1000)：让当前这个新线程“睡眠（暂停）” 1000 毫秒，也就是 1 秒钟。
            // 为什么要 sleep？因为如果不睡，这个死循环会疯狂打印，1秒钟打印几万次，你的电脑风扇直接起飞，屏幕全是字，你都看不清。
            // 必须用 try-catch 包起来，因为万一线程在睡觉的时候被强制叫醒（比如别人中断了它），就会抛出一个叫 InterruptedException 的异常。
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // 如果睡觉被打断了，这里捕获异常。你这里写的是把它重新包装成一个 RuntimeException 抛出，直接让程序崩溃报错。
                throw new RuntimeException(e);
            }
        }
    }
}

public class Demo1 {
    // main 方法，这是整个程序的主入口，也就是“主线程”开始的地方。
    public static void main(String[] args) {
        // 创建实例, 只是准备工作.
        // 这行代码只是在内存里 new 了一个 MyThread 对象，但此时操作系统还没有给它分配独立的执行流。
        Thread t = new MyThread();

        // 真正创建出线程, 还需要通过另一个方法
        // 核心！！重点！！
        // 调用 start()，操作系统底层才会真正开启一条新的线程（马路），然后让这条新线程自动去执行上面那个 run() 方法里的代码。
        t.start();
        
        // t.run(); 
        // 注意这里被注释掉的代码。如果你直接调用 run()，那它就是个普通的方法调用。
        // 相当于你直接在 main 线程里跑过去把 run 里的代码执行了一遍，根本没有开启新线程！
        
        // 下面这个是“主线程”的死循环
        while (true) {
            // 打印这句话，代表是“主线程”在干活
            System.out.println("hello world");
            
            // 主线程也睡 1 秒钟。如果不睡，主线程会霸占 CPU，新线程可能根本抢不到 CPU 执行。
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // 主线程睡觉如果被打断，打印错误信息。
                e.printStackTrace();
            }
        }
    }
}