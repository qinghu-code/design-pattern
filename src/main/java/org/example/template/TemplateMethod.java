package org.example.template;

/**
 * 模板方法模式
 *  具体实现由父类方法控制，调用子类方法辅助
 *  控制方法可以使用final关键字 防止被重写
 * 传统继承：
 *  具体实现由子类方法控制，调用父类方法辅助
 */
public class TemplateMethod {

    public static void main(String[] args) {
        AbstractTemplate template = new ConcreteTemplate();
        template.method();
    }

}

abstract class AbstractTemplate{
    public abstract void method1();
    public abstract void method2();
    public final void method(){
        method1();
        method2();
    }
}

class ConcreteTemplate extends AbstractTemplate{
    @Override
    public void method1() {
        System.out.println("method1");
    }

    @Override
    public void method2() {
        System.out.println("method2");
    }
}