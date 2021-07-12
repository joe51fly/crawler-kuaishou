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
    private static final Logger logger = LoggerFactory.getLogger(KsContraller.class);
    private static final String ksProfilePath = "ksProfile.properties";

    //@RequestParam(name = "callback") String callback,
    @PostMapping("/login")
    @ResponseBody
    public Result login(@RequestParam(name = "callback") String callback,@RequestParam(name = "liveCookie") String liveCookie, @RequestParam(name = "shortVideoCookie") String shortVideoCookie){
        if (StringUtils.isBlank(shortVideoCookie) || StringUtils.isBlank(liveCookie)){
            logger.warn("短视频Cookie或直播Cookie不能为空");
            return Result.error().message("短视频Cookie或直播Cookie不能为空");
        }
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

//        return callback+" ("+"{\"msg\":\"恭喜你设置Cookies成功\"}"+")";
//        return "{\"msg\":\"恭喜你设置Cookies成功\"}";

        return Result.ok().message("恭喜你设置Cookies成功");
    }

}
