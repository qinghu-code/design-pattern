package org.example.closure;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class FunctionClosureTest {
    public static void main(String[] args) {
        Function<Integer, Function<Integer, Integer>> function = (x) -> {
            AtomicReference<Integer> lx = new AtomicReference<>(x);
            return (Function) (y) -> {
                lx.updateAndGet(v -> v + (Integer) y);
                return lx.get();
            };
        };
        Integer apply = function.apply(10).apply(20);
        System.out.println(apply);
    }
}
