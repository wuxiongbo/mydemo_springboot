package com.outside;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <p> 自定义注解 </p>
 *
 * <pre>
 * @author wuxiongbo
 * @date 2021/6/18
 * </pre>
 */

//@Documented

// 这两个元注解不能少，否则 通过自定义注解 加载bean失败。
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)

// 将Student 托管给spring
@Import(Student.class)
public @interface StudentAnnotation {
}
