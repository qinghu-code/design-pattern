package org.example.handlerchain;


import java.util.Arrays;
import java.util.List;

/**
 * 不使用 静态内部类 作为 chainBuilder构造器， 使用 public的 内部类 代替 静态内部类
 *    静态内部类的 泛型 T 和 Handler 之间 没有任务关系
 *    普通内部类，可以直接使用 Handler中的泛型，形成绑定关系，自己不需要定义泛型
 *    这样一来， handler 被继承后 ConcreteHandler extends Handler<List<Stirng>，
 *          泛型 List<String> 是已知的，编译期间，直接替换，而不是擦除
 *
 *    非静态内部类，builder方法 也是非静态的，只能通过 handler 实例 调用，
 *    但为了使用方便，可以 使用 static defaultHandler，生成默认 实例，
 *    最好不要在 Handler中 实现，否则还是要指定 泛型 T，面临泛型擦除
 *
 *    由 明确了 泛型T 的 concreteHandler，实现自己的 默认实例
 *
 *  优点：简单、通用、无需强转
 *  缺点：每个 明确了 T 泛型的 类，需要实现 static defaultHandler方法，生成实例
 */
public class HandlerChain3 {
    public static void main(String[] args) {
        String input = "测试";
        ConcreteHandler5 build = ConcreteHandler5.defaultHandler()
                .builder() // 同 new Handler.Builder<String>
                .next(new ConcreteHandler5(1))
                .next(new ConcreteHandler5(2))
                .build();
        build.handle(input);

        ConcreteHandler6 concreteHandler6 = ConcreteHandler6.defaultHandler()
                .builder() // 同 new Handler.Builder<String>
                .next(new ConcreteHandler6(1))
                .next(new ConcreteHandler6(2))
                .build();
        concreteHandler6.handle(Arrays.asList(input));

        // 下面的依旧不行，因为泛型擦除，只传了 List.class 而需要的是 List<String>.class ,只能从具体类中得到
//        ConcreteHandler6 concreteHandler6 = Handler3.defaultHandler(List.class)
//                .builder() // 同 new Handler.Builder<String>
//                .next(new ConcreteHandler6(1))
//                .next(new ConcreteHandler6(2))
//                .build();
//        concreteHandler6.handle(Arrays.asList(input));
    }
}

class ConcreteHandler5 extends Handler3<String>{

    public ConcreteHandler5(int order) {
        super(order);
    }

    public static ConcreteHandler5 defaultHandler(){
        return new ConcreteHandler5(0);
    }
}

class ConcreteHandler6 extends Handler3<List<String>>{

    public ConcreteHandler6(int order) {
        super(order);
    }

    public static ConcreteHandler6 defaultHandler(){
        return new ConcreteHandler6(0);
    }
}

class Handler3<T>{
    private final int order;
    private Handler3<T> next;

    public Handler3(int order) {
        this.order = order;
    }

    public ChainBuilder3 builder(){
        return new ChainBuilder3();
    }

    public static <T> Handler3<T> defaultHandler(Class<T> tClass){
        return new Handler3<T>(0);
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

    public class ChainBuilder3{
        private Handler3<T> head;

        private Handler3<T> tail;

        public ChainBuilder3 next(Handler3<T> handler){
            if(head == null){
                head = handler;
            }else {
                tail.next = handler;
            }
            tail = handler;
            return this;
        }

        public <F extends Handler3<T>> F build(){
            return (F) head;
        }
    }
}

