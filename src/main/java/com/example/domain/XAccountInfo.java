package com.example.domain;

import lombok.Data;

/**
    * demo
    */
@Data
public class XAccountInfo {
    private Long id;

    /**
    * 客户id
    */
    private Long aid;

    /**
    * 姓名
    */
    private String name;

    /**
    * 联系地址
    */
    private String regionStr;

    /**
    * 身份号码
    */
    private String idcardNum;

    private Long createTime;

    private Long updateTime;

    /**
    * 0未审核 1已审核 2不通过
    */
    private Integer status;

    private String test;
}