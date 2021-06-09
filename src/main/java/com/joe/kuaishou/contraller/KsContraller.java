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
    /*private static String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36";
    *//*private final static String cookie = "kpn=GAME_ZONE; kuaishou.live.web_st=ChRrdWFpc2hvdS5saXZlLndlYi5zdBKgAfB4ePIvI1gvQN1OJIc9_JiPXGmD6jlxv2UtH7f4YzS2MWpISxwFRTpuOFX8QHFEloAwKH5-HmalniRCQ1wraDgmPzfRIWSlmPpFZMqYBv5Dauu-LJCRuENAb1DYfFhDCXMrQOf9uZbHqECuFacPAyRcKnL6she__-pMwYbrOqF3SIf2Bh8ddWQrs9U_cRoUp-_5jnn5sxyWWnjdN7UJBN0aEhq2DZZ7DUHSmXUzdRyAf3O3USIgxoDIjeyM_XnnvWwSuGYOrYJJQRJFARn9PQ82CQKluwwoBTAB; kuaishou.live.web_ph=8d2c394e2f6d9cee0e708196cd89a1730baf; userId=2296616163; clientid=3; did=web_906215bd0e2b75a2a8d9f656ec28b547; userId=2296616163; Hm_lvt_86a27b7db2c5c0ae37fee4a8a35033ee=1622027002; Hm_lpvt_86a27b7db2c5c0ae37fee4a8a35033ee=1622027002; kuaishou.live.bfb1s=9b8f70844293bed778aade6e0a8f9942; client_key=65890b29";*//*
    private static String originalUrl = "https://live.kuaishou.com/rest/wd/live/liveStream/myfollow";*/


    @RequestMapping(value = "data",method = RequestMethod.GET)
    @ResponseBody
    public String ksData(@RequestParam(name = "callback") String callback,@RequestParam(value = "inputCookie",required = false) String inputCookie){
        KuaishouLiveKit kslk = new KuaishouLiveKit();
        String originalUrl = kslk.readProperties("originalUrl");
        String userAgent = kslk.readProperties("userAgent");
        String dataCookie = kslk.readProperties("dataCookie");
        String _cookie = "";
        if (inputCookie != null && !"".equals(inputCookie)){
            _cookie = inputCookie;
        }else if (dataCookie != null && !"".equals(dataCookie)){
            _cookie = dataCookie;
        }
        String headers = "{'User-Agent': " +"\'"+ userAgent +"\'" +",'Cookie': " + "\'"+_cookie+"\'"+"}";
        String changedUrl = "\'" + originalUrl +"\'";

        String s = kslk.KsCrawlerKit(headers, originalUrl);

        if (s != null && !"".equals(s)){
            if (s.contains("hlsPlayUrl")){
                if (inputCookie != null && !"".equals(inputCookie)){
                    HashMap<String,String> dataMap = new HashMap<>();
                    dataMap.put("originalUrl",originalUrl);
                    dataMap.put("userAgent",userAgent);
                    dataMap.put("dataCookie",inputCookie);
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
