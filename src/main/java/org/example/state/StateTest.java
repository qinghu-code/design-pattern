package org.example.state;

/**
 * 状态模式：主要用来解决对象在多种状态转换时，需要对外输出不同的行为的问题。
 *         状态和行为是一一对应的，状态之间可以相互转换, 类似 有限状态机
 *
 *         对象的内在状态改变时，允许改变其行为
 *  举例：订单的状态有 预订单 - 正式订单 - 付款 - 订单完成 每个状态都可能成功或者失败，
 *       并且各个不同的状态下，有不同的操作逻辑，转换十分麻烦，可以考虑状态模式
 *
 *  context：上下文环境的类，用来封装 和 转换 不同的状态和行为，也是对外暴漏的门户（facade 类似门面，但需要调度不同状态）
 *  abstract state：抽象状态类，一般引入 context上下文对象，指定不同状态的公共方法
 *                 （也可以设置 成功 或 失败 后，下一步的转换状态实体（template 模板模式，但不够灵活，谨慎使用））
 *  concrete state：具体的状态类，指定该状态下需要完成的信息，以及 处理 成功或者失败 后 为 context 指定 下一个state
 *
 *    状态模式，对调用者 仅仅暴漏 context 对象，所以 具体使用的状态，一般在 context 中作为属性
 *            初始化完成，并申明为 public 类似枚举模式。具体状态类，在 handle中，指定 下一个状态时，可以直接：
 *            super.getContext().setCurState(Context.PreState),来设置下一个状态流变
 *
 *            也可以，在 abstract state中，保留 成功或者失败后的 nextState，这样就可以在context中，
 *            指定 不同状态的 下一步 ，然后 具体状态类，可以仅仅实现 判定等操作，具体状态流转 由 context、
 *            abstract state完成，但不够灵活，谨慎使用
 *
 */
public class StateTest {
    public static void main(String[] args) {
        OrderContext orderContext = new OrderContext();
        orderContext.showStateInfo();

        orderContext.handle();
        orderContext.showStateInfo();

        orderContext.handle();
        orderContext.showStateInfo();

        orderContext.handle();
        orderContext.showStateInfo();

        orderContext.handle();
        orderContext.showStateInfo();

        orderContext.handle();
        orderContext.handle();
        orderContext.handle();
    }
}

abstract class OrderState{
    private OrderContext orderContext;
    private OrderState successOrderState;
    private OrderState failOrderState;

    public OrderState getSucessOrderState() {
        return successOrderState;
    }

    public void setSuccessOrderState(OrderState sucessOrderState) {
        this.successOrderState = sucessOrderState;
    }

    public OrderState getFailOrderState() {
        return failOrderState;
    }

    public void setFailOrderState(OrderState failOrderState) {
        this.failOrderState = failOrderState;
    }

    public OrderState(OrderContext orderContext){
        this.orderContext = orderContext;
    }

    public OrderContext getOrderContext() {
        return orderContext;
    }

    public abstract void showStateInfo();

    public abstract boolean isSuccess();

    public abstract boolean isDone();

    public abstract void handle();

}

class OrderContext{
    public final PreOrderState preOrderState = new PreOrderState(this);
    public final RealOrderState realOrderState = new RealOrderState(this);
    public final PayOrderState payOrderState = new PayOrderState(this);
    public final FinishOrderState finishOrderState = new FinishOrderState(this);
    {
        preOrderState.setSuccessOrderState(realOrderState);
        preOrderState.setFailOrderState(preOrderState);

        realOrderState.setSuccessOrderState(payOrderState);
        realOrderState.setFailOrderState(preOrderState);

        payOrderState.setSuccessOrderState(finishOrderState);
        payOrderState.setFailOrderState(preOrderState);
    }

    private OrderState orderState;

    public OrderContext() {
        orderState = this.preOrderState;
    }

    public void setCurState(OrderState orderState){
        this.orderState = orderState;
    }

    public OrderState getCurState(){
        return this.orderState;
    }

    public void showStateInfo(){
        this.orderState.showStateInfo();
    }

    public void handle(){
        this.orderState.handle();
    }
}

class PreOrderState extends OrderState{

    public PreOrderState(OrderContext orderContext) {
        super(orderContext);
    }

    @Override
    public void showStateInfo() {
        System.out.println("当前状态：预订单");
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public boolean isDone() {
        return true;
    }

    public void handle(){
        if(!isDone()){
            System.out.println("预订单状态 没有结束");
        }
        if(isSuccess()){
            System.out.println("预订单状态 成功");
            this.getOrderContext().setCurState(this.getSucessOrderState());
        }else{
            System.out.println("预订单状态 失败");
            this.getOrderContext().setCurState(this.getFailOrderState());
        }
    }

}

class RealOrderState extends OrderState{

    public RealOrderState(OrderContext orderContext) {
        super(orderContext);
    }

    @Override
    public void showStateInfo() {
        System.out.println("当前状态：正式订单");
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public boolean isDone() {
        return true;
    }

    public void handle(){
        if(!isDone()){
            System.out.println("正式订单状态 没有结束");
        }
        if(isSuccess()){
            System.out.println("正式订单状态 成功");
            this.getOrderContext().setCurState(this.getSucessOrderState());
        }else{
            System.out.println("正式订单状态 失败");
            this.getOrderContext().setCurState(this.getFailOrderState());
        }
    }

}

class PayOrderState extends OrderState{

    public PayOrderState(OrderContext orderContext) {
        super(orderContext);
    }

    @Override
    public void showStateInfo() {
        System.out.println("当前状态：订单付款");
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public boolean isDone() {
        return true;
    }

    public void handle(){
        if(!isDone()){
            System.out.println("订单付款状态 没有结束");
        }
        if(isSuccess()){
            System.out.println("订单付款状态 成功");
            this.getOrderContext().setCurState(this.getSucessOrderState());
        }else{
            System.out.println("订单付款状态 失败");
            this.getOrderContext().setCurState(this.getFailOrderState());
        }
    }

}

class FinishOrderState extends OrderState{

    public FinishOrderState(OrderContext orderContext) {
        super(orderContext);
    }

    @Override
    public void showStateInfo() {
        System.out.println("当前状态：订单完成");
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public boolean isDone() {
        return true;
    }

    public void handle(){
        System.out.println("订单完成并记录状态");
    }

}
