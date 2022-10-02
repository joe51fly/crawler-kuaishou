package com.joe.kuaishou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.joe.kuaishou.bean.MyInfo;
import org.springframework.stereotype.Component;

@Component
public interface MyInfoMapper extends BaseMapper<MyInfo> {
    /*
    插入用户信息
     */
    boolean insertMyInfo(MyInfo myInfo);

    /*
    获取用户信息
     */
    MyInfo getMyInfo();

    /*
    更新用户信息
     */
    boolean updateMyInfo(MyInfo myInfo);
}
