package com.joe.kuaishou.contraller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.joe.kuaishou.common.Result;
import com.joe.kuaishou.service.MyInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@CrossOrigin
@RequestMapping(value = "/my-info", method = RequestMethod.POST)
@ResponseBody
public class MyInfoContraller {
    private static final Logger logger = LoggerFactory.getLogger(MyInfoContraller.class);

    @Autowired
    MyInfoService myInfoService;

    /**
     * @apiDefine MyInfoContraller 我的信息模块
     */


    /**
     * @apiDescription 从数据库获取我的信息
     * @api {POST} /get-my-info
     * @apiGroup MyInfoContraller
     *
     * @apiSuccessExample {json} 成功响应:
     * {
     *   "success": true,
     *   "code": 20000,
     *   "message": "成功",
     *   "data": {
     *     "myUserId": 2296616163,
     *     "myId": "3xkmynpmsgf6eq4",
     *     "myFans": 7,
     *     "myFollow": 1764,
     *     "myName": "知足常乐132",
     *     "id": 1,
     *     "myAvatar": "https://ali2.a.yximgs.com/uhead/AB/2021/06/14/08/BMjAyMTA2MTQwODEwNDFfMjI5NjYxNjE2M18yX2hkOTc0XzM0MQ==_s.jpg",
     *     "myEid": "3xkmynpmsgf6eq4"
     *   }
     * }
     * @apiVersion 1.0.0
     */
    @PostMapping(value = "/get-my-info")
    public Result getMyInfo() {
        MyInfo myInfo = myInfoService.getMyInfo();
        if (myInfo == null) {
            logger.info("用户数据为空，请插入用户信息：", myInfo);
            return Result.error().message("用户数据为空，请插入用户信息");
        } else {
            Map map = JSONObject.parseObject(JSON.toJSONString(myInfo), Map.class);
            logger.info("用户信息:{}", myInfo);
            return Result.ok().data(map);
        }
    }

    /**
     * @apiDescription 插入或者更新我的信息到数据库
     * @api {POST} /insert-my-info
     * @apiGroup MyInfoContraller
     *
     * @apiSuccessExample {json} 成功响应:
     * {
     *   "success": true,
     *   "code": 20000,
     *   "message": "更新用户数据成功",
     *   "data": {}
     * }
     * @apiVersion 1.0.0
     */
    @PostMapping(value = "/insert-my-info")
    public Result insertMyInfo() {
        boolean b = myInfoService.insertOrUpdateMyInfo();
        if (b) {
            logger.info("更新用户数据成功");
            return Result.ok().message("更新用户数据成功");
        } else {
            logger.error("更新用户数据失败");
            return Result.error().message("更新用户数据失败");
        }
    }
}
