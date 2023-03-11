package org.example.adapter;

/**
 * 适配器模式 ：将 原有的功能，通过适配器的改造，为 新功能 提供实现
 *   1. 代码复用 利用已经有的接口， 为新接口提供实现
 *   2. 适配器类，需要实现新接口，通过 组合/继承 旧有接口实现类，便于二者共同工作
 */
public class AdapterTest {
    public static void main(String[] args) {
        String s = " 123456 ";
        Adapter adapter = new Adapter();
        System.out.println(adapter.getNumString(s));
    }
}

interface RequireInterface{
     Integer getNum(String s);
}

class Adapter extends SourceConcrete implements RequireInterface{
    @Override
    public Integer getNum(String s) {
        return Integer.parseInt(getNumString(s));
    }
}

interface SourceInterface{
     String getNumString(String s);
}

class SourceConcrete implements SourceInterface{

    @Override
    public String getNumString(String s) {
        return s.trim();
    }
}

