package com.example.controller;

import com.example.mq.producer.MqProducer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>描述类的信息</p>
 *
 * <pre>
 * @author wuxiongbo
 * @date 2022/1/26
 * </pre>
 */
@RestController
@RequestMapping("/MQTest")
public class MQTestController {

    private final String topic = "TestTopic";


    @Resource
    private MqProducer producer;
    @RequestMapping("/sendMessage")
    public String sendMessage(String message){
        producer.sendMessage(topic,message);
        return "消息发送完成";
    }


    //这个发送事务消息的例子中有很多问题，需要注意下。
    @RequestMapping("/sendTransactionMessage")
    public String sendTransactionMessage(String message) throws InterruptedException {
        producer.sendMessageInTransaction(topic,message);
        return "消息发送完成";
    }

}