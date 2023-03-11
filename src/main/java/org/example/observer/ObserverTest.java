package org.example.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 观察者模式： 也叫 发布/订阅模式 publisher/subscriber
 *    定义对象间一种 一对多的依赖关系，使得每当一个对象改变状态，则所有依赖于它的对象都会得到通知并被自动更新
 *    1. 观察者 需要一个统一的 observer 接口，作为多态 被 具体的被观察者统一接收
 *       被观察者 需要一个统一的 observable 抽象类，用来封装 基本操作
 *       使用过程中，只需要自定义 观察者/被观察者 实现或继承 observer/observable ,
 *       则 具体实现类，不需要关注两者之间的关系管理，只关注具体业务流程即可
 *    2. java api 自带 java.util.Observable实现类 和 java.util.Observer接口，可以使用，通用性强，无需自定义
 *    3. 传递的 消息，可以封装为具体的 事件 EventObject，承载更多信息，或定义某一类事件（事件模型）
 */
public class ObserverTest {
    public static void main(String[] args) {
        ConcreteObservable observable = new ConcreteObservable("测试1");
        observable.addObserver(new ConcreteObserver());
        observable.resetName("测试2");
    }
}

interface CustomObserver{
    void update(String event);
}

class ConcreteObserver implements CustomObserver{

    @Override
    public void update(String event) {
        System.out.println("收到消息：" + event);
    }
}

abstract class AbstractObservable{
    private List<CustomObserver> observerList = new CopyOnWriteArrayList<>();

    public void addObserver(CustomObserver customObserver){
        this.observerList.add(customObserver);
    }

    public void removeObserver(CustomObserver customObserver){
        this.observerList.remove(customObserver);
    }

    public void notifyObservables(String event){
        this.observerList.forEach(observer -> observer.update(event));
    }

    public void asyncNotifyObservables(String event){
        this.observerList.parallelStream().forEach(observer -> observer.update(event));
    }

}

class ConcreteObservable extends AbstractObservable{

    private String name;

    public ConcreteObservable(String name){
        this.name = name;
    }

    public void resetName(String name){
        super.notifyObservables("被观察者更改了name 原来的name：" + this.name);
        this.name = name;
        super.asyncNotifyObservables("被观察者更改了name 最新的name：" + name);
    }

}