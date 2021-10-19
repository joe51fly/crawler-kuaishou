package com.joe.kuaishou.contraller;

import com.joe.kuaishou.common.Result;
import com.joe.kuaishou.tools.KuaishouLiveKit;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Controller
@CrossOrigin
@RequestMapping(value = "/",method = RequestMethod.POST)
public class LoginContraller {
    private static final Logger logger = LoggerFactory.getLogger(LoginContraller.class);
    private static final String ksProfilePath = "ksProfile.properties";

    /**
     * @apiDefine LoginContraller 登录模块
     */

    /**
     * @apiDescription 给后台传入 短视频Cookie 和 直播Cookie
     * @api {POST} /login
     * @apiGroup LoginContraller
     * @apiParam {String} callback 随便什么字符都行
     * @apiParam {String} shortVideoCookie 快手 短视频Cookie
     * @apiParam {String} liveCookie 快手 直播Cookie
     *
     * @apiSuccessExample {json} 成功响应:
     * {
     *  "success": true,
     *  "code": 20000,
     *  "message": "恭喜你设置Cookies成功",
     *  "data": {}
     * }
     * @apiVersion 1.0.0
     */
    @PostMapping("/login")
    @ResponseBody
    public Result login(@RequestParam(name = "callback") String callback,@RequestParam(name = "liveCookie") String liveCookie, @RequestParam(name = "shortVideoCookie") String shortVideoCookie){
//        if (StringUtils.isBlank(shortVideoCookie) || StringUtils.isBlank(liveCookie)){
//            logger.warn("短视频Cookie或直播Cookie不能为空");
//            return Result.error().message("短视频Cookie或直播Cookie不能为空");
//        }
        KuaishouLiveKit kslk = new KuaishouLiveKit();
        HashMap<String, String> ksProfileMap = kslk.readProperties(ksProfilePath);
        String ksProfileLiveCookie = ksProfileMap.get("liveCookie");
        String ksProfileShortVideoCookie = ksProfileMap.get("shortVideoCookie");
        String _liveCookie = null;
        String _shortVideoCookie = null;
        if (StringUtils.isNotBlank(liveCookie)){
            _liveCookie = liveCookie;
        } else if(StringUtils.isNotBlank(ksProfileLiveCookie) && !ksProfileLiveCookie.equals("1")){
            _liveCookie = ksProfileLiveCookie;
        }

        if (StringUtils.isNotBlank(shortVideoCookie)){
            _shortVideoCookie = shortVideoCookie;
        }else if(StringUtils.isNotBlank(ksProfileShortVideoCookie) && !ksProfileShortVideoCookie.equals("1")){
            _shortVideoCookie = ksProfileShortVideoCookie;
        }

        HashMap<String,String> dataMap = new HashMap<>();
        dataMap.put("liveCookie",_liveCookie);
        dataMap.put("shortVideoCookie",_shortVideoCookie);
        String comment = "cookies";
        kslk.writeProperties(dataMap,ksProfilePath,comment);

        return Result.ok().message("恭喜你设置Cookies成功");
    }

}
