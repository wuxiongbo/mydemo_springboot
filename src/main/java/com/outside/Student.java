package com.outside;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * <p>描述类的信息</p>
 *
 * <pre>
 * @author wuxiongbo
 * @date 2021/6/18
 * </pre>
 */
@Data
public class Student {
    private String name;
    private Integer age;

    public void study(){
        System.out.println("我在学习");
    }
}
