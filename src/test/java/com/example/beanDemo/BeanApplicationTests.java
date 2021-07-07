package com.example.beanDemo;

import com.outside.Student;
import com.outside.StudentAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p> 通过 自定注解将指定类 交给spring托管 </p>
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
