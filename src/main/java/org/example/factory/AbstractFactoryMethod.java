package org.example.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 抽象工厂模式 工厂方法模式的升级，工厂接口的方法和其具体工厂同时拓展
 *   1. 不同的方法，生成不同的产品种类 （手机 电脑）
 *   2. 实现相同接口的 不同工厂生产 相同种类的不同品级 （华为 小米）
 */
public class AbstractFactoryMethod {
    public static void main(String[] args) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        AbstractFactory factoryA = new ConcreteFactoryA();
        AbstractFactory factoryB = new ConcreteFactoryB();
        Product1 productA1 = factoryA.product1();
        Product2 productA2 = factoryA.product2();
        Product1 productB1 = factoryB.product1();
        Product2 productB2 = factoryB.product2();
        productA1.method();
        productB1.method();
        productA2.method();
        productB2.method();
    }
}

abstract class Product1{
    public abstract void method();
}
abstract class Product2{
    public abstract void method();
}
class ProductA1 extends Product1{
    public void method(){
        System.out.println(this.getClass().getName());
    }
}

class ProductB1 extends Product1{
    public void method(){
        System.out.println(this.getClass().getName());
    }
}

class ProductA2 extends Product2{
    public void method(){
        System.out.println(this.getClass().getName());
    }
}

class ProductB2 extends Product2{
    public void method(){
        System.out.println(this.getClass().getName());
    }
}

class ConcreteFactoryA extends AbstractFactory{
    public Product1 product1() {
        return new ProductA1();
    }
    @Override
    public Product2 product2() {
        return new ProductA2();
    }
}

class ConcreteFactoryB extends AbstractFactory{
    public Product1 product1() {
       return new ProductB1();
    }
    @Override
    public Product2 product2() {
        return  new ProductB2();
    }
}
abstract class AbstractFactory{
    public abstract  <T extends Product1> T product1();

    public abstract  <T extends Product2> T product2();

}


