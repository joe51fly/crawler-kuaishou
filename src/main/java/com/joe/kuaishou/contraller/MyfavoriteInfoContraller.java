package com.joe.kuaishou.contraller;

import com.joe.kuaishou.bean.Myfavorite;
import com.joe.kuaishou.common.Result;
import com.joe.kuaishou.service.MyInfoService;
import com.joe.kuaishou.service.MyfavoriteService;
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
public class MyfavoriteInfoContraller {
    private static final Logger logger = LoggerFactory.getLogger(MyfavoriteInfoContraller.class);

    @Autowired
    MyfavoriteService myfavoriteService;
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
        Result result = myfavoriteService.addOrUpdateAllMyfavorite(inputKsAnchorId);
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
        Result result = myfavoriteService.addMyfavoriteList();
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
        Result result = myfavoriteService.updateIsMyfavorite(inputKsAnchorId, inputIsMyfavorite);
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
        List<Myfavorite> myfavoriteByIsMyfavorite = myfavoriteService.getMyfavoriteByIsMyfavorite(inputIsMyfavorite);
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

}
