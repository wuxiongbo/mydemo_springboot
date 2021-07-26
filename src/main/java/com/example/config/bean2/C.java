package com.example.config.bean2;

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
public class C {

    public C() {
        System.out.println("你好，我是 C");
    }

    @Override
    public String toString() {
        return "这是 C";
    }
}
