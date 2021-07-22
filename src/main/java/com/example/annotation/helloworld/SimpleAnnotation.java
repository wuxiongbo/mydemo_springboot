package com.example.annotation.helloworld;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p> 最简单的自定义注解 </p>
 *  反编译后的信息中可以看出，注解就是一个继承自`java.lang.annotation.Annotation`的接口
 *
 *
 * <pre>
 * @author wuxiongbo
 * @date 2021/7/22
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SimpleAnnotation {
    String value();
    String name();
}
