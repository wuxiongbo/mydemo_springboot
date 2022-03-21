package com.example.config.demo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>描述类的信息</p>
 *
 * <pre>
 * @author wuxiongbo
 * @date 2021/11/30
 * </pre>
 */
@ConfigurationProperties(prefix = "my")
@Data
@Component
public class ConfigDemo {
    private String name;
    private String age;
}
