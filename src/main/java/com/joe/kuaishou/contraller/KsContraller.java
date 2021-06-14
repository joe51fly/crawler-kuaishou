package com.joe.kuaishou.contraller;

import com.joe.kuaishou.tools.KuaishouLiveKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;


@Controller
@CrossOrigin
public class KsContraller{
    private static final Logger logger = LoggerFactory.getLogger(KsContraller.class);

    @RequestMapping(value = "data",method = RequestMethod.GET)
    @ResponseBody
    public String ksData(@RequestParam(name = "callback") String callback,@RequestParam(value = "inputCookie",required = false) String inputCookie){
        KuaishouLiveKit kslk = new KuaishouLiveKit();
        String originalUrl = kslk.readProperties("originalUrl");
        String userAgent = kslk.readProperties("userAgent");
        String liveCookie = kslk.readProperties("liveCookie");
        String _cookie = "";
        if (inputCookie != null && !"".equals(inputCookie)){
            _cookie = inputCookie;
        }else if (liveCookie != null && !"".equals(liveCookie)){
            _cookie = liveCookie;
        }
        String headers = "{'User-Agent': " +"\'"+ userAgent +"\'" +",'Cookie': " + "\'"+_cookie+"\'"+"}";

        String s = kslk.KsCrawlerKit(headers, originalUrl);

        if (s != null && !"".equals(s)){
            if (s.contains("hlsPlayUrl")){
                if (inputCookie != null && !"".equals(inputCookie)){
                    HashMap<String,String> dataMap = new HashMap<>();
                    dataMap.put("originalUrl",originalUrl);
                    dataMap.put("userAgent",userAgent);
                    dataMap.put("liveCookie",inputCookie);
                    kslk.writeProperties(dataMap);
                }
                return callback+" ("+s+")";
            }else {
                logger.error("读取列表失败:{}",s);
                return callback+" ("+"{\"error_msg\":\"帐号异常，请重新登录.\", \"tip\":\"如果是第一次登录,请输入正确的Cookie。不是第一次,请直接F5,会根据上次输入的Cookie登录\"}"+")";
            }
        }
        logger.error("返回数据是null才会打印这句话，请查找原因s:{}",s);
        return callback+" ("+"{\"error_msg\":\"帐号异常，请重新登录.\", \"tip\":\"如果是第一次登录,请输入正确的Cookie。不是第一次,请直接F5,会根据上次输入的Cookie登录\"}"+")";
    }
    @RequestMapping("/")
    public String index(){
        return "index";
    }
}
