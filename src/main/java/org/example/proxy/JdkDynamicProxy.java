package org.example.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * jdk 动态代理和 cglib代理 类似
 *  1. MethodInterceptor 对应 jdk invocationHandler 接口
 *  2. Enhancer 对应 jdk Proxy
 *
 * jdk 动态代理 使用jdk特性 invocationHandler 类 和 proxy 类，动态创建 代理类
 *  1. 创建通用代理类 JdkProxyHandler 实现 invocationHandler接口
 *     其类似静态代理中的 代理类，invoke方法就相当于实现了接口
 *  2. 通用代理类中可以使用增强方法，在invoke方法中使用，可以单独拆出来，形成 enhance类
 *      和静态代理不同，通用代理类 handler 可以被多个 实现类使用，
 *      所以可以简历 enhance和 realObject 的映射关系 或者 enhance和 method的映射
 *      实现aop
 *  3. Proxy类 通过静态方法，使用 handler和真实类，动态创建接口代理类（相较与静态代理 唯一多的步骤）
 */
public class JdkDynamicProxy {
    public static void main(String[] args) {
        Producer producer = new RealProducer();
        InvocationHandler handler = new JdkProxyHandler(producer, new RealEnhance());
        //new Class<?>[]{Producer.class} 同 producer.getClass().getInterfaces()
        Producer proxyProducer = (Producer) Proxy.newProxyInstance(producer.getClass().getClassLoader(),
                producer.getClass().getInterfaces(), handler);
        producer.product();
        System.out.println("............");
        proxyProducer.product();
    }
}

interface Producer {
    Object product();
}

class RealProducer implements Producer {
    public Object product() {
        System.out.println("生产产品1");
        return null;
    }
}

class JdkProxyHandler implements InvocationHandler {
    private Object object;
    private Enhance enhance;

    public JdkProxyHandler(Object object, Enhance enhance) {
        this.object = object;
        this.enhance = enhance;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        enhance.before();
        Object invoke = method.invoke(object, args);
        enhance.after();
        return invoke;
    }
}

interface Enhance{
    void before();
    void after();
}

class RealEnhance implements Enhance{

    @Override
    public void before() {
        System.out.println("产品生产评估");
    }

    @Override
    public void after() {
        System.out.println("产品质量检测");
    }
}
