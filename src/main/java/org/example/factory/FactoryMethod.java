//package org.example.factory;
//
//import java.lang.reflect.Constructor;
//import java.lang.reflect.InvocationTargetException;
//
/**
 * 工厂方法模式 是 new 的替代，封装对象创建的复杂度
 *  在工厂方法模式里，我们关注的是一个产品整体，单一性质，粒度粗
 *  在建造者模式中，一个具体产品的产生是依赖各个部件的产生以及装配顺序，它关注的是“由零件一步一步地组装出产品对象”，复杂性质，粒度细
 * 工厂方法模式 一个工厂接口，对应一个工厂 对应一个或多个方法 根据传入的参数不同，生成不同的商品、
 *   可以通过 增加具体工厂 或者 修改添加 工厂方法 来拓展功能
 *   1. 将工厂接口去掉，保留单一的具体工厂，product方法 加上 static，便是简单工厂模式
 *   2. 工厂方法，不传参数。通过多个具体工厂类，让一个具体工厂只生成有一种具体的产品，便是多工厂模式
 */
//public class FactoryMethod {
//    public static void main(String[] args) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
//        AbstractFactory factory = new ConcreteFactory();
//        Product product1 = factory.product(ProductA.class);
//        Product product2 = factory.product(ProductB.class);
//        product1.method();
//        product2.method();
//    }
//}
//
//abstract class Product{
//    public abstract void method();
//}
//class ProductA extends Product{
//    public void method(){
//        System.out.println(this.getClass().getName());
//    }
//}
//
//class ProductB extends Product{
//    public void method(){
//        System.out.println(this.getClass().getName());
//    }
//}
//
// class ConcreteFactory extends AbstractFactory{
//    public <T> T product(Class<T> tClass) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
//        Constructor<T> constructor = tClass.getDeclaredConstructor();
//        System.out.println(constructor.getName());
//        return constructor.newInstance();
//    }
//}
//abstract class AbstractFactory{
//    public abstract  <T> T product(Class<T> tClass) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException;
//}
//
