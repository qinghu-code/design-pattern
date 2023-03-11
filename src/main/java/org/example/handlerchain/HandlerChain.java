package org.example.handlerchain;

import java.util.Arrays;
import java.util.List;

/**
 * 责任链模式 将处理过程，封装为处理器，然后编排这些处理器为一个整体
 *  1. handler中handler方法，接受到 input对象时，有4种策略
 *    （1）自己先处理，如果不符合条件，则不处理
 *    （2）自己先处理，如果不符合条件，则抛出异常
 *    （3）先条用next的handle 由下面的处理器先处理，返回结果由自己处理，如果不符合条件 则不处理
 *    （4）先条用next的handle 由下面的处理器先处理，返回结果由自己处理，如果不符合条件 则抛出异常
 *    可以分别组成不同类型的责任链，灵活使用
 *  2. 可以将 建造者模式结合责任链模式，灵活构造
 *    （1）建造者的泛型和handler保持一致，next()添加后续节点时，不需要声明类型
 *    （2）由于类泛型，handler中 builder() 方法，为静态类型，可以引入 class<T> 参数，来确定生成的
 *        构造器的泛型。如果绝对不直观，也可以不用 builder() 方法 直接 new Handler.Builder<>
 */
public class HandlerChain {
    public static void main(String[] args) {
        String input = "测试";
        ConcreteHandler concreteHandler = (ConcreteHandler) Handler.builder(String.class) // 同 new Handler.Builder<String>
                .next(new ConcreteHandler(1))
                .next(new ConcreteHandler(2))
                .build();
        concreteHandler.handle(input);

        // 上面那种可以，引入 String 代替了 泛型 T

//        ConcreteHandler2 concreteHandler2 = (ConcreteHandler2) Handler.builder(List.class)
//                .next((new ConcreteHandler2(1)))
//                .next(new ConcreteHandler2(2))
//                .build();
//        concreteHandler2.handle(Arrays.asList(input));

        // 这种 不可以，ConcreteHandler 继承的 是 Handler<List<String>>的 子类
        // 因为 泛型擦除 List<String> 的 class 和 List.class 一致 ，builder 中只能放入 List.class
        // 但是 next 入参 需要 List.class , 不需要 List<String> (元素的继承关系 和 容器继承关系无关)
        // 如果 使用强制类型转换 (Handler<List>) (new ConcreteHandler2(1)) 也会失败，因为
        // ConcreteHandler2 继承 Handler<List<String>> 而不是继承 Handler<List>
        // 这时候 可以考虑 在 next 入参阶段做 类型 转换
    }
}

class ConcreteHandler extends Handler<String>{

    public ConcreteHandler(int order) {
        super(order);
    }
}

class ConcreteHandler2 extends Handler<List<String>>{

    public ConcreteHandler2(int order) {
        super(order);
    }
}

class Handler<T>{
    private final int order;
    private Handler<T> next;

    public Handler(int order) {
        this.order = order;
    }

    public void setNext(Handler<?> handler){
        next = (Handler<T>) handler;
    }

    public static <F> ChainBuilder<F> builder(Class<F> fClass){
        return new ChainBuilder<>();
    }

    public T handle(T object){
        // object 符合处理器条件，进行处理
        if(object != null){
            System.out.println(this.getClass().getName() + " " + order + "处理完毕");
        }
        // 处理后，交给下一个处理器
        if(next != null){
            return next.handle(object);
        }
        // 如果没有next，则返回处理结果
        return object;
    }

    public static class ChainBuilder<T>{
        private Handler<T> head;

        private Handler<T> tail;

        public ChainBuilder<T> next(Handler<T> handler){
            if(head == null){
                head = handler;
            }else{
                tail.next = handler;
            }
            tail = handler;
            return this;
        }

        public Handler<T> build(){
            return head;
        }
    }
}




