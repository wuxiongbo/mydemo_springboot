package com.example.mongodb;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * <p>产品</p>
 *
 * <pre>
 * @author wuxiongbo
 * @date 2021/6/15
 * </pre>
 */
@Data
@Document("products")
public class Product {
    String sku;
    Integer quantity;
    Metric metrics;

    @Data
    public static class Metric{
        Integer orders;
        Double ratings;
    }
}
