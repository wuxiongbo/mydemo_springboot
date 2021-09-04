package com.example.service;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.example.domain.XAccountInfo;
import com.example.mapper.XAccountInfoMapper;

import java.util.List;

@Service
public class XAccountInfoService{

    @Resource
    private XAccountInfoMapper xAccountInfoMapper;

    
    public int deleteByPrimaryKey(Long id) {
        return xAccountInfoMapper.deleteByPrimaryKey(id);
    }

    
    public int insert(XAccountInfo record) {
        return xAccountInfoMapper.insert(record);
    }

    
    public int insertSelective(XAccountInfo record) {
        return xAccountInfoMapper.insertSelective(record);
    }

    
    public XAccountInfo selectByPrimaryKey(Long id) {
        return xAccountInfoMapper.selectByPrimaryKey(id);
    }

    public List<XAccountInfo> selectByCondition(XAccountInfo xAccountInfo){
        return xAccountInfoMapper.selectByCondition(xAccountInfo);
    }


    public int updateByPrimaryKeySelective(XAccountInfo record) {
        return xAccountInfoMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(XAccountInfo record) {
        return xAccountInfoMapper.updateByPrimaryKey(record);
    }

}
