package com.joe.kuaishou.mapper;

import com.joe.kuaishou.bean.MyInfo;
import org.springframework.stereotype.Component;

@Component
public interface MyInfoMapper {
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
