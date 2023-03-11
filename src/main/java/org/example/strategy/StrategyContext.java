package org.example.strategy;

import java.util.List;
import java.util.Random;

/**
 * 策略模式：一般用于算法切换 比如 排序 可以定义为策略接口 快速排序、堆排序可以作为具体策略执行类
 *   1. 解耦了 策略上下文和策略的具体实现
 *   2. 用于避免多重条件判定，策略上下文初始化时，由各个模块选择自己的具体策略，不需要条件判定和参数检测
 *   3. 符合里氏替换原则
 */
public class StrategyContext {
    private SortStrategy strategy;
    List<Integer> list = new Random()
            .ints(1000000, 0, 1000000)
            .boxed().toList();

    public StrategyContext(SortStrategy strategy) {
        this.strategy = strategy;
    }
    public List<Integer> sort(){
        return strategy.sort(list);
    }

    public static void main(String[] args) {
        StrategyContext strategyContext = new StrategyContext(new SyncSortStrategy());
        long cur = System.currentTimeMillis();
        strategyContext.sort();
        System.out.println("耗时：" + (System.currentTimeMillis() - cur));
        StrategyContext strategyContext1 = new StrategyContext(new ParallelSortStrategy());
        cur = System.currentTimeMillis();
        strategyContext1.sort();
        System.out.println("耗时：" + (System.currentTimeMillis() - cur));
    }

}

interface SortStrategy{
    List<Integer> sort(List<Integer> list);
}
class SyncSortStrategy implements SortStrategy{
    @Override
    public List<Integer> sort(List<Integer> list) {
        return list.stream().sorted().toList();
    }
}

class ParallelSortStrategy implements SortStrategy{
    @Override
    public List<Integer> sort(List<Integer> list) {
        return list.parallelStream().sorted().toList();
    }
}