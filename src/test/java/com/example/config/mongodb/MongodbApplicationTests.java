package com.example.config.mongodb;

import com.example.domain.mongo.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;


//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = MongodbApplication.class)

@SpringBootTest
@Slf4j
class MongodbApplicationTests {

    @Autowired
    private MongoTemplate mongoOps;

    @Test
    void contextLoads() {
        Product p = new Product();

        // 插入文档
//        mongoOps.insert(p);
//        log.info("Insert: " + p);

        // 查询文档
//        p = mongoOps.findById(1, Product.class);
//        log.info("Found: " + p);

        // 查询条件
        Criteria criteria = new Criteria();
        criteria.where("quantity").is(16).and("sku").is("abc123");
//        Criteria criteria1 = new Criteria();
        // 构建查询对象
        Query query = new Query();
        query.addCriteria(criteria);
//        query.addCriteria(criteria1);

        // 更新文档
        Update update = new Update();
        update.inc("quantity"); // 自增长
//        mongoOps.updateFirst(query,update,Product.class);
        mongoOps.upsert(query,update,Product.class);
    }

}
