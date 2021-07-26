package com.example.config;

import com.example.config.bean.A;
import com.example.config.bean1.B;
import com.example.config.bean2.C;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

/**
 * <p>主配置类 demo</p>
 *
 * <pre>
 * @author wuxiongbo
 * @date 2021/7/26
 * </pre>
 */

// Full配置类
// boolean proxyBeanMethods() default true;
@Configuration

// Lite配置类
//@Component

//@ComponentScan("com.example.config.bean")
//@Import({C.class,B.class})
public class MainConfig {

    @Bean
    public A getA(){
        return new A();
    }

    @Bean
    public A callGetA(){
        return getA();
    }

//    @Bean
//    public B getB(){
//        return new B();
//    }
//
//    @Bean
//    public C getC(){
//        return new C();
//    }

}
