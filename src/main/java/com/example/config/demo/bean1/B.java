package com.example.config.demo.bean1;

/**
 * <p>描述类的信息</p>
 *
 * <pre>
 * @author wuxiongbo
 * @date 2021/7/26
 * </pre>
 */
//@Component
//@ComponentScan("com.example.config.demo.bean2")
public class B {
    public B() {
        System.out.println("你好，我是 B");
    }

    @Override
    public String toString() {
        return "这是 B";
    }
}
