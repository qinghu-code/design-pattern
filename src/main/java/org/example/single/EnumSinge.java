package org.example.single;

/**
 * 枚举实现单例模式 饿汉式
 *  将想要实现单例的对象 , 设置构造器为private
 *  getInstance 方法，由 内部枚举类，封装生成对象实例
 */
public class EnumSinge {
    private EnumSinge() {
    }
    public static EnumSinge getInstance(){
        return Singe.INSTANCE.getInstance();
    }

    enum Singe{
        INSTANCE;
        private EnumSinge enumSinge;
        Singe() {
            this.enumSinge = new EnumSinge();
        }
        public EnumSinge getInstance(){
            return this.enumSinge;
        }
    }
}


