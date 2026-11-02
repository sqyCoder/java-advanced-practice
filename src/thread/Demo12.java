package thread;

// 此处要求 Singleton 类只能有一个实例.
// 饿汉模式. 会在类加载的时候创建实例.
class Singleton {
    // 加了 static, 当前的成员成为 类属性, 在类对象上的. 类对象只有一个实例.
    // 此处 Instance 就可以保证在当前 java 进程中只有一份.
    private final static Singleton instance = new Singleton();

    public static Singleton getInstance() {
        return instance;
    }

    // 单例模式最关键的要点, 禁止构造方法被外部使用.
    private Singleton() {

    }
}

public class Demo12{
    public static void main(String[] args) {
        // 此时其他代码想 new 这个实例, 直接编译报错.
        // Singleton s = new Singleton();
        Singleton s1 = Singleton.getInstance();
        Singleton s2 = Singleton.getInstance();
        System.out.println(s1 == s2);
    }
}