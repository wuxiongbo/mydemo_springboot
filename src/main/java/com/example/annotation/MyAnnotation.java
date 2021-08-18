package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * <p> AOP 之 自定义注解</p>
 *
 * <pre>
 * @author wuxiongbo
 * @date 2021/7/21
 *
 *
 * aop+自定义注解
 * https://www.cnblogs.com/chenglc/p/9642891.html
 *
 * </pre>
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})//使用位置（类，方法）
@Retention(RetentionPolicy.RUNTIME)//加载到jvm里运行
public @interface MyAnnotation {
    String value();              // 注解的属性，如果只有一个属性，一般叫value
    String name() default "";    // 定义name属性，默认值""，可以不写
}
