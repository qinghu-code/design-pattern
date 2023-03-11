package org.example.handlerchain;

import java.util.Arrays;
import java.util.List;

/**\
 *
 *  推荐使用 ：细节全部封装、不需要多余实例  缺点：构造细节多、类型转换
 *
 *  ConcreteHandler4 继承的 是 Handler<List<String>>的 子类
*       因为 泛型擦除 List<String> 的 class 和 List.class 一致 ，builder 中只能放入 List.class
*       但是 next 入参 需要 List.class , 不需要 List<String> (元素的继承关系 和 容器继承关系无关)
*       如果 使用强制类型转换 (Handler<List>) (new ConcreteHandler2(1)) 也会失败，因为
*       ConcreteHandler2 继承 Handler<List<String>> 而不是继承 Handler<List>
 *
*       这时候 可以考虑 在 next 入参阶段做 类型 转换
 *
 *      使用 <? extends T> 通配符 继承 泛型 来表示，？类型是 泛型 T 的派生类。
 *      只能使用 通配符标识，如果再次引入 其他泛型 来标识，创建类时，需要指定具体的派生类型，再次限制了自由度
 */
public class HandlerChain2 {
    public static void main(String[] args) {
        String input = "测试";
        ConcreteHandler4 concreteHandler4 = (ConcreteHandler4) Handler2.builder(List.class) // 同 new Handler.Builder<String>
                .next(new ConcreteHandler4(1))
                .next(new ConcreteHandler4(2))
                .build();
        concreteHandler4.handle(Arrays.asList(input));

        ConcreteHandler3 concreteHandler3 = (ConcreteHandler3) Handler2.builder(String.class) // 同 new Handler.Builder<String>
                .next(new ConcreteHandler3(1))
                .next(new ConcreteHandler3(2))
                .build();
        concreteHandler3.handle(input);
    }
}

class ConcreteHandler3 extends Handler2<String>{

    public ConcreteHandler3(int order) {
        super(order);
    }
}

class ConcreteHandler4 extends Handler2<List<String>>{

    public ConcreteHandler4(int order) {
        super(order);
    }
}

class Handler2<T>{
    private final int order;
    private Handler2<T> next;

    public Handler2(int order) {
        this.order = order;
    }

    /**
     * 注意这里 必须用 ？ 通配符接收 , 静态内部类 的 泛型 和 handler类的泛型，没有任务关系，所以用 ？ 标识接受所有类型
     * @param handler
     */
    public void setNext(Handler2<?> handler){
        next = (Handler2<T>) handler;
    }

    public static <F> ChainBuilder2<F> builder(Class<F> fClass){
        return new ChainBuilder2<>();
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

    public static class ChainBuilder2<T>{
        private Handler2<? extends T> head;

        private Handler2<? extends T> tail;

        public ChainBuilder2<T> next(Handler2<? extends T> handler){
            if(head == null){
                head = handler;
            }else{
                // tail next 的 类型 为 ？extends T 是不确定类型
                // handler 参数 的 ？ extends T 也是不确定类型，但二者不相等
                // 必须强转为 builder中的 <? extends T>，但是不确定类型不能转换
                // handler 中的 T 和 builder中的 < ? extends T> 是等价且确定类型，把handler通过set中传入
                // 在 handler中强转
                tail.setNext(handler);
            }
            tail = handler;
            return this;
        }

        public Handler2<? extends T> build(){
            return head;
        }
    }
}




