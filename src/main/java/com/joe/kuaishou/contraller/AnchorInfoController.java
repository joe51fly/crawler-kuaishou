package com.joe.kuaishou.contraller;

import com.joe.kuaishou.bean.AnchorInfo;
import com.joe.kuaishou.bean.AnchorLiveInfo;
import com.joe.kuaishou.common.Result;
import com.joe.kuaishou.service.AnchorInfoService;
import com.joe.kuaishou.service.MyInfoService;
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
@RequestMapping(value = "/myfavorite-info", method = RequestMethod.POST)
@ResponseBody
public class AnchorInfoController {
    private static final Logger logger = LoggerFactory.getLogger(AnchorInfoController.class);

    @Autowired
    AnchorInfoService anchorInfoService;
    @Autowired
    MyInfoService myInfoService;

    /**
     * @apiDefine MyfavoriteInfoContraller 我关注的主播模块
     */

    /**
     * @apiDescription 通过AnchorId 更新或插入主播数据
     * @api {POST} /addOrUpdateMyfavoriteByAnchorId
     * @apiGroup MyfavoriteInfoContraller
     * @apiParam {type} callback 随便什么字符都行
     * @apiParam {type} inputKsAnchorId 主播的eid
     * @apiParamExample {json} 请求示例:
     * {
     * }
     * @apiSuccessExample {json} 成功响应:
     * {
     * "code": 0,
     * "data": {},
     * "message": "string",
     * "success": true
     * }
     * @apiVersion 1.0.0
     */
    @PostMapping(value = "/addOrUpdateMyfavoriteByAnchorId")
    public Result addOrUpdateAllMyfavorite(@RequestParam(name = "callback") String callback, @RequestParam(name = "inputKsAnchorId") String inputKsAnchorId) {
        Result result = anchorInfoService.addOrUpdateAllMyfavorite(inputKsAnchorId);
        if (result.getSuccess()){
            return Result.ok().data(result.getData());
        }else {
            return Result.error().message(result.getMessage());
        }
    }

    /**
     * @apiDescription 通过list批量更新主播数据
     * @api {POST} /addMyfavoriteList
     * @apiGroup MyfavoriteInfoContraller
     * @apiParam {String} callback 随便什么字符都行
     * @apiSuccessExample {json} 成功响应:
     * {
     * "success": true,
     * "code": 20000,
     * "message": "更新数据成功",
     * "data": {}
     * }
     * @apiVersion 1.0.0
     */
    @PostMapping(value = "/addMyfavoriteList")
    public Result addMyfavoriteList(@RequestParam(name = "callback") String callback) {
        Result result = anchorInfoService.addMyfavoriteList();
        if (result.getSuccess()){
            return Result.ok().data(result.getData());
        }else {
            return Result.error().message(result.getMessage());
        }
    }

    /**
     * @apiDescription 添加主播到特别关注
     * @api {POST} /updateIsMyfavorite
     * @apiGroup MyfavoriteInfoContraller
     * @apiParam {String} callback 随便什么字符都行
     * @apiParam {String} inputKsAnchorId 主播的eid 例:3x5vunqghyenjsi
     * @apiSuccessExample {json} 成功响应:
     * {
     * "success": true,
     * "code": 20000,
     * "message": "成功添加主播到我的特别关注",
     * "data": {}
     * }
     * @apiVersion 1.0.0
     */
    @PostMapping(value = "/updateIsMyfavorite")
    public Result updateIsMyfavorite(@RequestParam(name = "callback") String callback, @RequestParam(name = "inputKsAnchorId") String inputKsAnchorId, @RequestParam(name = "inputIsMyfavorite") boolean inputIsMyfavorite) {
        Result result = anchorInfoService.updateIsMyfavorite(inputKsAnchorId, inputIsMyfavorite);
        if (result.getSuccess()){
            return Result.ok().data(result.getData());
        }else {
            return Result.error().message(result.getMessage());
        }
    }

    /**
     * @apiDescription 获取我特别关注主播的信息
     * @api {POST} /getMyfavoriteByIsMyfavorite
     * @apiGroup MyfavoriteInfoContraller
     * @apiHeader {String} name=desc
     * @apiParam {String} callback 随便什么字符都行
     * @apiParam {boolean} inputIsMyfavorite 是否添加到特别关注列表
     * @apiSuccessExample {json} 成功响应:
     * {
     * "success": true,
     * "code": 20000,
     * "message": "查询我的特别关注成功",
     * "data": {}
     * }
     * @apiVersion 1.0.0
     */
    @PostMapping(value = "/getMyfavoriteByIsMyfavorite")
    public Result getMyfavoriteByIsMyfavorite(@RequestParam(name = "callback") String callback, @RequestParam(name = "inputIsMyfavorite") boolean inputIsMyfavorite) {
        List<AnchorInfo> myfavoriteByIsMyfavorite = anchorInfoService.getMyfavoriteByIsMyfavorite(inputIsMyfavorite);
        if (myfavoriteByIsMyfavorite.size() > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("result", myfavoriteByIsMyfavorite);
            logger.info("查询我的特别关注成功");
            return Result.ok().message("查询我的特别关注成功").data(map);
        } else {
            logger.error("您还没有添加主播到特别关注");
            return Result.error().message("您还没有添加主播到特别关注");
        }
    }

    /**
     * @apiDescription 关注或者取消关注 直接作用到快手账号
     * @api {POST} /setFocusInOrNot
     * @apiGroup MyfavoriteInfoContraller
     * @apiName setFocusInOrNot
     * @apiHeader {String} name=desc
     * @apiParam {String} anchorEid 主播的eid
     * @apiParam {int} isFocusIn 1:关注  2:取消关注
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
    @PostMapping(value = "/setFocusInOrNot")
    public Result setFocusInOrNot(@RequestParam(name = "anchorEid") String anchorEid, @RequestParam(name = "isFocusIn") int isFocusIn) {
        Result result = anchorInfoService.focusInOrNot(anchorEid, isFocusIn);
        if (result.getSuccess()) {
            return Result.ok().message(result.getMessage());
        } else {
            return Result.error().message(result.getMessage());
        }
    }

    /**
     * @apiDescription 添加主播到特别关注 或者 移除特别关注
     * @api {POST} /updateMyfavLiveInfoByIsMyfavorite 添加主播到特别关注 或者 移除特别关注
     * @apiName updateMyfavLiveInfoByIsMyfavorite
     * @apiGroup MyfavoriteLiveContraller
     * @apiHeader {String} name=desc
     * @apiParam {String} inputEid 主播的eid
     * @apiParam {boolean} inputIsMyfavorite 添加或者移除
     * @apiSuccessExample {json} 成功响应:
     * {
     *  "success": true,
     *  "code": 20000,
     *  "message": "String",
     *  "data": {}
     * }
     * @apiVersion 1.0.0
     */
    @PostMapping(value = "/updateMyfavLiveInfoByIsMyfavorite")
    public Result updateMyfavLiveInfoByIsMyfavorite(@RequestParam(name = "inputEid") String inputEid, @RequestParam(name = "inputIsMyfavorite") boolean inputIsMyfavorite) {
        if (StringUtils.isBlank(inputEid)) {
            logger.error("eid不能为空");
            return Result.error().message("eid不能为空");
        }
        AnchorInfo anchorInfo = new AnchorInfo();
        anchorInfo.setIsMyfavorite(inputIsMyfavorite);
        anchorInfo.setAnchorId(inputEid);
        anchorInfo.setUpdateTime(NowDateUtils.getDaDate());

        AnchorInfo myfavoriteByAnchorId = anchorInfoService.getMyfavoriteByAnchorId(inputEid);
        String anchorName = myfavoriteByAnchorId.getAnchorName();
        Map<String, Object> map = new HashMap<>();
        map.put("anchorName", anchorName);
        if (inputIsMyfavorite) {
            boolean b = anchorInfoService.update(anchorLiveInfo);
            if (b) {
                logger.info("AnchorLiveInfo-添加特别关注成功");
                return Result.ok().data(map).message("添加特别关注成功");
            } else {
                logger.error("AnchorLiveInfo-添加特别关注失败");
                return Result.error().message("添加特别关注失败");
            }
        } else {
            boolean b = anchorLiveInfoService.updateIsMyfavoriteLiveInfoByIsMyfavorite(anchorLiveInfo);
            if (b) {
                logger.info("AnchorLiveInfo-移除特别关注成功");
                return Result.ok().data(map).message("移除特别关注成功");
            } else {
                logger.error("AnchorLiveInfo-移除特别关注成功");
                return Result.error().message("移除特别关注成功");
            }
        }
    }

}
