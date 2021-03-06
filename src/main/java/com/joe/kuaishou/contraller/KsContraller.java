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
    private static final String ksProfilePath = "ksProfile.properties";

    @RequestMapping(value = "data",method = RequestMethod.GET)
    @ResponseBody
    public String ksData(@RequestParam(name = "callback") String callback,@RequestParam(value = "inputCookie",required = false) String inputCookie){
        KuaishouLiveKit kslk = new KuaishouLiveKit();
        HashMap<String, String> ksProfileMap = kslk.readProperties(ksProfilePath);
        String userAgent = ksProfileMap.get("userAgent");
        String originalLiveUrl = ksProfileMap.get("originalLiveUrl");
        String liveCookie = ksProfileMap.get("liveCookie");
        String livePythonPath = ksProfileMap.get("livePythonPath");
        String _cookie = "";
        if (inputCookie != null && !"".equals(inputCookie)){
            _cookie = inputCookie;
        }else if (liveCookie != null && !"".equals(liveCookie)){
            _cookie = liveCookie;
        }
        String headers = "{'User-Agent': " +"\'"+ userAgent +"\'" +",'Cookie': " + "\'"+_cookie+"\'"+"}";

        String s = kslk.KsCrawlerKit(headers,null, originalLiveUrl,livePythonPath);
        String comment = "ksLiveProfile";
        if (s != null && !"".equals(s)){
            if (s.contains("hlsPlayUrl")){
                if (inputCookie != null && !"".equals(inputCookie)){
                    HashMap<String,String> dataMap = new HashMap<>();
                    dataMap.put("originalLiveUrl",originalLiveUrl);
                    dataMap.put("userAgent",userAgent);
                    dataMap.put("liveCookie",inputCookie);
                    kslk.writeProperties(dataMap,ksProfilePath,comment);
                }
                return callback+" ("+s+")";
            }else {
                logger.error("??????????????????:{}",s);
                return callback+" ("+"{\"error_msg\":\"??????????????????????????????.\", \"tip\":\"????????????????????????,??????????????????Cookie??????????????????,?????????F5,????????????????????????Cookie??????\"}"+")";
            }
        }
        logger.error("???????????????null???????????????????????????????????????s:{}",s);
        return callback+" ("+"{\"error_msg\":\"??????????????????????????????.\", \"tip\":\"????????????????????????,??????????????????Cookie??????????????????,?????????F5,????????????????????????Cookie??????\"}"+")";
    }

    @PostMapping(value = "myLikeData")
    @ResponseBody
    public String ksMyLikeData(@RequestParam(name = "callback") String callback,@RequestParam(value = "inputinputShortVideoCookie",required = false) String inputShortVideoCookie){
        KuaishouLiveKit kslk = new KuaishouLiveKit();
        HashMap<String, String> ksProfileMap = kslk.readProperties(ksProfilePath);
        String userAgent = ksProfileMap.get("userAgent");
        String originalPostUrl = ksProfileMap.get("originalPostUrl");
        String shortVideoHost = ksProfileMap.get("shortVideoHost");
        String shortVideoCookie = ksProfileMap.get("shortVideoCookie");
        String contentType = ksProfileMap.get("contentType");
        String myLikePythonPath = ksProfileMap.get("myLikePythonPath");

//        String shortVideoMyLikePayload = "{'operationName':'visionProfileLikePhotoList','variables':{'pcursor':'','page':'profile'},'query':'query visionProfileLikePhotoList($pcursor: String, $page: String, $webPageArea: String) {\n  visionProfileLikePhotoList(pcursor: $pcursor, page: $page, webPageArea: $webPageArea) {\n    result\n    llsid\n    webPageArea\n    feeds {\n      type\n      author {\n        id\n        name\n        following\n        headerUrl\n        headerUrls {\n          cdn\n          url\n          __typename\n        }\n        __typename\n      }\n      tags {\n        type\n        name\n        __typename\n      }\n      photo {\n        id\n        duration\n        caption\n        likeCount\n        realLikeCount\n        coverUrl\n        coverUrls {\n          cdn\n          url\n          __typename\n        }\n        photoUrls {\n          cdn\n          url\n          __typename\n        }\n        photoUrl\n        liked\n        timestamp\n        expTag\n        animatedCoverUrl\n        stereoType\n        videoRatio\n        __typename\n      }\n      canAddComment\n      currentPcursor\n      llsid\n      status\n      __typename\n    }\n    hostName\n    pcursor\n    __typename\n  }\n}\n'}";
        String shortVideoMyLikePayload = "{'operationName':'visionProfileLikePhotoList','variables':{'pcursor':'','page':'profile'},'query':'query visionProfileLikePhotoList($pcursor: String, $page: String, $webPageArea: String) {   visionProfileLikePhotoList(pcursor: $pcursor, page: $page, webPageArea: $webPageArea) {     result     llsid     webPageArea     feeds {       type       author {         id         name         following         headerUrl         headerUrls {           cdn           url           __typename         }         __typename       }       tags {         type         name         __typename       }       photo {         id         duration         caption         likeCount         realLikeCount         coverUrl         coverUrls {           cdn           url           __typename         }         photoUrls {           cdn           url           __typename         }         photoUrl         liked         timestamp         expTag         animatedCoverUrl         stereoType         videoRatio         __typename       }       canAddComment       currentPcursor       llsid       status       __typename     }     hostName     pcursor     __typename   } } '}";
        String _cookie = "";
        if (inputShortVideoCookie != null && !"".equals(inputShortVideoCookie)){
            _cookie = inputShortVideoCookie;
        }else if (shortVideoCookie != null && !"".equals(shortVideoCookie)){
            _cookie = shortVideoCookie;
        }
        String headers = "{'User-Agent': " +"\'"+ userAgent +"\'"+",'Cookie':" + "\'"+_cookie+"\'"+",'Content-Type':"+"\'"+contentType+"\'"+",'Host':"+"\'"+shortVideoHost+"\'" + "}";

        String s = kslk.KsCrawlerKit(headers,shortVideoMyLikePayload, originalPostUrl,myLikePythonPath);
        String comment = "ksMyLike";
        if (s != null && !"".equals(s)){
            if (s.contains("feeds")){
                if (inputShortVideoCookie != null && !"".equals(inputShortVideoCookie)){
                    HashMap<String,String> dataMap = new HashMap<>();
                    dataMap.put("originalPostUrl",originalPostUrl);
                    dataMap.put("userAgent",userAgent);
                    dataMap.put("shortVideoCookie",inputShortVideoCookie);
                    dataMap.put("shortVideoHost",shortVideoHost);
                    dataMap.put("contentType",contentType);
                    kslk.writeProperties(dataMap,ksProfilePath,comment);
                }
                return callback+" ("+s+")";
            }else {
                logger.error("??????????????????:{}",s);
                return callback+" ("+"{\"error_msg\":\"??????????????????????????????.\", \"tip\":\"????????????????????????,??????????????????Cookie??????????????????,?????????F5,????????????????????????Cookie??????\"}"+")";
            }
        }
        logger.error("???????????????null???????????????????????????????????????s:{}",s);
        return callback+" ("+"{\"error_msg\":\"??????????????????????????????.\", \"tip\":\"????????????????????????,??????????????????Cookie??????????????????,?????????F5,????????????????????????Cookie??????\"}"+")";
    }

    @RequestMapping("index")
    public String index(){
        return "index";
    }

    @PostMapping("/")
    public String login(@RequestParam(name = "liveCookie") String liveCookie,@RequestParam(name = "shortVideoCookie") String shortVideoCookie){
        return "login";
    }
}
