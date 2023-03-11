package org.example.intermediary;

import java.util.ArrayList;
import java.util.List;

/**
 * 中介者模式 类似门面模式，但却是对内的，用于多个类之间互相依赖，呈网状模式
 * 可以通过中介者模式，让 成员类 只依赖 中介者，有中介者对成员类提供功能 呈星状
 *  1. 成员类 和 中介者 之间相互依赖，二者不能同时通过构造函数引入依赖，否者会产生循环依赖
 *  2. 根据容器化思想，中介者是一个通用协调器，可以将其构造过程 和 初始化依赖项 分离，
 *      成员类通过构造函数引入中介者，代码明了简洁
 */
public class Intermediary {

    private final List<Colleague> colleagueList = new ArrayList<>();

    public Intermediary() {
    }

    public void add(Colleague colleague) {
        this.colleagueList.add(colleague);
    }

    public void provideOther(Colleague colleague) {
        colleagueList.stream().filter(node -> !node.equals(colleague))
                .forEach(Colleague::provideComponent);
    }

    public static void main(String[] args) {
        Intermediary intermediary = new Intermediary();
        Colleague colleague1 = new Colleague1(intermediary);
        Colleague colleague2 = new Colleague2(intermediary);
        Colleague colleague3 = new Colleague3(intermediary);
        intermediary.add(colleague1);
        intermediary.add(colleague2);
        intermediary.add(colleague3);
        colleague1.produce();
    }
}

class Colleague {
    private Intermediary intermediary;

    public Colleague(Intermediary intermediary) {
        this.intermediary = intermediary;
    }

    public Intermediary getIntermediary() {
        return intermediary;
    }

    public void provideComponent() {
        System.out.println("组件");
    }

    public void produce(){
        intermediary.provideOther(this);
        provideComponent();
    }
}

class Colleague1 extends Colleague {
    public Colleague1(Intermediary intermediary) {
        super(intermediary);
    }

    public void provideComponent() {
        System.out.println("组件1");
    }

}

class Colleague2 extends Colleague {
    public Colleague2(Intermediary intermediary) {
        super(intermediary);
    }

    public void provideComponent() {
        System.out.println("组件2");
    }
}

class Colleague3 extends Colleague {
    public Colleague3(Intermediary intermediary) {
        super(intermediary);
    }

    public void provideComponent() {
        System.out.println("组件3");
    }
}