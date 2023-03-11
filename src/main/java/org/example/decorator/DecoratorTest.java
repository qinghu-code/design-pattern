package org.example.decorator;

/**
 * 装饰器模式 ： 类似静态代理模式。原始类 和 装饰类/代理类 实现或继承同一个接口、抽象类，
 *              且 装饰类/代理类 都是调用原始类方法。
 *              但是 静态代理一般只有 一个代理类 对应 一个原始类，用来拓展某一方面功能
 *                  装饰者模式 则使用 抽象、父类装饰器 对应 一个原始类，然后不同的具体装饰器，对应不同的拓展
 *              因此，装饰器模式，更类似于 继承关系，但是 继承是对类的拓展，装饰器是对对象的拓展
 *                   不同的拓展，对应不同的子类，对应不同的具体装饰器
 */
public class DecoratorTest {
    public static void main(String[] args) {
        Component component = new ConcreteComponent();
        component.operate();
        System.out.println(".....................");
        Component decorator = new ConcreteDecorator(component);
        decorator.operate();
    }
}

abstract class Component{
    abstract void operate();
}

class ConcreteComponent extends Component{

    @Override
    void operate() {
        System.out.println("做一些事情");
    }
}

abstract class Decorator extends Component{
    private Component component;

    public Decorator(Component component) {
        this.component = component;
    }
    @Override
    void operate() {
        System.out.println("抽象类装饰器");
        component.operate();
    }
}

class ConcreteDecorator extends Decorator{
    public ConcreteDecorator(Component component) {
        super(component);
    }

    public void decorate(){
        System.out.println("装饰");
    }

    @Override
    void operate() {
        decorate();
        super.operate();
    }
}