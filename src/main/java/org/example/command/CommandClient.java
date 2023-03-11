package org.example.command;

/**
 * 命令模式 行为型模式 解耦了 发送方 和 接受方 中间通过 命令对象 来联系 类似 单模块的门面模式
 *  1. 对于 调用方，只依赖命令对象，具体实现，由 命令对象 主动调用 命令接受方执行
 *     类似 shell 只写入命令，命令调用操作系统执行
 *  2. CommandClient 相当于 client 客户端，用来组合 不同的 命令 和 命令接受方的映射
 *  3. 命令对象本身可拓展性强，可以有不同的校验方法和执行流程，对应不同的命令
 *     不同的命令和不同的命令执行器之间 是 多 对 多的关系， 具体组合由 client决定
 *  4. 命令模式可以结合责任链模式，实现命令族解析任务；
 *     结合模板方法模式，则可以减少Command子类的膨胀问题。
 *
 */
public class CommandClient {

    public static void main(String[] args) {
        CommandExecutor executor = new ConcreteCommandExecutor();
        Command command = new ConcreteCommand(executor);
        Sender sender = new Sender(command);
        sender.send();
    }

}

interface CommandExecutor {
    void executeCommand();
}

class ConcreteCommandExecutor implements CommandExecutor {
    public void doSomeThing() {
        System.out.println("做一些事情");
    }

    @Override
    public void executeCommand() {
        System.out.println(this.getClass().getName() + " 执行命令");
        doSomeThing();
    }
}

abstract class Command {
    private CommandExecutor commandExecutor;

    public abstract void valid();

    public Command(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    public void execute() {
        valid();
        commandExecutor.executeCommand();
    }
}

class ConcreteCommand extends Command{
    @Override
    public void valid() {
        System.out.println("检测命令");
    }

    public ConcreteCommand(CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
}

class Sender{
    private Command command;

    public Sender(Command command){
        this.command = command;
    }

    public void send(){
        command.execute();
    }
}