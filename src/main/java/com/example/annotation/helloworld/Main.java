package com.example.annotation.helloworld;

/**
 * <p> 注解是什么？ </p>
 * 注解是一种继承自接口`java.lang.annotation.Annotation`的特殊接口
 *
 * SimpleAnnotation extends Annotation
 *
 * 那么接口怎么能够设置属性呢？
 * 简单来说就是java通过动态代理的方式为你生成了一个实现了 接口 SimpleAnnotation 的实例
 * （对于当前的实体来说，例如类、方法、属性域等，这个代理对象是单例的），
 *
 * 然后对该代理实例的属性赋值，这样就可以在程序运行时（如果将注解设置为运行时可见的话）通过反射获取到注解的配置信息。
 *
 *
 * 本人实验发现无效。
 * 加上vm运行参数  -Dsun.misc.ProxyGenerator.saveGeneratedFiles=true
 *
 *
 * from  https://www.zhihu.com/question/24401191
 *
 * <pre>
 * @author wuxiongbo
 * @date 2021/7/22
 * </pre>
 */
@SimpleAnnotation(name="12321321",value="34123421")
public class Main {
    public static void main(String[] args){
//        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles","true");

        // Class 对象的 getAnnotation方法 ，获取类上注解。
        // Method 对象的 getAnnotation方法 ，获取方法上的注解。
        SimpleAnnotation annotation = Main.class.getAnnotation(SimpleAnnotation.class);

        // 打印出来，看到注解的实现 是个代理类。  com.sun.proxy.$Proxy1
        System.out.println(annotation.getClass().getName());
    }

}
