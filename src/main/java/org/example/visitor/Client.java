package org.example.visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * 访问者模式：表示一个作用于某 对象结构 中的各元素的操作。
 *           它使你可以在不改变各元素类的前提下定义作用于这些元素的新操作，比如拓展 ivistor 的新实现
 *
 * 1.Visitor 抽象访问者角色，为该对象结构中具体元素角色声明一个访问操作接口。
 *          该操作接口的名字和参数标识了发送访问请求给具体访问者的具体元素角色，
 *          这样访问者就可以通过该元素角色的特定接口直接访问它。
 *
 * 2.ConcreteVisitor.具体访问者角色，实现Visitor声明的接口。
 *
 * 3.Element 定义一个接受访问操作(accept())，它以一个访问者(Visitor)作为参数。
 *
 * 4.ConcreteElement 具体元素，实现了抽象元素(Element)所定义的接受操作接口。
 *
 * 5.ElementStructure 结构对象角色，这是使用访问者模式必备的角色。
 *                  它具备以下特性：能枚举它的元素；
 *                  可以提供一个高层接口以允许访问者访问它的元素；
 *                  如有需要，可以设计成一个复合对象或者一个聚集（如一个列表或无序集合）。
 * 特点：
 *  1. 访问者模式 本质是 将 不同的 visitor 主动调用 element，然后通过 if else ，
 *     根据不同element类型，做不同处理的过程
 *     ->
 *     通过 组织结构，被动 的接受 具体的 visitor，
 *     通过 element的 accept方法的重写，统一让 具体的element 接受 具体的visitor,
 *     然后 具体的element 将 this 自身类型，通过 visitor 内部的 visit方法的重载，
 *     触发访问者的处理过程
 *
 *     将 主动调用的过程，转换为 接受访问 的过程，利用重写 和 重载，将 操作 和 元素 解耦
 *
 *  2. 访问者模式把 数据结构 Structure 和 作用于结构上的操作解耦合，使得操作集合可相对自由地演化。
 *
 *  3. 增加操作很容易，因为增加操作意味着增加新的访问者。
 *     访问者模式将有关行为集中到一个访问者对象中，其改变不影响系统数据结构。
 *     其缺点就是增加新的数据结构很困难，每个访问者必须增加新的方法处理这种类型
 */
public class Client {
    public static void main(String[] args) {
        ElementStructure structure = new ElementStructure();
        structure.add(new Element1());
        structure.add(new Element2());

        IVisitor iVisitorNormal = new NormalVisitor();
        IVisitor iVisitorVip = new VipVisitor();
        structure.accept(iVisitorNormal);
        structure.accept(iVisitorVip);
    }
}

interface IVisitor{
    void visit(Element1 element1);
    void visit(Element2 element2);
}

/**
 * 中介者角色，对访问者而言，只能感知 组织结构，不需要接触具体元素
 *           对元素而言，组织结构 把 元素组合，不需要接触 具体访问者
 */
class ElementStructure{
    List<Element> elementList = new ArrayList<>();
    public void add(Element e){
        elementList.add(e);
    }
    public void remove(Element e){
        elementList.remove(e);
    }
    public void accept(IVisitor iVisitor){
        elementList.forEach(element -> element.accept(iVisitor));
    }
}

abstract class Element{
    private String name;
    private Integer price;

    public Element(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public abstract void accept(IVisitor iVisitor);

    public String getName() {
        return name;
    }
    public Integer getPrice() {
        return price;
    }
}

class Element1 extends Element{
    public Element1(){
        super("萝卜", 10);
    }

    @Override
    public void accept(IVisitor iVisitor) {
        iVisitor.visit(this);
    }
}

class Element2 extends Element{
    public Element2(){
        super("白菜", 20);
    }

    @Override
    public void accept(IVisitor iVisitor) {
        iVisitor.visit(this);
    }
}

class NormalVisitor implements IVisitor{

    @Override
    public void visit(Element1 element1) {
        String name = element1.getName();
        Integer price = element1.getPrice();
        System.out.println("normal "+ name + " " + price);
    }

    @Override
    public void visit(Element2 element2) {
        String name = element2.getName();
        Integer price = element2.getPrice();
        System.out.println("normal "+ name + " " + price);
    }
}

class VipVisitor implements IVisitor{
    public Double vipDiscount(){
        return 0.8;
    }

    @Override
    public void visit(Element1 element1) {
        String name = element1.getName();
        Double price = Double.valueOf(element1.getPrice());
        price *= vipDiscount();
        System.out.println("vip "+ name + " " + price);
    }

    @Override
    public void visit(Element2 element2) {
        String name = element2.getName();
        Double price = Double.valueOf(element2.getPrice());
        price *= vipDiscount();
        System.out.println("vip "+ name + " " + price);
    }
}
