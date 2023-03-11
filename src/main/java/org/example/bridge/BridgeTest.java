package org.example.bridge;

/**
 * 桥接器模式：类似适配器模式，都是让 组合不同类，来提供功能
 *     但 适配器模式，一般用于，已经有一个老的接口的实现，想用来为新接口，提供功能，可以使用适配器
 *     继承或组合 原有接口，实现新接口，1：1
 * 桥接器模式，是 继承关系 的 替代，解耦 抽象和实现，比如说，一个类需要 另一个类的功能，可以通过继承，
 *          但 如果父类改变，子类会跟着改变，如果子类不想跟着改变，可以通过 桥接器 隔离变化，提供稳定接口
 *          关系：m：n
 *  举例来说：windows 的 jvm 和 linux 的 jvm 都是 桥接器，windows和linux的系统 api 用法不同，
 *          通过 不同版本的 jvm ，为 java 开发者提供的确实相同的 java api
 *
 *          其中 面向 用户的 java api 就是 abstractionBridge，规定了 不变的 api 方法
 *          windows 版本的 jvm 和 linux版本的 就是 修正版的 桥接器 refineBridge
 *          原有功能 implementor 就是 系统 api
 *
 */
public class BridgeTest {

    public static void main(String[] args) {
        Implementor implementor1 = new ConcreteImplementor1();
        Implementor implementor2 = new ConcreteImplementor2();
        AbstractionBridge refineAbstraction1 = new RefineBridge1(implementor1);
        AbstractionBridge refineAbstraction2 = new RefineBridge2(implementor2);
        Client client = new Client(refineAbstraction1);
        client.doString();
        Client client1 = new Client(refineAbstraction2);
        client1.doString();
    }
}

interface Implementor{
    void implement();
}

class ConcreteImplementor1 implements Implementor{

    @Override
    public void implement() {
        System.out.println("系统1 提供110v电压");
    }
}

class ConcreteImplementor2 implements Implementor{

    @Override
    public void implement() {
        System.out.println("系统2 提供220v电压");
    }
}

abstract class AbstractionBridge{
    private Implementor implementor;
    public AbstractionBridge(Implementor implementor){
        this.implementor = implementor;
    }
    public Implementor getImplementor(){
        return this.implementor;
    }
    public void doString(){
        implementor.implement();
    }
}

class RefineBridge1 extends AbstractionBridge{

    public RefineBridge1(Implementor implementor) {
        super(implementor);
    }
    public void doString(){
        super.getImplementor().implement();
        System.out.println("桥接器1 不削减，输出");
    }
}

class RefineBridge2 extends AbstractionBridge{

    public RefineBridge2(Implementor implementor) {
        super(implementor);
    }
    public void doString(){
        super.getImplementor().implement();
        System.out.println("桥接器2 削减一半，输出");
    }
}

class Client{
    private AbstractionBridge bridge;
    public Client(AbstractionBridge bridge){
        this.bridge = bridge;
    }

    public void doString(){
        System.out.println("需要110v电力，从桥接器中拿取");
        bridge.doString();
    }
}

