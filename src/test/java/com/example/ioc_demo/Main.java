package com.example.ioc_demo;

import com.example.config.demo.MainConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * <p>描述类的信息</p>
 *
 * <pre>
 * @author wuxiongbo
 * @date 2021/7/26
 * </pre>
 */
public class Main {
    public static void main(String[] args){
        ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig.class);


        Object a = context.getBean("a");
        Object a1 = context.getBean("getA");
        Object a2 = context.getBean("callGetA");


        System.out.println("a:"+a.hashCode());
        System.out.println("a1:"+a1.hashCode());
        System.out.println("a2:"+a2.hashCode());

    }
}
