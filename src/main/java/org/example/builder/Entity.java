package org.example.builder;

/**
 * 构造者模式 封装对象构建过程
 *  *  在工厂方法模式里，我们关注的是一个产品整体，单一性质，粒度粗
 *  *  在建造者模式中，一个具体产品的产生是依赖各个部件的产生以及装配顺序，它关注的是“由零件一步一步地组装出产品对象”，复杂性质，粒度细
 *
 *  1. 一般采用静态内部类实现
 *  2. 静态内部类的属性 和 外部类保证一致，便于build时，调用外部类全属性构造方法
 *  3. 静态内部类，可以直接访问外部类静态变量，如果命名冲突，默认取自己的静态变量，可以指定类名
 *  4. 构造者属性赋值时，返回构造者本身对像（说明builder需要new，属性和方法不需要为static）
 *     为防止new关键字暴漏，可以在外部类添加 static builder() 方法，主动获取自己的构造者
 */
public class Entity {
    private String key;
    private String value;
    private static String staticValue = "entity";
    public Entity(String key, String value) {
        this.key = key;
        this.value = value;
    }
    public String getKey() {
        return key;
    }
    public String getValue() {
        return value;
    }
    public static Builder builder(){
        return new Builder();
    }

    @Override
    public String toString() {
        return "Entity{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public static class Builder{
        private String key;
        private String value;
        private static String staticValue = "builder";
        public Builder key(String key){
            this.key = key;
            return this;
        }
        public Builder value(String value){
            this.value = value;
            return this;
        }
        public Entity build(){
            System.out.println(Entity.staticValue);
            return new Entity(this.key, this.value);
        }
    }

    public static void main(String[] args) {
        Entity build = Entity.builder().key("ceshi key").value("ceshi value").build();
        System.out.println(build.toString());
    }
}
