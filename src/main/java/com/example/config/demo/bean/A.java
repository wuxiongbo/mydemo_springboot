package com.example.config.demo.bean;

import org.springframework.stereotype.Component;

/**
 * <p>描述类的信息</p>
 *
 * <pre>
 * @author wuxiongbo
 * @date 2021/7/26
 * </pre>
 */
@Component
//@ComponentScan("com.example.config.demo.bean1")
public class A {

    public A() {
        System.out.println("你好，我是A");
    }

    @Override
    public String toString() {
        return "这是A";
    }
}
