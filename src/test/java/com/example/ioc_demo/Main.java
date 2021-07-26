package com.example.ioc_demo;

import com.example.config.MainConfig;
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

//        Object a = context.getBean("getA");
//        Object a = context.getBean("callGetA");
        Object a = context.getBean("a");

        System.out.println(a);

    }
}
