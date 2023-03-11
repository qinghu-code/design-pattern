package org.example.strategy;

import java.util.List;
import java.util.Random;

/**
 * 枚举 + 策略 ： 将 枚举类 作为 策略接口，定义接口的抽象方法
 *              枚举类中的实例 实现 抽象方法，定义不同的策略执行方法
 */
public class EnumStrategyContext {
    public static void main(String[] args) {
        List<Integer> list = new Random()
                .ints(1000000, 0, 1000000)
                .boxed().toList();
        long cur = System.currentTimeMillis();
        SortStrategyContextEnum.SYNC.sort(list);
        System.out.println("耗时：" + (System.currentTimeMillis() - cur));
        cur = System.currentTimeMillis();
        SortStrategyContextEnum.PARALLEL.sort(list);
        System.out.println("耗时：" + (System.currentTimeMillis() - cur));
    }
}

enum SortStrategyContextEnum{
    SYNC{
        @Override
        public List<Integer> sort(List<Integer> list) {
            return list.stream().sorted().toList();
        }
    },
    PARALLEL{
        @Override
        public List<Integer> sort(List<Integer> list) {
            return list.parallelStream().sorted().toList();
        }
    };

    public abstract List<Integer> sort(List<Integer> list);

}
