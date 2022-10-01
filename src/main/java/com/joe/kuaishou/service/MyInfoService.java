package com.joe.kuaishou.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joe.kuaishou.bean.MyInfo;

public interface MyInfoService extends IService<MyInfo> {
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
