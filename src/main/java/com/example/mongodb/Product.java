package com.example.mongodb;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;

/**
 * <p>产品</p>
 *
 * <pre>
 * @author wuxiongbo
 * @date 2021/6/15
 * </pre>
 */
@Data
// mongo文档
@Document("products")
public class Product {
    String sku;
    Integer quantity;
//    @Valid // 嵌套验证必须用@Valid
    Metric metrics;

    @Data
    public static class Metric{
        Integer orders;
        Double ratings;
    }
}
