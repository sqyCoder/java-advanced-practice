package thread;

// 懒汉的单例模式
class SingletonLazy {
    private volatile static SingletonLazy instance = null;

    // 作为要加锁的对象. 由于是要在 static 方法中使用锁对象, 对象本身也得是 static 的.
    private static Object locker = new Object();

    // 懒汉模式的关键在于, 把实例的创建时机推迟了, 推迟到第一次使用的时候, 创建.
    public static SingletonLazy getInstance() {
        // 这个条件判定是否需要加锁
        if (instance == null) {
            synchronized (locker) {
                // 判定是否需要创建实例
                if (instance == null) {
                    instance = new SingletonLazy();
                }
            }
        }
        return instance;
    }

    private SingletonLazy() {
    }
}
public class Demo13 {
    public static void main(String[] args) {
        SingletonLazy s1 = SingletonLazy.getInstance();
        SingletonLazy s2 = SingletonLazy.getInstance();
        System.out.println(s1 == s2);
    }
}