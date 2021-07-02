package com.outside;

import com.outside.Student;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <p>描述类的信息</p>
 *
 * <pre>
 * @author wuxiongbo
 * @date 2021/6/18
 * </pre>
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
// 将Student 托管给spring
@Import(Student.class)
public @interface StudentAnnotation {
}
