package com.example.mq.consumer;

/**
 * <p>描述类的信息</p>
 *
 * <pre>
 * @author wuxiongbo
 * @date 2022/1/26
 * </pre>
 */

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author ：楼兰
 * @date ：Created in 2020/10/22
 * @description:
 **/
@Component
@RocketMQMessageListener(consumerGroup = "MyConsumerGroup", topic = "TestTopic")
public class MyConsumerListener implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        System.out.println("Received message : "+ message);
    }

}