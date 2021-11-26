package com.example.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>描述类的信息</p>
 *
 * javax.validation 是基于JSR-303标准开发出来的，使用注解方式实现，及其方便，但是这只是一个接口，没有具体实现。
 * Hibernate-Validator是一个hibernate独立的包，可以直接引用，他实现了javax.validation同时有做了扩展，比较强大。
 * SpringBoot在内部集成了hibernate-validation，可以直接使用。
 *
 * @Null 被注解的元素必须为null
 * @NotNull 被注解的元素必须不为null
 * @AssertTrue 被注解的元素必须为true
 * @AssertFalse 被注解的元素必须为false
 * @Min(value) 被注解的元素必须为数字，其值必须大于等于最小值
 * @Max(value) 被注解的元素必须为数字，其值必须小于等于最小值
 * @Size(max,min) 被注解的元素的大小必须在指定范围内
 * @Past 被注解的元素必须为过去的一个时间
 * @Future 被注解的元素必须为未来的一个时间
 * @Pattern 被注解的元素必须符合指定的正则表达式
 *
 *  https://www.yanand.me/695.html
 *
 * <pre>
 * @author wuxiongbo
 * @date 2021/7/7
 * </pre>
 */

// @ApiModel注解，在swagger中，用来描述实体类。
//    不指定value， 显示类名 user，
//    指定value， 显示中文含义  "用户实体"
// @ApiModel("用户实体")
@ApiModel
@Data
public class User {
    /**
     * @ApiModelProperty 用于描述属性
     */
    @ApiModelProperty(value = "用户id")
    private Integer id;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "用户地址")
    private String address;

    @ApiModelProperty(value = "请求时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime time;

}

