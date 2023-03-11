package org.example.combination;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合模式：维护和整理 部分与整体的关系。 树形结构，一般采用组合模式实现
 *   1. component：类似 node，表示 叶子节点 和 非叶子节点的抽象
 *   2. Leaf：叶子节点
 *   3. composite：非叶子节点，可以承装其他 component
 */
public class ComponentTest {

    public static void main(String[] args) {
        Composite root = new Composite("根目录");
        Composite dir1 = new Composite("一级目录");
        Leaf leaf1 = new Leaf("一级文件");
        root.addComponent(leaf1);
        root.addComponent(dir1);
        Leaf leaf2 = new Leaf("二级文件");
        dir1.addComponent(leaf2);
        ComponentTest.display(root);
    }

    public static void display(Component root){
        System.out.println(root.getName());
        if(root instanceof Composite){
            ((Composite) root).getChildren().forEach(ComponentTest::display);
        }
    }

}

abstract class Component {
    private String name;

    /**
     * 抽象函数的构造函数，不能再new、其他方法 中使用
     * 但可以被其他构造函数、子类构造函数中使用，用来初始化参数
     * @param name
     */
    public Component(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class Leaf extends Component {
    public Leaf(String name) {
        super(name);
    }
}

class Composite extends Component {
    List<Component> componentList = new ArrayList<>();
    public Composite(String name) {
        super(name);
    }

    public void addComponent(Component component) {
        this.componentList.add(component);
    }

    public void removeComponent(Component component){
        this.componentList.remove(component);
    }

    public List<Component> getChildren(){
        return this.componentList;
    }

}