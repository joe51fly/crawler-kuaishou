package com.joe.kuaishou.contraller;

import com.joe.kuaishou.bean.AnchorLiveInfo;
import com.joe.kuaishou.common.Result;
import com.joe.kuaishou.service.AnchorInfoService;
import com.joe.kuaishou.service.AnchorLiveInfoService;
import com.joe.kuaishou.service.MyfavoriteLiveService;
import com.joe.kuaishou.tools.NowDateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@CrossOrigin
@RequestMapping(value = "/live", method = RequestMethod.POST)
@ResponseBody
public class AnchorLiveInfoController {
    private static final Logger logger = LoggerFactory.getLogger(AnchorLiveInfoController.class);

    @Autowired
    AnchorLiveInfoService anchorLiveInfoService;

    @Autowired
    AnchorInfoService anchorInfoService;

    /**
     * @apiDefine MyfavoriteLiveContraller 关注的正在直播的主播的信息
     */

    /**
     * @apiDescription 从myfavorite_live_info 获取 isMyfavorite为true的信息----也就是添加特别关注的主播的信息
     * @api {POST} /getAllMySpecialFollowInfo 从主表获取添加特别关注的主播的信息
     * @apiName getAllMySpecialFollowInfo
     * @apiGroup MyfavoriteLiveContraller
     * @apiSuccessExample {json} 成功响应:
     * {
     *  "success": true,
     *  "code": 20000,
     *  "message": "String",
     *  "data": {}
     * }
     * @apiVersion 1.0.0
     */
    @PostMapping(value = "/getAllMySpecialFollowInfo")
    public Result getAllMySpecialFollowInfo() {
        List<AnchorLiveInfo> mySpecialFollows = anchorLiveInfoService.getMyfavoriteLiveInfoByIsMyfavorite(true);
        if (mySpecialFollows.size() > 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("results", mySpecialFollows);
            logger.info("查询我的特别关注成功");
            return Result.ok().data(map).message("查询我的特别关注成功");
        } else {
            logger.error("您还没有添加特别关注，请添加后再查");
            return Result.error().message("您还没有添加特别关注，请添加后再查");
        }
    }

    /**
     * @apiDescription 从myfavorite_live_info_temp表获取 已经添加特别关注 的主播的直播信息
     * @api {POST} /getOldMySpecialFollowFromTemp 从临时表获取 已经添加特别关注 的主播的直播信息
     * @apiName getOldMySpecialFollowFromTemp
     * @apiGroup MyfavoriteLiveContraller
     * @apiSuccessExample {json} 成功响应:
     * {
     *  "success": true,
     *  "code": 20000,
     *  "message": "String",
     *  "data": {}
     * }
     * @apiVersion 1.0.0
     */
    @PostMapping(value = "/getOldMySpecialFollowFromTemp")
    public Result getOldMySpecialFollowFromTemp() {
        List<AnchorLiveInfo> mySpecialFollowInfo = anchorLiveInfoService.getMySpecialFollowInfo();
        if (mySpecialFollowInfo.size() > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("result", mySpecialFollowInfo);
            logger.info("查询正在直播的特别关注的主播信息成功");
            /*//另起一个线程去执行 正在直播主播信息入临时表的操作
            new Thread(new InsertMyfavoriteLiveTempInfoRunnable())
                    .start();*/

            return Result.ok().data(map).message("查询正在直播的特别关注的主播信息成功");
        } else {
            logger.error("您还没有添加主播到特别关注");
            return Result.error().message("您还没有添加主播到特别关注");
        }
    }
    /**
     * @apiDescription 从myfavorite_live_info_temp表获取 已经添加特别关注 的主播的直播信息
     * @api {POST} /getNewMySpecialFollowFromTemp 从临时表获取 新插入的正在直播的主播信息
     * @apiName getNewMySpecialFollowFromTemp
     * @apiGroup MyfavoriteLiveContraller
     * @apiSuccessExample {json} 成功响应:
     * {
     *  "success": true,
     *  "code": 20000,
     *  "message": "String",
     *  "data": {}
     * }
     * @apiVersion 1.0.0
     */
    @PostMapping(value = "/getNewMySpecialFollowFromTemp")
    public Result getNewMySpecialFollowFromTemp() {
        Result newMySpecialFollowInfo = anchorLiveInfoService.getNewMySpecialFollowInfo();
        if (newMySpecialFollowInfo.getSuccess()) {
            logger.info("新的正在直播的特别关注的主播数据查询成功");
            return Result.ok().data(newMySpecialFollowInfo.getData()).message(newMySpecialFollowInfo.getMessage());
        } else {
            logger.error("您还没有添加主播到特别关注");
            return Result.error().message(newMySpecialFollowInfo.getMessage());
        }
    }

    @PostMapping(value = "/getNotIsMySpecialFollow")
    public Result getNotIsMySpecialFollow() {
        Result notIsMySpecialFollowInfo = anchorLiveInfoService.getNotIsMySpecialFollowInfo();
        if (notIsMySpecialFollowInfo.getSuccess()) {
            logger.info("成功获取不是特别关注的正在直播的主播列表");
            return Result.ok().data(notIsMySpecialFollowInfo.getData()).message(notIsMySpecialFollowInfo.getMessage());
        } else {
            logger.error("您还没有添加主播到特别关注");
            return Result.error().message(notIsMySpecialFollowInfo.getMessage());
        }
    }

    /**
     * @apiDescription 获取临时表myfavorite_live_info_temp的全部数据
     * @api {POST} /getAllFromTemp 获取临时表的全部数据
     * @apiName getAllFromTemp
     * @apiGroup MyfavoriteLiveContraller
     * @apiSuccessExample {json} 成功响应:
     * {
     *  "success": true,
     *  "code": 20000,
     *  "message": "String",
     *  "data": {}
     * }
     * @apiVersion 1.0.0
     */
    @PostMapping(value = "/getAllFromTemp")
    public Result getAllFromTemp() {
        List<AnchorLiveInfo> mySpecialFollowInfo = anchorLiveInfoService.getAllFromTemp();
        if (mySpecialFollowInfo.size() > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("result", mySpecialFollowInfo);
            logger.info("查询临时表数据成功");
            return Result.ok().data(map).message("查询临时表数据成功");
        } else {
            logger.error("临时表中还没有数据，请先添加后再查询");
            return Result.error().message("临时表中还没有数据，请先添加后再查询");
        }
    }

    /**
     * @apiDescription list批量插入到myfavorite_live_info-已关注主播正在直播的信息
     * @api {POST} /insertMyfavoriteLiveInfoByList list批量插入直播信息表到主表
     * @apiName insertMyfavoriteLiveInfoByList
     * @apiGroup MyfavoriteLiveContraller
     * @apiSuccessExample {json} 成功响应:
     * {
     *  "success": true,
     *  "code": 20000,
     *  "message": "String",
     *  "data": {}
     * }
     * @apiVersion 1.0.0
     */
    @PostMapping(value = "/insertMyfavoriteLiveInfoByList")
    public Result insertMyfavoriteLiveInfoByList() {
        Result result = anchorLiveInfoService.insertMyfavoriteLiveInfoByList(false);
        if (result.getSuccess()) {
            return result;
        } else {
            return result;
        }
    }

    /**
     * @apiDescription list批量插入到临时表myfavorite_live_info_temp-已关注主播正在直播的信息
     * @api {POST} /insertMyfavoriteLiveInfoByListForTemp list批量插入直播信息到临时表
     * @apiName insertMyfavoriteLiveInfoByListForTemp
     * @apiGroup MyfavoriteLiveContraller
     * @apiSuccessExample {json} 成功响应:
     * {
     *  "success": true,
     *  "code": 20000,
     *  "message": "String",
     *  "data": {}
     * }
     * @apiVersion 1.0.0
     */
    @PostMapping(value = "/insertMyfavoriteLiveInfoByListForTemp")
    public Result insertMyfavoriteLiveInfoByListForTemp() {
        //插入数据之前先看临时表有没有上次存留的数据 如果有的话清除数据
        List<AnchorLiveInfo> allFromTemp = anchorLiveInfoService.getAllFromTemp();
        if (allFromTemp.size() > 0) {
            anchorLiveInfoService.truncateTemp();
            logger.info("清除临时表数据操作完成");
        }
        Result result = anchorLiveInfoService.insertMyfavoriteLiveInfoByList(true);
        if (result.getSuccess()) {
            return result;
        } else {
            return result;
        }
    }


    /**
     * @apiDescription 清除临时表 myfavorite_live_info_temp 的数据
     * @api {POST} /truncateTemp 清除临时表的数据
     * @apiName truncateTemp
     * @apiGroup MyfavoriteLiveContraller
     * @apiSuccessExample {json} 成功响应:
     * {
     *  "success": true,
     *  "code": 20000,
     *  "message": "String",
     *  "data": {}
     * }
     * @apiVersion 1.0.0
     */
    @PostMapping(value = "/truncateTemp")
    public Result truncateTemp() {
        anchorLiveInfoService.truncateTemp();
        logger.info("临时表数据清除完成");
        return Result.ok().message("临时表数据清除完成");

    }

    /**
     * @apiDescription 通过Eid在 myfavorite_live_info 表中查询主播的信息
     * @api {POST} /getIsMyfavoriteByEid
     * @apiGroup MyfavoriteLiveContraller
     * @apiName getIsMyfavoriteByEid
     * @apiParam {String} inputEid 主播的Eid
     * @apiParamExample {json} 请求示例:
     * {
     *  "success": true,
     *  "code": 20000,
     *  "message": "String",
     *  "data": {}
     * }
     * @apiVersion 1.0.0
     */
    @PostMapping(value = "/getIsMyfavoriteByEid")
    public Result getIsMyfavoriteByEid(@RequestParam(name = "inputEid") String inputEid) {
        AnchorLiveInfo AnchorLiveInfoByEid = anchorLiveInfoService.getMyfavoriteLiveInfoByEid(inputEid);
        if (AnchorLiveInfoByEid != null) {
            logger.info("通过Eid查询主播信息成功");
            Map<String, Object> map = new HashMap<>();
            map.put("result", AnchorLiveInfoByEid);
            return Result.ok().data(map).message("通过Eid查询主播信息成功");
        } else {
            logger.error("库里没有该主播的信息，请添加");
            return Result.error().message("库里没有该主播的信息，请添加");
        }
    }

    /**
     * @apiDescription 在我的特别关注页面，置顶主播或者取消置顶
     * @api {POST} /updateForTheTopByIsTop
     * @apiGroup MyfavoriteLiveContraller
     * @apiName
     * @apiHeader {String} name=desc
     * @apiParam {String} inputEid 主播的eid
     * @apiParam {boolean} isSetTop 是否置顶主播
     *
     * @apiParamExample {json} 请求示例:
     * {
     *
     * }
     * @apiSuccessExample {json} 成功响应:
     * {
     *  "success": true,
     *  "code": 20000,
     *  "message": "String",
     *  "data": {}
     * }
     * @apiVersion 1.0.0
     */
    @PostMapping(value = "/updateForTheTopByIsTop")
    public Result updateForTheTopByIsTop(@RequestParam(name = "inputEid") String inputEid, @RequestParam(name = "isSetTop") boolean isSetTop){
        if (StringUtils.isNotBlank(inputEid)){
            Result result = anchorLiveInfoService.updateForTheTopByIsTop(inputEid, isSetTop);
            if (result.getSuccess()){
                return Result.ok().data(result.getData()).message(result.getMessage());
            }else {
                return Result.error().message(result.getMessage());
            }
        }else {
            logger.error("主播置顶设置失败:主播eid为空，请检查");
            return Result.error().message("主播eid为空，请检查");
        }
    }

    /**
     * @apiDescription 设置或者取消 超级置顶
     * @api {POST} /updateSuperSet_top
     * @apiGroup MyfavoriteLiveContraller
     * @apiName
     * @apiHeader {String} name=desc
     * @apiParam {String} inputEid 主播的Eid
     * @apiParam {String} isSetSuper_top 设置或者取消超级置顶
     *
     * @apiParamExample {json} 请求示例:
     * {
     * }
     * @apiSuccessExample {json} 成功响应:
     * {
     *  "success": true,
     *  "code": 20000,
     *  "message": "String",
     *  "data": {}
     * }
     * @apiVersion 1.0.0
     */
    @PostMapping(value = "/updateSuperSet_top")
    public Result updateSuperSet_top(@RequestParam(name = "inputEid") String inputEid, @RequestParam(name = "isSetSuper_top") boolean isSetSuper_top){
        if (StringUtils.isNotBlank(inputEid)){
            Result result = anchorLiveInfoService.updateSuperSet_top(inputEid, isSetSuper_top);
            if (result.getSuccess()){
                return Result.ok().data(result.getData()).message(result.getMessage());
            }else {
                return Result.error().message(result.getMessage());
            }
        }else {
            logger.error("主播置顶设置失败:主播eid为空，请检查");
            return Result.error().message("主播eid为空，请检查");
        }
    }
}
