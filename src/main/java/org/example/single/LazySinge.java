package org.example.single;

/**
 * 懒汉式 ： 需要时，生成单例
 * volatile 必不可少
 * 防止指令重排 创建对象并不是单一的指令，而是多个指令
 *  创建对象：
 *  1. 分配内存
 *  2. 初始化对象
 *  3. 变量指向对象
 *    2 3 可能指令重排序，导致 thread1 完成 3 但 2 还没完成时，
 *  thread2 在第一个 null判断时，变量不为null，直接使用
 */
public class LazySinge {

    private static volatile LazySinge INSTANCE = null;

    private LazySinge() {
    }

    public static LazySinge getInstance(){
        if(INSTANCE == null){
            synchronized (LazySinge.class){
                if(INSTANCE == null){
                    INSTANCE = new LazySinge();
                }
            }
        }
        return INSTANCE;
    }
}
