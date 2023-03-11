package org.example.memorandum;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 备忘录模式： 在不破坏封装的前提下，捕获一个对象的内部状态，也称快照模式
 *            并在该对象之外保存这个状态，这样可以在以后将对象恢复到原先保存的状态: 事务管理器
 *            (类似监听机制，不破坏原有结构，原有对象不添加额外引用)
 *
 *   用于需要保存和恢复数据的相关状态场景，提供回滚操作，可参考 日志、git、mvcc等
 *
 *    1. createTime 指 备忘录创建的时间，原始对象备份的时刻，只有一份： 日志时间
 *       recordTime 值 备忘录 放到 备忘录管理器 的时间
 *       因为 一份记录，可能放到不同的备忘录管理器，所以由 不同的备忘录管理器 自己管理 ： git、mvcc
 *    2. version 版本号，用来 rollback回滚 ，由 不同的备忘录自己管理
 *    3. 可以将存入指定管理类 封装为 origin.store方法，参数为 指定管理类，作为简化
 *
 *    上面这种方法，类似日志。通过备忘条目，解耦 备忘录 和 原始对象，但使用上 需要同时 调用不同函数，
 *    但拓展性高、可以存入不同备忘录、不需要验证、序列化时不会误操作
 *
 *    如果将 备忘录作为管理器的内部类，原始对象引入管理类，提前绑定，使用起来会很方便，但是破坏了原始类结构、且序列化时，需要注意
 *    如果引入工厂类，则类型冗余，用起来改动更大。
 */
public class MemorandumCaretaker {

    private final Map<Integer, Memorandum> memorandumItemMap = new HashMap<>();
    private final Map<Integer, Date> recordTimeMap = new HashMap<>();
    private final AtomicInteger maxVersion = new AtomicInteger(1);

    public void storeItem(Memorandum memorandumItem){
        memorandumItemMap.put(maxVersion.get(), memorandumItem);
        recordTimeMap.put(maxVersion.get(), new Date());
        maxVersion.incrementAndGet();
    }

    public Memorandum getItem(Integer version){
        return memorandumItemMap.get(version);
    }

    public void browse(){
        memorandumItemMap.forEach((key, value) -> System.out.println(key + " " + recordTimeMap.get(key) + " " + value));
    }

    public static void main(String[] args) {
        MemorandumCaretaker caretaker = new MemorandumCaretaker();
        Origin origin = new Origin("1", "1");
        origin.store(caretaker);
        origin.setValue1("2");
        // 1. 生成快照
        Memorandum store = origin.store();
        // 2. 管理者主动去存储快照
        // 如果有不同类型的 管理者，可以加入判断条件，由管理者根据其编好 主动挑选自身可用快照
        caretaker.storeItem(store);
        origin.setValue2("2");
        // 简化操作，将上面两步合并到store方法中。不够灵活，但如果仅仅使用一个管理者，可以选择
        origin.store(caretaker);
        caretaker.browse();
        origin.restore(caretaker.getItem(1));
        System.out.println(origin.toString());
    }

}


class Memorandum{
    private String value1;
    private String value2;
    /**
     * 下面为隐藏字段，只能由 Memorandum 管理者操作
     */
    private Date createTime = new Date();

    public Memorandum(String value1, String value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public String getValue1() {
        return value1;
    }

    public String getValue2() {
        return value2;
    }

    @Override
    public String toString() {
        return "MemorandumItem{" +
                "value1='" + value1 + '\'' +
                ", value2='" + value2 + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}

class Origin{

    private String value1;
    private String value2;

    public Origin(String value1, String value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public Memorandum store(){
        return new Memorandum(value1, value2);
    }

    public void store(MemorandumCaretaker memorandumCaretaker){
        memorandumCaretaker.storeItem(store());
    }

    public void restore(Memorandum memorandumItem){
        this.setValue1(memorandumItem.getValue1());
        this.setValue2(memorandumItem.getValue2());
    }

    @Override
    public String toString() {
        return "Origin{" +
                "value1='" + value1 + '\'' +
                ", value2='" + value2 + '\'' +
                '}';
    }
}

