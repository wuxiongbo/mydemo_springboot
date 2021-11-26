package com.example.config.demo;

import com.example.config.demo.bean.A;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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

@ComponentScan("com.example.config.demo.bean")
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
