package org.example.interprete;

/**
 * 解释器模式：就是给定一个语言的文法表示，并且定义一个解释器，用来解释语言中的句子。
 *          解释器模式描述了怎样在有了一个简单的文法后，使用模式设计解释这些语句。
 *  优点：
 *    扩展性好。由于在解释器模式中使用类来表示语言的文法规则，因此可以通过继承等机制来改变或扩展文法。
 *    容易实现。在语法树中的每个表达式节点类都是相似的，所以实现其文法较为容易。
 *
 * 缺点：
 *    执行效率较低。解释器模式中通常使用大量的循环和递归调用，当要解释的句子较复杂时，其运行速度很慢，且代码的调试过程也比较麻烦。
 *    会引起类膨胀。解释器模式中的每条规则至少需要定义一个类，当包含的文法规则很多时，类的个数将急剧增加，导致系统难以管理与维护。
 *    可应用的场景比较少。在软件开发中，需要定义语言文法的应用实例非常少，所以这种模式很少被使用到。
 *
 * 1 抽象表达式(Expression)角色：
 *      声明一个所有的具体表达式角色都需要实现的抽象接口。接口主要是一个interpret()方法，称做解释操作。
 *
 * 2 终结符表达式(Terminal Expression)角色：
 *      实现了抽象表达式角色所要求的接口，主要是一个interpret()方法；
 *      文法中的每一个终结符都有一个具体终结表达式与之相对应。
 *      比如有一个简单的公式R=R1+R2，在里面R1和R2就是终结符，对应的解析R1和R2的解释器就是终结符表达式。
 *
 * 3 非终结符表达式(NonTerminal Expression)角色：
 *       文法中的每一条规则都需要一个具体的非终结符表达式，
 *       非终结符表达式一般是文法中的运算符或者其他关键字，
 *       比如公式R=R1+R2中，“+"就是非终结符，解析“+”的解释器就是一个非终结符表达式。
 *
 * 4 环境(Context)角色：
 *     一般是用来存放文法中各个终结符所对应的具体值，
 *     比如R=R1+R2，给R1赋值100，给R2赋值200。这些信息需要存放到环境角色中，一般使用Map来充当环境角色就足够。
 */

import java.beans.Expression;
import java.util.*;

/**我们自己设计一种语言来说明这一模式
        （1）该语言区分大小写
        （2）该语言以PROGRAM开头，END结尾
        （3）PRINTLN表示打印一行并换行
        （4）使用FOR…FROM…TO…END表示循环

   引用自：https://www.cnblogs.com/fantongxue/p/16802225.html
**/
public class Context {
    private final StringTokenizer stringTokenizer;
    // 当前命令
    private String currentToken;
    // 用来存储动态变化信息内容
    private final Map<String, Object> map = new HashMap<>();
    /**
     * 构造方法设置解析内容
     * @param text
     */
    public Context(String text) {
        // 使用空格分隔待解析文本内容
        this.stringTokenizer = new StringTokenizer(text);
    }
    /**
     * 解析文本
     */
    public String next() {
        if (this.stringTokenizer.hasMoreTokens()) {
            currentToken = this.stringTokenizer.nextToken();
        } else {
            currentToken = null;
        }
        return currentToken;
    }
    /**
     * 判断命令是否正确
     * @param command
     * @return
     */
    public boolean equalsWithCommand(String command) {
        if (command == null || !command.equals(this.currentToken)) {
            return false;
        }
        return true;
    }
    /**
     * 获得当前命令内容
     * @return
     */
    public String getCurrentToken() {
        return this.currentToken;
    }
    /**
     * 获得节点的内容
     * @return
     */
    public String getTokenContent(String text) {
        String str = text;
        if (str != null) { // 替换map中的动态变化内容后返回 Iterator<String>
            // 替换map中的动态变化内容后返回
            Iterator<String> iterator = this.map.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Object obj = map.get(key);
                str = str.replaceAll(key, obj.toString());
            }
        }
        return str;
    }
    public void put(String key, Object value) {
        this.map.put(key, value);
    }
    public void clear(String key) {
        this.map.remove(key);
    }
    public static void main(String[] args) {
        String str = "PROGRAM PRINTLN" +
                " start... " +
                "FOR i FROM 90 TO 104 " +
                "PRINTLN i " +
                "END PRINTLN" +
                " end..." +
                " END";
        System.out.println("str:" + str);
        // 创建PROGRAM表达式
        IExpressions expressions = new ProgramExpression(str);
        // 解释执行
        expressions.interpret();
    }
}

interface IExpressions {
    /**
     * 解析
     * @param context
     */
    public void parse(Context context);
    /**
     * 执行方法
     */
    public void interpret();
}

class ProgramExpression implements IExpressions {
    // 上下文环境
    private final Context context;
    // 当前命令
    private final static String COMMAND = "PROGRAM";
    // 存储下一个表达式引用
    private IExpressions expressions;
    /**
     * 构造方法将待解析的内容传入
     *
     * @param text
     */
    public ProgramExpression(String text) {
        this.context = new Context(text);
        this.parse(this.context);
    }
    @Override
    public void parse(Context context) {
        // 获取第一个命令节点
        this.context.next();
    }
    /**
     * 实现解释方法
     */
    @Override
    public void interpret() {
        // 判断是否是以PROGRAM 开始
        if (!this.context.equalsWithCommand(COMMAND)) {
            System.out.println("The '" + COMMAND + "' is Excepted For Start!");
        } else {
            // 是以PROGRAM 开始
            this.context.next();
            this.expressions = new ListExpression();
            this.expressions.parse(this.context);
            // ListExpression表达式开始解析
            this.expressions.interpret();
        }
    }
}
class ListExpression implements IExpressions {
    private Context context;
    private final ArrayList<IExpressions> list = new ArrayList<IExpressions>();
    /**
     * 构造方法将待解析的context传入
     *
     * @param context
     */
    public void parse(Context context) {
        this.context = context;
        // 在ListExpression解析表达式中,循环解释语句中的每一个单词,直到终结符表达式或者异常情况退出
        while (true) {
            if (this.context.getCurrentToken() == null) {
                // 获取当前节点如果为 null 则表示缺少END表达式
                System.out.println("Error: The Experssion Missing 'END'! ");
                break;
            } else if (this.context.equalsWithCommand("END")) {
                this.context.next();
                // 解析正常结束
                break;
            } else {
                // 建立Command 表达式
                IExpressions expressions = new CommandExperssion(this.context);
                // 添加到列表中
                list.add(expressions);
            }
        }
    }
    /**
     * 实现解释方法
     */
    @Override
    public void interpret() {
        // 循环list列表中每一个表达式 解释执行
        Iterator<IExpressions> iterator = list.iterator();
        while (iterator.hasNext()) {
            (iterator.next()).interpret();
        }
    }
}

class CommandExperssion implements IExpressions {
    private final Context context;
    private IExpressions expressions;
    /**
     * 构造方法将待解析的context传入
     *
     * @param context
     */
    public CommandExperssion(Context context) {
        this.context = context;
        this.parse(this.context);
    }
    public void parse(Context context) {
        // 判断当前命令类别 在此只对For和最原始命令进行区分
        if (this.context.equalsWithCommand("FOR")) {
            // 创建For表达式进行解析
            expressions = new ForExpression(this.context);
        } else {
            // 创建原始命令表达式进行内容解析
            expressions = new PrimitiveExpression(this.context);
        }
    }
    /**
     * 解析内容
     */
    @Override
    public void interpret() {
        // 解析内容
        this.expressions.interpret();
    }
}

class ForExpression implements IExpressions {
    private final Context context;
    // 存储当前索引key值
    private String variable;
    // 存储循环起始位置
    private int start_index;
    // 存储循环结束位置
    private int end_index;
    private IExpressions expressions;
    /**
     * 构造方法将待解析的context传入
     *
     * @param context
     */
    public ForExpression(Context context) {
        this.context = context;
        this.parse(this.context);
    }
    /**
     * 解析表达式
     */
    @Override
    public void parse(Context context) {
        // 首先获取当前节点
        this.context.next();
        while (true) {
            // 判断节点
            if (this.context.equalsWithCommand("FROM")) {
                // 设置开始索引内容
                String nextStr = this.context.next();
                try {
                    this.start_index = Integer.parseInt(nextStr);
                } catch (Exception e) {
                    System.out
                            .println("Error: After 'FROM' Expression Exist Error!Please Check the Format Of Expression is Correct!");
                    break;
                }
                // 获取下一个节点
                this.context.next();
            } else if (this.context.equalsWithCommand("TO")) {
                // 设置结束索引内容
                String nextStr = this.context.next();
                try {
                    this.end_index = Integer.parseInt(nextStr);
                } catch (Exception e) {
                    System.out
                            .println("Error: After 'TO' Expression Exist Error!Please Check the Format Of Expression is Correct!");
                }
                this.context.next();
                break;
            } else {
                // 设置当前索引变量内容
                if (this.variable == null) {
                    this.variable = this.context.getCurrentToken();
                }
                // 获取下一个节点
                this.context.next();
            }
        }
        // 建立列表表达式
        this.expressions = new ListExpression();
        this.expressions.parse(this.context);
    }
    /**
     * 实现解释方法
     */
    @Override
    public void interpret() {
        // 建立命令表达式
        for (int x = this.start_index; x <= this.end_index; x++) {
            // 设置变量内容
            this.context.put("" + this.variable, x);
            // 执行解释方法
            this.expressions.interpret();
        }
        // 移除使用的临时变量内容
        this.context.clear("" + this.variable);
    }
}

class PrimitiveExpression implements IExpressions {
    private Context context;
    // 节点名称
    private String tokenName;
    // 文本内容
    private String text;
    /**
     * 构造方法将待解析的context传入
     *
     * @param context
     */
    public PrimitiveExpression(Context context) {
        this.parse(context);
    }
    @Override
    public void parse(Context context) {
        this.context = context;
        this.tokenName = this.context.getCurrentToken();
        this.context.next();
        if ("PRINTLN".equals(this.tokenName)) {
            this.text = this.context.getCurrentToken();
            this.context.next();
        }
    }
    /**
     * 实现解释方法
     */
    @Override
    public void interpret() {
        // 首先获取当前节点内容
        if ("PRINTLN".equals(tokenName)) {
            // 获得内容信息
            // 打印内容
            System.out.println(this.context.getTokenContent(this.text));
        }
    }
}