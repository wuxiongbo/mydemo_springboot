package com.example.config.redis;

import com.example.MongodbApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>redisson</p>
 *
 * string（字符串），           二进制                                          可以包含任何数据,比如jpg图片或者序列化的对象,一个键最大能存储512M
 * hash（哈希），               键值对集合,即编程语言中的Map类型
 * list（列表），               链表(双向链表)
 * set（集合），                哈希表实现,元素不重复
 * zset(sorted set：有序集合)。 Set中的元素增加了一个权重参数score,元素按score有序排列
 *
 * <pre>
 * @author wuxiongbo
 * @date 2021/6/17
 * </pre>
 */
@SpringBootTest(classes = MongodbApplication.class)
@Slf4j
public class RedisApplicationTests {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * redis数据结构1：string
     *
     * 如果不设置redisson的序列化方式，这里会报错
     */
    @Test
    void string(){

        // set String
        RBucket<Object> bucket = redissonClient.getBucket("stringTest");
        bucket.set("中国");


        // update String
        bucket.compareAndSet("中国","日本");


        // 设置过期时间
        bucket.expire(10, TimeUnit.SECONDS);


        // get String
        bucket = redissonClient.getBucket("stringTest");
        String str = (String)bucket.get();
        System.out.println(str);
    }


    /**
     * redis数据结构2：list
     */
    @Test
    void list(){

        List<String> strList = new ArrayList<String>(){{
            add("1");
            add("2");
            add("3");
        }};


        // set List
        RList<Object> rList = redissonClient.getList("listTest");
        rList.addAll(strList);


        // get List
        rList = redissonClient.getList("listTest");
        List<Object> objects = rList.readAll();


        System.out.println(objects);
    }


    /**
     * redis数据结构3：hash
     */
    @Test
    void hash(){
        Map<String,String> map = new HashMap<String,String>(){{
            put("id","2");
            put("name","3");
        }};



        // set map
        RMap<Object, Object> rMap = redissonClient.getMap("hashTest");
        rMap.putAll(map);



        // get map
        Map<Object, Object> hash = rMap.readAllMap();
        System.out.println(hash);

    }


    /**
     * redis数据结构4：set
     */
    @Test
    void set(){
        List<String> strList = new ArrayList<String>(){{
            add("1");
            add("2");
            add("3");
        }};


        // set Set
        RSet<Object> rSet = redissonClient.getSet("setTest");
        rSet.addAll(strList);


        // get Set
        rSet = redissonClient.getSet("setTest");
        Set<Object> objects = rSet.readAll();
        System.out.println(objects);
    }


//========================================================================================

    @Test
    // https://www.codota.com/code/java/methods/org.redisson.api.RBucket/delete
    void delete(){

        // delete hash
        redissonClient.getBucket("hashTest").delete();

        // delete list
        redissonClient.getList("listTest").delete();
    }


    /**
     * 如果不设置redisson的序列化方式，这里会报错
     */
    @Test
    void beforeIncrement(){
        // 1.初始化一个空的 String 桶
        RBucket<Object> bucket = redissonClient.getBucket("incrementTest");

        Object result = bucket.get();
        System.out.println(result);
    }
    @Test
    void increment(){
        // 2.自增之后，再次看 刚初始化的 String 桶
        RAtomicLong atomicLong = redissonClient.getAtomicLong("incrementTest");
        long value = atomicLong.incrementAndGet();

        System.out.println(value);
    }
    @Test
    void expire(){
        RBucket<Object> bucket = redissonClient.getBucket("incrementTest");
        bucket.expire(10, TimeUnit.SECONDS);

        bucket = redissonClient.getBucket("setTest");
        bucket.expire(10, TimeUnit.SECONDS);
    }
}
