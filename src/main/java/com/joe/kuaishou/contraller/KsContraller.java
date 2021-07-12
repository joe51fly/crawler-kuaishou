package com.joe.kuaishou.contraller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joe.kuaishou.bean.KsLiveMyfavorite;
import com.joe.kuaishou.common.Result;
import com.joe.kuaishou.service.KsLiveMyfavoriteService;
import com.joe.kuaishou.tools.KuaishouLiveKit;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Controller
@CrossOrigin
@RequestMapping(value = "/ks", method = RequestMethod.POST)
public class KsContraller {
    private static final Logger logger = LoggerFactory.getLogger(KsContraller.class);
    private static final String ksProfilePath = "ksProfile.properties";
    //存这次请求的Pcursor值，待请求下一页的时候要用
//    private String pcursor = "";
    @Autowired
    KsLiveMyfavoriteService ksLiveMyfavoriteService;

    @PostMapping(value = "/data")
    @ResponseBody
    public Result ksData(@RequestParam(name = "callback") String callback) {
        KuaishouLiveKit kslk = new KuaishouLiveKit();
        HashMap<String, String> ksProfileMap = kslk.readProperties(ksProfilePath);
        String userAgent = ksProfileMap.get("userAgent");
        String originalLiveUrl = ksProfileMap.get("originalLiveUrl");
        String liveCookie = ksProfileMap.get("liveCookie");
        String livePythonPath = ksProfileMap.get("livePythonPath");
        String _cookie = "";
        if (StringUtils.isNotBlank(liveCookie)) {
            _cookie = liveCookie;
        } else {
            logger.warn("配置文件里没有liveCookie的值，请重新输入liveCookie");
            return Result.error().message("配置文件里没有直播Cookie的值，请重新输入直播Cookie.");
//            return callback+" ("+"{\"error_msg\":\"配置文件里没有直播Cookie的值，请重新输入直播Cookie.\"}"+")";
        }
        String headers = "{'User-Agent': " + "\'" + userAgent + "\'" + ",'Cookie': " + "\'" + _cookie + "\'" + "}";
        String s = kslk.KsCrawlerKit(headers, null, originalLiveUrl, livePythonPath);

        if (StringUtils.isNotBlank(s)) {
            if (s.contains("hlsPlayUrl")) {
                return Result.ok().data("result", s);
            } else {
                logger.error("读取列表失败:{}", s);
                return Result.error().message("帐号异常，请重新登录");
//                return callback+" ("+"{\"error_msg\":\"帐号异常，请重新登录.\", \"tip\":\"如果是第一次登录,请输入正确的Cookie。不是第一次,请直接F5,会根据上次输入的Cookie登录\"}"+")";
            }
        }
        logger.error("返回数据是null才会打印这句话，请查找原因s:{}", s);
        return Result.error().message("帐号异常，请重新登录");
//        return callback+" ("+"{\"error_msg\":\"帐号异常，请重新登录.\", \"tip\":\"如果是第一次登录,请输入正确的Cookie。不是第一次,请直接F5,会根据上次输入的Cookie登录\"}"+")";
    }

    @PostMapping(value = "/myLikeData")
    @ResponseBody
    public Result ksMyLikeData(@RequestParam(name = "callback") String callback,@RequestParam(name = "pcursor") String pcursor) {
        KuaishouLiveKit kslk = new KuaishouLiveKit();
        HashMap<String, String> ksProfileMap = kslk.readProperties(ksProfilePath);
        String userAgent = ksProfileMap.get("userAgent");
        String originalPostUrl = ksProfileMap.get("originalPostUrl");
        String shortVideoHost = ksProfileMap.get("shortVideoHost");
        String shortVideoCookie = ksProfileMap.get("shortVideoCookie");
        String contentType = ksProfileMap.get("contentType");
        String myLikePythonPath = ksProfileMap.get("myLikePythonPath");
        String shortVideoMyLikePayload = null;
        if (StringUtils.isBlank(pcursor)) {
            //        String shortVideoMyLikePayload = "{'operationName':'visionProfileLikePhotoList','variables':{'pcursor':'','page':'profile'},'query':'query visionProfileLikePhotoList($pcursor: String, $page: String, $webPageArea: String) {\n  visionProfileLikePhotoList(pcursor: $pcursor, page: $page, webPageArea: $webPageArea) {\n    result\n    llsid\n    webPageArea\n    feeds {\n      type\n      author {\n        id\n        name\n        following\n        headerUrl\n        headerUrls {\n          cdn\n          url\n          __typename\n        }\n        __typename\n      }\n      tags {\n        type\n        name\n        __typename\n      }\n      photo {\n        id\n        duration\n        caption\n        likeCount\n        realLikeCount\n        coverUrl\n        coverUrls {\n          cdn\n          url\n          __typename\n        }\n        photoUrls {\n          cdn\n          url\n          __typename\n        }\n        photoUrl\n        liked\n        timestamp\n        expTag\n        animatedCoverUrl\n        stereoType\n        videoRatio\n        __typename\n      }\n      canAddComment\n      currentPcursor\n      llsid\n      status\n      __typename\n    }\n    hostName\n    pcursor\n    __typename\n  }\n}\n'}";
            shortVideoMyLikePayload = "{'operationName':'visionProfileLikePhotoList','variables':{'pcursor':'','page':'profile'},'query':'query visionProfileLikePhotoList($pcursor: String, $page: String, $webPageArea: String) {   visionProfileLikePhotoList(pcursor: $pcursor, page: $page, webPageArea: $webPageArea) {     result     llsid     webPageArea     feeds {       type       author {         id         name         following         headerUrl         headerUrls {           cdn           url           __typename         }         __typename       }       tags {         type         name         __typename       }       photo {         id         duration         caption         likeCount         realLikeCount         coverUrl         coverUrls {           cdn           url           __typename         }         photoUrls {           cdn           url           __typename         }         photoUrl         liked         timestamp         expTag         animatedCoverUrl         stereoType         videoRatio         __typename       }       canAddComment       currentPcursor       llsid       status       __typename     }     hostName     pcursor     __typename   } } '}";
        } else {
            shortVideoMyLikePayload = "{'operationName':'visionProfileLikePhotoList','variables':{'pcursor':" + pcursor + ",'page':'profile'},'query':'query visionProfileLikePhotoList($pcursor: String, $page: String, $webPageArea: String) {   visionProfileLikePhotoList(pcursor: $pcursor, page: $page, webPageArea: $webPageArea) {     result     llsid     webPageArea     feeds {       type       author {         id         name         following         headerUrl         headerUrls {           cdn           url           __typename         }         __typename       }       tags {         type         name         __typename       }       photo {         id         duration         caption         likeCount         realLikeCount         coverUrl         coverUrls {           cdn           url           __typename         }         photoUrls {           cdn           url           __typename         }         photoUrl         liked         timestamp         expTag         animatedCoverUrl         stereoType         videoRatio         __typename       }       canAddComment       currentPcursor       llsid       status       __typename     }     hostName     pcursor     __typename   } } '}";
        }
        String _cookie = "";
        if (StringUtils.isNotBlank(shortVideoCookie)) {
            _cookie = shortVideoCookie;
        } else {
            logger.warn("配置文件里没有shortVideoCookie的值，请重新输入shortVideoCookie");
            return Result.error().message("配置文件里没有短视频Cookie的值，请重新输入短视频Cookie.");
//            return callback+" ("+"{\"error_msg\":\"配置文件里没有短视频Cookie的值，请重新输入短视频Cookie.\"}"+")";
        }
//        String headers = "{'User-Agent': " +"\'"+ userAgent +"\'"+",'Cookie':" + "\'"+_cookie+"\'"+",'Content-Type':"+"\'"+contentType+"\'"+",'Host':"+"\'"+shortVideoHost+"\'" + "}";
        String headers = "{'User-Agent': " + "\'" + userAgent + "\'" + ",'Cookie':" + "\'" + _cookie + "\'" + ",'Content-Type':" + "\'" + contentType + "\'}";

        String s = kslk.KsCrawlerKit(headers, shortVideoMyLikePayload, originalPostUrl, myLikePythonPath);

        if (StringUtils.isNotBlank(s)) {
            if (s.contains("feeds")) {
//                ObjectMapper mapper = new ObjectMapper();
//                mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
//                    JsonNode jsonNode = mapper.readTree(s);
//                    map = mapper.readValue(s, Map.class);
                JSONObject jsonObject = JSONObject.parseObject(s);
                pcursor = jsonObject.getJSONObject("data").getJSONObject("visionProfileLikePhotoList").getString("pcursor");
//                JSONObject data = jsonObject.getJSONObject("data");
//                JSONObject visionProfileUserList = data.getJSONObject("visionProfileLikePhotoList");
//                pcursor = visionProfileUserList.getString("pcursor");

                //json对象转Map
                Map<String,Object> map = (Map<String,Object>)jsonObject;

                return Result.ok().data(map);
//                return callback+" ("+s+")";
            } else {
                logger.error("读取列表失败:{}", s);
                return Result.error().message("帐号异常，请重新登录");
//                return callback+" ("+"{\"error_msg\":\"帐号异常，请重新登录.\", \"tip\":\"如果是第一次登录,请输入正确的Cookie。不是第一次,请直接F5,会根据上次输入的Cookie登录\"}"+")";
            }
        }
        logger.error("返回数据是null才会打印这句话，请查找原因s:{}", s);
        return Result.error().message("帐号异常，请重新登录");
//        return callback+" ("+"{\"error_msg\":\"帐号异常，请重新登录.\", \"tip\":\"如果是第一次登录,请输入正确的Cookie。不是第一次,请直接F5,会根据上次输入的Cookie登录\"}"+")";
    }

    @PostMapping(value = "/addOrUpdateAllMyfavorite")
    @ResponseBody
    public Result addOrUpdateAllMyfavorite(@RequestParam(name = "callback") String callback, @RequestParam(name = "inputKsAnchorId") String inputKsAnchorId) {
        KsLiveMyfavorite ksLiveMyfavoriteByAnchorId = null;
        if (StringUtils.isNotBlank(inputKsAnchorId)) {
            ksLiveMyfavoriteByAnchorId = ksLiveMyfavoriteService.getKsLiveMyfavoriteByAnchorId(inputKsAnchorId);
        } else {
            logger.error("请输入要查询的用户id");
            return Result.error().message("请输入要查询的用户id");
//            return callback+" ("+"{\"error_msg\":\"请输入要查询的用户id\"}"+")";
        }
        if (ksLiveMyfavoriteByAnchorId != null) {
            //有该用户，执行update 操作
            ksLiveMyfavoriteByAnchorId.setMyfavorite(true);
            ksLiveMyfavoriteByAnchorId.setUpdateTime(new Date());
            boolean b = ksLiveMyfavoriteService.updateKsLiveMyfavoriteByid(ksLiveMyfavoriteByAnchorId);
            if (b) {
                logger.info("更新操作执行成功");
                return Result.ok().message("更新操作执行成功");
//                return callback+" ("+"{\"info_msg\":\"更新操作执行成功\"}"+")";
            } else {
                logger.error("更新操作失败");
                return Result.error().message("更新操作失败");
//                return callback+" ("+"{\"error_msg\":\"更新操作失败\"}"+")";
            }
        } else {
            logger.error("应该执行不到这里吧");
            return Result.error().message("应该执行不到这里吧");
        }
    }


    @PostMapping(value = "/addMyfavorite")
    public Result addMyfavorite(@RequestParam(name = "callback") String callback, @RequestParam(name = "myfavoriteId") String myfavoriteId) {
        int id = Integer.parseInt(myfavoriteId);
        KsLiveMyfavorite ksLiveMyfavorite = new KsLiveMyfavorite();
        ksLiveMyfavorite.setId(id);
        boolean b = ksLiveMyfavoriteService.updateKsLiveMyfavoriteByid(ksLiveMyfavorite);
        if (b) {
            logger.info("更新数据成功");
            return Result.ok().message("应该执行不到这里吧");
//            return callback+"("+"{info_msg\":\"更新数据成功.}"+")";
        } else {
            logger.warn("更新数据失败");
            return Result.error().message("更新数据失败");
//            return callback+"("+"{error_msg\":\"更新数据失败.}"+")";
        }
    }


    @PostMapping(value = "/test/live-data")
    @ResponseBody
    public Result testKsData(@RequestParam(name = "callback") String callback) throws JsonProcessingException {
        KuaishouLiveKit kslk = new KuaishouLiveKit();
        HashMap<String, String> ksProfileMap = kslk.readProperties(ksProfilePath);
        String s = ksProfileMap.get("testData");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        Map map = mapper.readValue(s, Map.class);
        return Result.ok().data(map);
//        if (StringUtils.isNotBlank(s)){
//            if (s.contains("hlsPlayUrl")){
//                return Result.ok().data("result",s);
//            }else {
//                logger.error("读取列表失败:{}",s);
//                return Result.error().message("帐号异常，请重新登录");
//            }
//        }
//        logger.error("返回数据是null才会打印这句话，请查找原因s:{}");
//        return Result.error().message("帐号异常，请重新登录");
    }


    @RequestMapping("index")
    public String index() {
        return "index";
    }

}
