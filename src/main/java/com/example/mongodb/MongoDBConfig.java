package com.example.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * <p> MongoDB配置类 </p>
 *
 * <pre>
 * @author wuxiongbo
 * @date 2021/6/15
 * </pre>
 */
@Configuration
public class MongoDBConfig {

    /**
     * 1.建立 连接客户端
     * @return
     */
    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://localhost:27017");
    }

    /**
     * 2.操作类MongoTemplate
     * @return
     */
    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), "mycs");
    }
}
