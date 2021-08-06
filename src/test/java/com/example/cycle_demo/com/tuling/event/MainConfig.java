package com.example.cycle_demo.com.tuling.event;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Created by xsls on 2019/7/15.
 */
//@Configuration
//@ComponentScan(basePackages = {"com.example.cycle_demo.com.tuling.event"})
//@EnableAsync  异步事件
public class MainConfig {

   /*往SimpleApplicationEventMulticaster设置taskExecutor则为异步事件
     或者使用@Async
    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster
                = new SimpleApplicationEventMulticaster();

        eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return eventMulticaster;
    }*/
}
