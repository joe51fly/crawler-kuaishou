package com.joe.kuaishou.service;

import com.joe.kuaishou.bean.MyInfo;

public interface MyInfoService {
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


    boolean insertOrUpdateMyInfo();
}
