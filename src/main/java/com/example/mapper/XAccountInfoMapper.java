package com.example.mapper;

import com.example.domain.XAccountInfo;

import java.util.List;

public interface XAccountInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(XAccountInfo record);

    int insertSelective(XAccountInfo record);

    XAccountInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(XAccountInfo record);

    int updateByPrimaryKey(XAccountInfo record);

    List<XAccountInfo> selectByCondition(XAccountInfo xAccountInfo);
}