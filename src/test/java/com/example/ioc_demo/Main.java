package com.example.ioc_demo;

import com.example.config.MainConfig;
import com.example.config.bean.A;
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

//        Object a = context.getBean(A.class);
//
//        System.out.println(a);

    }
}
