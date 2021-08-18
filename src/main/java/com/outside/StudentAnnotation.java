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

// 这两个 “元注解” 不能少，否则 通过自定义注解 加载bean失败。

@Target(ElementType.TYPE) //使用位置 （TYPE 类，METHOD 方法）
@Retention(RetentionPolicy.RUNTIME) //加载到jvm里运行

// 通过 @Import注解，手动的 将扫描范围以外的bean -- Student 交给spring托管
@Import(Student.class)
public @interface StudentAnnotation {
    String value() default "";           //注解的属性。 如果只有一个属性，一般叫value
    String name() default ""; //注解的属性。 默认值""，可以不写
}
