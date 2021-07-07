package com.example.mongodb;

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
        p = mongoOps.findById(1, Product.class);
        log.info("Found: " + p);

        Criteria criteria = new Criteria();
        criteria.where("_id").is(1);

        Query query = new Query();
        query.addCriteria(criteria);

        Update update = new Update();
        update.inc("quantity");
        mongoOps.updateFirst(query,update,Product.class);

    }

}
