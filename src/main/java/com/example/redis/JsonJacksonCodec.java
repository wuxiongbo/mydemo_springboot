package com.example.redis;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

/**
 * <p>redisson 序列化</p>
 *
 *  redisson 是支持JsonJacksonCodec等各种序列化的，但是redisson-spring-boot-starter中却不能配置序列化
 *
 *  具体设置方式：
 *      1. 按照官方文档上写一个 redisson的yaml配置文件，
 *      2. application中 spring.redis.redisson.config 指定配置文件路径。
 *         或  Config config = Config.fromYAML(new File("config-file.yaml"));
 *
 *  https://blog.csdn.net/qq_23512279/article/details/107639179
 *  https://github.com/redisson/redisson/wiki/2.-%E9%85%8D%E7%BD%AE%E6%96%B9%E6%B3%95
 *
 * <pre>
 * @author wuxiongbo
 * @date 2021/7/7
 * </pre>
 */
//@Component
public class JsonJacksonCodec extends org.redisson.codec.JsonJacksonCodec {
    @Override
    protected void init(ObjectMapper objectMapper) {
        super.init(objectMapper);
        objectMapper.findAndRegisterModules();
    }

    @Override
    protected void initTypeInclusion(ObjectMapper objectMapper) {
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
    }
}
