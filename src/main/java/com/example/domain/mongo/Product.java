package com.example.domain.mongo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;

/**
 * <p>产品</p>
 *
 *
 * https://www.cnblogs.com/myinspire/articles/7649027.html
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
//    @Valid   // 引用对象的嵌套验证，必须用 @Valid
    Metric metrics;

    @Data
    public static class Metric{
        Integer orders;
        Double ratings;
    }
}
