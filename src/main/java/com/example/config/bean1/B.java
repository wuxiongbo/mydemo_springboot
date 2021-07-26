package com.example.config.bean1;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * <p>描述类的信息</p>
 *
 * <pre>
 * @author wuxiongbo
 * @date 2021/7/26
 * </pre>
 */
//@Component
//@ComponentScan("com.example.config.bean2")
public class B {
    public B() {
        System.out.println("你好，我是 B");
    }

    @Override
    public String toString() {
        return "这是 B";
    }
}
