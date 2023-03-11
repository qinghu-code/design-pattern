package org.example.single;

/**
 * 饿汉式 提前生成单例模式
 */
public class HangerSinge {

    private static final HangerSinge INSTANCE = new HangerSinge();

    private HangerSinge() {
    }

    public static HangerSinge getInstance(){
        return INSTANCE;
    }
}
