package com.example.beanDemo;

import com.outside.Student;
import com.outside.StudentAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p> 通过 自定注解将扫描范围以外的指定类 交给spring托管 </p>
 *
 * @StudentAnnotation  自定义注解
 * com.outside.Student  springboot扫描范围之外的类
 *
 * <pre>
 * @author wuxiongbo
 * @date 2021/6/18
 * </pre>
 */
@SpringBootTest
@Slf4j
@StudentAnnotation
public class BeanApplicationTests {

    @Autowired
    private Student student;

    @Test
    void test(){
        student.study();
    }
}
