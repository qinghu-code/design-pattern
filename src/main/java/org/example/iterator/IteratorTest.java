package org.example.iterator;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

/**
 * 迭代器模式： 提供一个对象来顺序访问聚合对象中的一系列数据，而不暴露聚合对象的内部表示。迭代器模式是一种对象行为型模式。
 *   1. 迭代器模式 是 容器的 指针 或者 游标， 迭代器的操作，只能影响当前指向的 元素
 *   2. 迭代器一般用于 容器内部，和具体容器绑定，作为私有的内部类，一般不需要自己实现，细节较多，容易出错
 *
 *   下面这个简易 迭代器 是 参考 ArrayList 的 iterator 实现
 */
public class IteratorTest {
    public static void main(String[] args) {
        Container<Integer> container = new ConcreteContainer<>();
        container.add(1);
        container.add(2);
        Iterator<Integer> iterator = container.iterator();
        container.toString();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
            System.out.println(iterator.remove());
            System.out.println(iterator.remove());
        }
        container.toString();

        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        java.util.Iterator<Integer> iterator1 = list.iterator();
        while (iterator1.hasNext()){
            iterator1.remove();
        }
    }

    /**
     * 直接通过遍历 list 删除
     *  因为 list的 size 会随着 remove 减少，所以 下面这个 list，不会被删除干净
     *  第一次 删除 1，size 变 2 ，i++ 为 1
     *  第二次 删除 3，size 变 1， i++ 为 2
     *  第三次 i > size 结束 ，list 剩余 [2]
     */
    public static void test() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        for(int i = 0; i < list.size(); i++){
            //System.out.println(i);
            System.out.println("前 "+list.size());
            list.remove(i);
            System.out.println(i);
            System.out.println("后 "+list.size());
        }
        list.iterator().remove();
        System.out.println(list);
    }
}

/**
 * 迭代器接口 一般为 public，使用时，直接 用接口接收，防止出现 Extern.Iterator 这种内部类写法
 * @param <T>
 */
interface Iterator<T>{
    T next();
    boolean hasNext();
    boolean remove();
}

interface Container<T>{
    void add(T t);
    void remove(T t);
    Iterator<T> iterator();
}

/**
 * 抽象容器类，便于多态使用 类似 Collection接口
 * @param <T>
 */
class ConcreteContainer<T> implements Container<T>{

    private List<T> list = new ArrayList<>();
    /**
     * fast-fail 快速失败机制
     * modCount 计算 容器的修改次数
     * expectModCount 计算 容器和迭代器 修改次数
     * 一致 则说明没有外部线程 操作容器， 不一致则可能有异常
     */
    private int modCount = 0;

    @Override
    public void add(T t) {
        modCount++;
        list.add(t);
    }

    @Override
    public void remove(T t) {
        modCount++;
        list.remove(t);
    }

    @Override
    public Iterator<T> iterator() {
        return new UsefulIterator();
    }

    public String toString(){
        System.out.println(list.toString());
        return null;
    }

    /**
     * 具体迭代器 专门用于 具体的容器，所以一般 作为 具体容器的 私有内部类 ，也便于访问 modCount
     */
    private class UsefulIterator implements Iterator<T>{
        /**
         * 游标
         */
        private int index = 0;
        /**
         *  add 时，将 index 赋值给 lastIndex，标识当前 迭代到哪里
         *  remove时，因为 迭代器在当前位置，只能 remove 一次 ，赋值为 -1，标识不能继续 remove
         *
         *  也可以 只使用 index，add index++ remove index-- hasNext 做上下界
         *        可以一直调用 remove，将前面已经遍历的元素移除
         *  但是 迭代器 是 容器的指针 指向一个位置后，remove 只应该移除当前位置，不应该改动其他位置
         */
        private int lastIndex = -1;
        /**
         * fast-fail 快速失败机制
         * modCount 计算 容器的修改次数
         * expectModCount 计算 容器和迭代器 修改次数，迭代器创建时，由 modCount 初始化
         * 一致 则说明没有外部线程 操作容器， 不一致则可能有异常
         */
        private int expectModCount = modCount;
        public UsefulIterator(){
            java.util.Iterator<T> iterator = list.iterator();
            while (iterator.hasNext()){
                T next = iterator.next();
                // 过滤
                if(!isUseful(next)){
                    iterator.remove();
                }
            }
        }
        @Override
        public T next() {
            if(modCount != expectModCount){
                throw new ConcurrentModificationException();
            }
            if(hasNext()){
                expectModCount++;
                modCount = expectModCount;

                lastIndex = index;
                return list.get(index++);
            }
            return null;
        }

        @Override
        public boolean hasNext() {
            return index < list.size();
        }

        /**
         * 只能移除一次
         * @return
         */
        @Override
        public boolean remove() {
            if(lastIndex >= 0){
                expectModCount++;
                modCount = expectModCount;

                list.remove(lastIndex);

                lastIndex = -1;
                index--;
                return true;
            }
            return false;
        }
        public boolean isUseful(T t){
            return true;
        }
    }
}