package com.example.redis;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RBuckets;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * <p>描述类的信息</p>
 *
 * <pre>
 * @author wuxiongbo
 * @date 2021/6/17
 * </pre>
 */
@Slf4j
@Configuration
public class RedissonConfiguration {

    @Value("${myconfig.redis.addr}")
    private String addr;
    @Value("${myconfig.redis.database:0}")
    private int database;
    @Value("${myconfig.redis.ssl:false}")
    private Boolean userSSL;
    @Value("${myconfig.redis.trustStore:}")
    private String trustStore;
    @Value("${myconfig.redis.password:}")
    private String password;

    // https://stackoverflow.com/questions/45547557/redisson-spring-unexpected-exception-while-processing-command
    @Bean
    public RedissonClient redisson() throws IOException {
        Config config = new Config();
        // 用String编解码器。否则会报错。
        config.setCodec(new StringCodec());
//        config.setCodec(new TypedJsonJacksonCodec(String.class, new JsonJacksonCodec().getObjectMapper()));
        config.useSingleServer().setAddress(addr).setDatabase(database);
        return Redisson.create(config);
    }

    public static void main(String[] args){
        Config config = new Config();
        config.setCodec(new JsonJacksonCodec());
        config.useSingleServer().setAddress("redis://127.0.0.1:15235").setDatabase(0);
        RedissonClient redissonClient = Redisson.create(config);
        RBuckets buckets = redissonClient.getBuckets();
        Map<String, Object> user = buckets.get("user");
        System.out.println(user);
    }
}
