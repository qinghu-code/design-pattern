package org.example.prototype;

/**
 * 原型模式 java中即实现cloneable接口，通过object的clone 实现二进制流的拷贝，来创建对象
 * 1. object.clone 直接通过二进制流拷贝，性能比 new 更好，循环创建对象时，可以考虑
 * 2. clone接口创建的对象，是直接复制二进制流，所以没有经过构造函数，也不受构造函数约束
 * 3. clone接口，默认浅拷贝，即 域属性为 引用数据类型时，所指定的元素为同一个
 *    如果实现深拷贝，clone自己后，还需要让 域属性 调用clone方法，并将返回值 重新赋值给 克隆对象的 域属性
 * 4. 对于没有clone方法的语言，可以使用 工具类、工厂类、反射等方法自行实现 clone方法
 */
public class Prototype {
    public static void main(String[] args) {
        Origin origin = new Origin("1");
        System.out.println(origin.getValue());
        Origin clone = origin.clone();
        System.out.println(clone.getValue());
    }
}

class Origin implements Cloneable{
    private String value;

    public Origin(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Origin clone(){
        Origin origin = null;
        try {
            origin = (Origin)super.clone();
        } catch (CloneNotSupportedException e) {
        }
        return origin;
    }
}
