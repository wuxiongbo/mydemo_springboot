package com.example.redis;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;

import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;


/**
 * <p>redisson的配置类</p>
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
        // 加载配置文件的方式一：
//        Config config = new Config();

        // 加载配置文件的方式二：
        // 加载redisson的json编解码器，需要使用 通过加载yaml配置文件创建放入配置类 才行，具体原因不详。
        // ClassPathResource类的构造方法接收路径名称，自动去classpath路径下找文件
        ClassPathResource classPathResource = new ClassPathResource("redisson.yaml");
        // 获得File对象，当然也可以获取输入流对象
        File file = classPathResource.getFile();
        // 加载配置文件
        Config config = Config.fromYAML(file);


        config.useSingleServer()
                .setAddress(addr)
                .setDatabase(database);

        // 创建客户端
        return Redisson.create(config);
    }

}
