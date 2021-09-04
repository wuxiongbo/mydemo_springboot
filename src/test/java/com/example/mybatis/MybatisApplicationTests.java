package com.example.mybatis;

import com.alibaba.fastjson.JSON;
import com.example.MongodbApplication;
import com.example.domain.XAccountInfo;
import com.example.service.XAccountInfoService;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * @author wuxiongbo
 * @date 2021/6/17
 * </pre>
 */
@SpringBootTest(classes = MongodbApplication.class)
@Slf4j
public class MybatisApplicationTests {

    @Autowired
    private XAccountInfoService xAccountInfoService;

    /**
     * 如果不设置redisson的序列化方式，这里会报错
     */
    @Test
    void stringTest(){
        XAccountInfo xAccountInfo = xAccountInfoService.selectByPrimaryKey(556L);
        System.out.println(xAccountInfo);
        String s = JSON.toJSONString(xAccountInfo);
        System.out.println(s);

        XAccountInfo condition = new XAccountInfo();
        condition.setStatus(0);
        List<XAccountInfo> xAccountInfos = xAccountInfoService.selectByCondition(condition);
        System.out.println(xAccountInfos);

    }


}
