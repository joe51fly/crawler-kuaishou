package com.joe.kuaishou.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.joe.kuaishou.bean.MyInfo;
import com.joe.kuaishou.common.Result;
import com.joe.kuaishou.mapper.MyInfoMapper;
import com.joe.kuaishou.service.MyInfoService;
import com.joe.kuaishou.tools.KuaishouLiveKit;
import jnr.ffi.annotations.In;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyInfoServiceImpl implements MyInfoService {
    private static final Logger logger = LoggerFactory.getLogger(MyInfoServiceImpl.class);

    @Autowired
    MyInfoMapper myInfoMapper;

    @Override
    public boolean insertMyInfo(MyInfo myInfo) {
        return myInfoMapper.insertMyInfo(myInfo);
    }

    @Override
    public MyInfo getMyInfo() {
        return myInfoMapper.getMyInfo();
    }

    @Override
    public boolean updateMyInfo(MyInfo myInfo) {
        return myInfoMapper.updateMyInfo(myInfo);
    }

    public boolean insertOrUpdateMyInfo() {
        MyInfo myInfo = getMyInfo();
        if (myInfo != null) {
            logger.info("用户信息已存在:{}", myInfo);
            KuaishouLiveKit kuaishouLiveKit = new KuaishouLiveKit();
            MyInfo myInfoForUpdate = getMyInfo();
            String s = kuaishouLiveKit.myInfoCrawlerByEId(myInfoForUpdate.getMyEid());
            if (StringUtils.isNotBlank(s)) {
                if (!s.contains("errors")) {
                    logger.info("执行更新操作:{}", s);
                    JSONObject userProfile = JSONObject.parseObject(s).getJSONObject("data").getJSONObject("visionProfile").getJSONObject("userProfile");
                    JSONObject ownerCount = userProfile.getJSONObject("ownerCount");
                    String follow = ownerCount.getString("follow");
                    String fan = ownerCount.getString("fan");

                    JSONObject profile = userProfile.getJSONObject("profile");
                    String eid = profile.getString("user_id");

                    MyInfo myInfoUpdate = new MyInfo();
                    myInfoUpdate.setMyEid(eid);
                    myInfoUpdate.setMyFans(Integer.valueOf(fan));
                    myInfoUpdate.setMyFollow(Integer.valueOf(follow));

                    boolean b = updateMyInfo(myInfoUpdate);
                    if (b) {
                        logger.info("用户信息更新成功:{}", myInfoUpdate);
                        return true;
                    } else {
                        logger.error("用户信息更新失败:{}", myInfoUpdate);
                        return false;
                    }
                } else {
                    logger.error("读取列表失败:{}", s);
                    return false;
                }
            } else {
                logger.error(s);
                return false;
            }
        } else {
            //数据不存在，执行插入操作
            KuaishouLiveKit kuaishouLiveKit = new KuaishouLiveKit();
            String s = kuaishouLiveKit.myInfoCrawler();
            if (StringUtils.isNotBlank(s)) {
                if (!s.contains("errors")) {
                    JSONObject visionOwnerInfo = JSONObject.parseObject(s).getJSONObject("data").getJSONObject("visionOwnerInfo");
                    String id = visionOwnerInfo.getString("id");
                    String name = visionOwnerInfo.getString("name");
                    String avatar = visionOwnerInfo.getString("avatar");
                    String eid = visionOwnerInfo.getString("eid");
                    String userId = visionOwnerInfo.getString("userId");

                    MyInfo myInfoInsert = new MyInfo();
                    myInfoInsert.setMyId(id);
                    myInfoInsert.setMyAvatar(avatar);
                    myInfoInsert.setMyEid(eid);
                    myInfoInsert.setMyName(name);
                    myInfoInsert.setMyUserId(Long.valueOf(userId));

                    boolean b = insertMyInfo(myInfoInsert);
                    if (b) {
                        logger.info("用户信息插入成功:{}", myInfoInsert);
                        return true;
                    } else {
                        logger.error("用户信息插入失败:{}", myInfoInsert);
                        return false;
                    }
                } else {
                    logger.error("读取列表失败:{}", s);
                    return false;
                }
            } else {
                logger.error("读取列表失败:{}", s);
                return false;
            }
        }
    }
}
