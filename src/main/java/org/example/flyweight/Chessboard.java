package org.example.flyweight;

import java.util.*;

/**
 * 享元模式：池化技术的实现方式 比如：缓冲池、线程池、对象池、连接池等。
 * 使用场景：
 *   1. 系统中存在大量的相似对象。
 *   2. 细粒度的对象都具备较接近的外部状态，而且内部状态与环境无关，也就是说对象没有特定身份。
 *   3. 需要缓冲池的场景。
 * 好处：
 *   1. 节省大量的内存使用
 *   2. 提升系统性能
 * 说明：享元模式
 * 两个重要概念：
 *   1. 内部状态：享元对象的属性，不因外部环境而改变。
 *   2. 外部状态：和context环境相关的属性
 *   比如 棋子 有 名称、颜色、坐标 ，
 *      其中 名称、颜色固定那么几种，和棋盘状态无关，就是内部状态
 *      坐标，会随着 棋子 在 棋盘 移动而改变，为外部状态
 * 有三个重要对象：
 *   1. flyweight： 抽象享元 所有享元的共同属性和方法，可以定义 调用外部状态的接口
 *   2. concrete flyweight： 具体享元 定义角色的业务属性和方法
 *   3. flyweightFactory：享元工厂 负责管理 对象池 和 创建享元对象 一般为单例，可以利用枚举类型
 *
 *  享元对象 只能由享元工厂创建，从而利用已经有的享元对象
 *  flyweightFactory，在连接池中，一般不直接叫 factory，而是叫 connectionPool，表示池子，更加直观
 *  Factory.getFlyWeight() 一般为懒加载模式，没有则创建，有则直接返回，
 *
 *  享元模式 中的 享元对象 可以没有外部状态，这样就是一个 单纯的缓冲池，比如 IntegerCache、常量池、对象池等
 *
 *  原始对象，可以拆成 享元对象 和 外部对象两部分，使用过程中，一般两种方式：
 *   1. 保留原始对象 在创建原始对象时 ： new Origin(Factory.getFlyweight(), externProperties),来代替
 *      可以将 常用的共享变量复用，减少内存使用
 *   2. 不保留原始对象，由 context环境 保留 享元对象 和 外部状态 的关系 ：
 *       context.put(Factory.getFlyweight(), externProperties)
 *      使用中，可以由环境灵活调用，也可以调用 flyweight.operation(externProperties) 的保留接口，让
 *      享元对象 根据外部变量不同，执行不同的逻辑
 *
 *   最常用的模型是 1. 没有外部状态的享元模式，由 factory 获取对象后，自己随意使用    IntegerCache、缓冲池
 *                2. 为了管理 环境中使用享元，由 factory 获取对象后，因为享元是无状态的，所以
 *                   在context中创建map，映射一个id，来代表享元                 sessionId 和 session池
 *                3. 享元 在同一时间只能由一个任务使用，在factory中，可以添加 remove方法，在获取
 *                   享元后移除，使用完，release它，将享元重新加入 pool           连接池、common-pool2
 */
public class Chessboard {

    private Map<AbstractChessPiece, Position> map = new HashMap<>();

    public Chessboard(){
        init(0, 0, "车", Color.GREEN);
    }

    private void init(int x, int y, String name, Color color){
        Position position = new Position(x, y);
        AbstractChessPiece chessPiece = FlyweightFactory.INSTANCE.getChessPiece(name, color);
        map.put(chessPiece, position);
    }

    public Map<AbstractChessPiece, Position> getMap(){
        return map;
    }

    public static void main(String[] args) {
        Chessboard chessboard = new Chessboard();
        Map<AbstractChessPiece, Position> chessboardMap = chessboard.getMap();
        chessboardMap.forEach(AbstractChessPiece::displayChessPieceInfo);
    }
}

class Position{
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}

abstract class AbstractChessPiece{
    private Color color;

    public AbstractChessPiece(Color color){
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public abstract void displayChessPieceInfo(Position position);
}

class RedChessPiece extends AbstractChessPiece{

    private String name;

    public RedChessPiece(String name) {
        super(Color.RED);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void displayChessPieceInfo(Position position) {
        if(position.getX() > 10){
            System.out.println("当前棋子已经过河");
        }else{
            System.out.println("当前棋子没有过河");
        }
        System.out.println("当前棋子:" + name +" " + getColor().getColor() + "的状态为：" + position.getX() + " " + position.getY());
    }
}

class GreenChessPiece extends AbstractChessPiece{

    private String name;

    public String getName() {
        return name;
    }

    public GreenChessPiece(String name) {
        super(Color.GREEN);
        this.name = name;
    }

    @Override
    public void displayChessPieceInfo(Position position) {
        if(position.getX() > 10){
            System.out.println("当前棋子已经过河");
        }else{
            System.out.println("当前棋子没有过河");
        }
        System.out.println("当前棋子:" + name +" " + getColor().getColor() + "的状态为：" + position.getX() + " " + position.getY());
    }
}

enum FlyweightFactory{
    INSTANCE;
    private Map<String, AbstractChessPiece> chessPiecePool = new HashMap<>();

    /**
     * 懒加载 获取享元 没有则创建，传入的参数必须满足 能够创建享元
     * @param name
     * @param color
     * @return
     */
    public AbstractChessPiece getChessPiece(String name, Color color){
        // 该 id 主要用于寻址，一般不能暴漏到环境中，因为享元对象都是 无状态的、共享的、没有身份标识的，
        //       和外部变量的映射关系由 不同的环境对象context自己管理，不能污染享元对象
        //       也可以用不用id，用set存储享元，但是寻找过程中，需要name、color一层层遍历列表，时间复杂度高
        // 如果传入的参数 有自己的唯一标识，不可以直接使用，
        //       如果唯一标识name相同，其他属性color不同，可能会造成返回的值和传入参数不匹配
        //       使用自己的id，来当作索引，则获取 享元 时，get只能传 id，不能传其他参数，否则不匹配
        //       自己实现 create 方法，还要自己保证传入的 id，没有被使用过，否则不匹配
        // 这样一来，factory形同虚设，违背了原则，尽量在环境对象中，保存 正在使用的享元对象 和 id、外部状态的映射
        String id = name + "-" + color;
        if (chessPiecePool.containsKey(id)) {
            return chessPiecePool.get(id);
        }
        AbstractChessPiece chessPiece = null;
        if(color.equals(Color.GREEN)){
            chessPiece = new GreenChessPiece(name);
        }else{
            chessPiece = new RedChessPiece(name);
        }
        chessPiecePool.put(id, chessPiece);
        return chessPiece;
    }

}

enum Color{
    RED("红色"),
    GREEN("绿色");
    private String color;
    Color(String color){
        this.color = color;
    }
    public String getColor(){
        return this.color;
    }
}