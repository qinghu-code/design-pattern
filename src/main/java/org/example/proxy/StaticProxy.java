//package org.example.proxy;
//
/**
 * 静态代理，代理类创建时，传入真实类
 *  1. 使用真实类实现接口
 *  2. 代理类内部实现增强方法
 *  3. 增强方法可以单独拆出来，形成增强类，但静态代理中 proxy类 和 real类 的数量一致，没必要单独拆出来
 */
//public class StaticProxy {
//    public static void main(String[] args) {
//        Producer producerReal = new RealProducer();
//        producerReal.product();
//        System.out.println("....................");
//        Producer producer = new ProxyProducer(producerReal);
//        producer.product();
//    }
//}
//
//interface Producer{
//    Object product();
//}
//
//class RealProducer implements Producer{
//    public Object product(){
//        System.out.println("生产产品1");
//        return null;
//    }
//}
//
//class ProxyProducer implements Producer{
//    private Producer producer;
//    public ProxyProducer(Producer producer) {
//        this.producer = producer;
//    }
//    public void before(){
//        System.out.println("产品生产评估");
//    }
//    @Override
//    public Object product() {
//        before();
//        Object o = producer.product();
//        after();
//        return o;
//    }
//
//    public void after(){
//        System.out.println("产品质量检测");
//    }
//}
//
//
//
