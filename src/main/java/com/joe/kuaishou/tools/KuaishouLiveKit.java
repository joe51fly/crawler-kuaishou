package com.joe.kuaishou.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.joe.kuaishou.bean.MyInfo;
import com.joe.kuaishou.common.Result;
import com.joe.kuaishou.service.MyInfoService;
import com.joe.kuaishou.service.impl.MyInfoServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;

import javax.naming.spi.ResolveResult;
import java.io.*;
import java.util.*;

/**
 * 获取快手我的关注正在直播的列表信息
 */
public class KuaishouLiveKit {
    private static final Logger logger = LoggerFactory.getLogger(KuaishouLiveKit.class);
    private static final String ksProfilePath = "ksProfile.properties";

    private String threadData;

    public String getThreadData() {
        return threadData;
    }

    public void setThreadData(String threadData) {
        this.threadData = threadData;
    }

    /**
     * 调用Python爬虫
     *
     * @param headers
     * @param payload
     * @param url
     * @param pythonPath
     * @return 爬虫返回的信息
     */
    public String KsCrawlerKit(String headers, String payload, String url, String pythonPath) {
        Process process = null;
        try {
            String[] args = null;
            if (StringUtils.isBlank(payload)) {
                args = new String[]{"python", pythonPath, headers, url};
            } else {
                args = new String[]{"python", pythonPath, headers, payload, url};
            }
            process = Runtime.getRuntime().exec(args);
            InputStream inputStream = process.getInputStream();
            InputStream errorStream = process.getErrorStream();
//            readStreamInfo(errorStream, inputStream);
//            Thread.sleep(500);
//            //启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流
            new Thread() {
                public void run() {
                    int i = 0;
                    while (i < 1) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuffer stringBuffer = new StringBuffer();
                        String line = "";
                        try {
                            while ((line = bufferedReader.readLine()) != null) {
                                stringBuffer.append(line);
                                logger.info("python爬虫返回信息:{} ", line);
                                threadData = stringBuffer.toString();
                                i = 1;
                            }
                            i = 1;
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (bufferedReader != null) {
                                try {
                                    bufferedReader.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        BufferedReader errorBufferedReader = null;
                        try {
                            if (errorStream.available() != 0) {
                                errorBufferedReader = new BufferedReader(new InputStreamReader(errorStream));
                                StringBuffer errorStringBuffer = new StringBuffer();
                                String errorLine = "";
                                try {
                                    while ((errorLine = errorBufferedReader.readLine()) != null) {
                                        errorStringBuffer.append(errorLine);
                                        logger.error("python爬虫返回错误信息:{} ", errorLine);
                                        threadData = errorStringBuffer.toString();
                                        i = 1;
                                    }
                                    i = 1;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (errorBufferedReader != null) {
                                try {
                                    bufferedReader.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }.start();

            int i = process.waitFor();
            Thread.sleep(1000);
            if (i == 0) {
                logger.info("python爬虫正常完成");
            } else {
                logger.error("python爬虫异常结束");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                try {
                    if (process.getErrorStream() != null) {
                        process.getErrorStream().close();
                    }
                    if (process.getInputStream() != null) {
                        process.getInputStream().close();
                    }
                    if (process.getOutputStream() != null) {
                        process.getOutputStream().close();
                    }
                    process.destroy();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        /*return stringBuffer.toString();*/
        return threadData;
    }


    /**
     * 读取Runtime.exec运行子进程的输入流
     *
     * @param inputStreams 输入流
     */
    private void readStreamInfo(InputStream... inputStreams) {

        new Thread() {
            public void run() {
                int i = 0;
                while (i < 1) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStreams[1]));
                    StringBuffer stringBuffer = new StringBuffer();
                    String line = "";
                    try {
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuffer.append(line);
                            logger.info("python爬虫返回信息:{} ", line);
                            threadData = stringBuffer.toString();
                            i = 1;
                        }
                        i = 1;
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    BufferedReader errorBufferedReader = null;
                    try {
                        if (inputStreams[0].available() != 0) {
                            errorBufferedReader = new BufferedReader(new InputStreamReader(inputStreams[0]));
                            StringBuffer errorStringBuffer = new StringBuffer();
                            String errorLine = "";
                            try {
                                while ((errorLine = errorBufferedReader.readLine()) != null) {
                                    errorStringBuffer.append(errorLine);
                                    logger.error("python爬虫返回错误信息:{} ", errorLine);
                                    threadData = errorStringBuffer.toString();
                                    i = 1;
                                }
                                i = 1;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (errorBufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }.start();
    }
//    private void readStreamInfo(InputStream... inputStreams) {
//        //启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流
//        new Thread() {
//            public void run() {
//                try {
//                    if (inputStreams[1].available() > 0) {
//                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStreams[1]));
//                        StringBuffer stringBuffer = new StringBuffer();
//                        String line = "";
//                        try {
//
//                            while ((line = bufferedReader.readLine()) != null) {
//                                stringBuffer.append(line);
//                                logger.info("python爬虫返回信息:{} ", line);
//                                threadData = stringBuffer.toString();
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } finally {
//                            if (bufferedReader != null) {
//                                try {
//                                    bufferedReader.close();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//
//        new Thread() {
//            public void run() {
//                BufferedReader errorBufferedReader = null;
//                try {
//                    logger.info("错误输入流inputStreams[0]：{}", inputStreams[0]);
//                    if (inputStreams[0] != null) {
//                        if (inputStreams[0].available() != 0) {
//                            errorBufferedReader = new BufferedReader(new InputStreamReader(inputStreams[0]));
//                            StringBuffer errorStringBuffer = new StringBuffer();
//                            String errorLine = "";
//                            try {
//                                while ((errorLine = errorBufferedReader.readLine()) != null) {
//                                    errorStringBuffer.append(errorLine);
//                                    logger.error("python爬虫返回错误信息:{} ", errorLine);
//                                    threadData = errorStringBuffer.toString();
//                                }
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    if (errorBufferedReader != null) {
//                        try {
//                            errorBufferedReader.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }.start();
//
//    }


    public void writeProperties(HashMap<String, String> mapData, String ksProfilePath, String comment) {
        String path = this.getClass().getClassLoader().getResource(ksProfilePath).getPath();
        Properties properties = new Properties();
        FileWriter fileWriter = null;
        try {
            Iterator<Map.Entry<String, String>> entryIterator = mapData.entrySet().iterator();
            while (entryIterator.hasNext()) {
                Map.Entry entry = (Map.Entry) entryIterator.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                if (StringUtils.isNotBlank((String) value)) {
                    properties.setProperty((String) key, (String) value);
                } else {
                    continue;
                }
//                System.out.println(key+":"+value);
            }
            /*//直播数据写入
            String originalUrl = mapData.get("originalUrl");
            String userAgent = mapData.get("userAgent");
            String liveCookie = mapData.get("liveCookie");
            //短视频数据的写入
            String originalPostUrl = mapData.get("originalPostUrl");
            String shortVideoCookie = mapData.get("shortVideoCookie");
            String shortVideoHost = mapData.get("shortVideoHost");
            String contentType = mapData.get("contentType");

            if (originalPostUrl != null) {
                properties.setProperty("userAgent", userAgent);
                properties.setProperty("originalPostUrl", originalPostUrl);
                properties.setProperty("shortVideoCookie", shortVideoCookie);
                properties.setProperty("shortVideoHost", shortVideoHost);
                properties.setProperty("contentType", contentType);
            } else {
                properties.setProperty("originalUrl", originalUrl);
                properties.setProperty("userAgent", userAgent);
                properties.setProperty("liveCookie", liveCookie);
            }*/
            fileWriter = new FileWriter(path, true);
            properties.store(fileWriter, comment);   //保存到流
            logger.info("数据写入完成：{}", mapData.toString());
        } catch (FileNotFoundException e) {
            logger.error("数据读取失败：{}", e);
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("数据读取失败：{}", e);
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public HashMap<String, String> readProperties(String ksProfilePath) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(ksProfilePath);
        Properties properties = new Properties();
        HashMap<String, String> ksProfileMap = new HashMap<String, String>();
        try {
            properties.load(resourceAsStream);    //从流读取properties文件内容
            Set<String> ksProfileKey = properties.stringPropertyNames();
            Iterator<String> ksyIterator = ksProfileKey.iterator();
            while (ksyIterator.hasNext()) {
                String key = ksyIterator.next();
                String value = properties.getProperty(key);
                ksProfileMap.put(key, value);
            }
            return ksProfileMap;
        } catch (IOException e) {
            logger.error("数据读取失败：{}", e);
            e.printStackTrace();
        } finally {
            if (resourceAsStream != null) {
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ksProfileMap;
    }

    public Result myfavoriteLiveAllData() {
        HashMap<String, String> ksProfileMap = readProperties(ksProfilePath);
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
        }
        String headers = "{'User-Agent': " + "\'" + userAgent + "\'" + ",'Cookie': " + "\'" + _cookie + "\'" + "}";
        String s = KsCrawlerKit(headers, null, originalLiveUrl, livePythonPath);

        if (StringUtils.isNotBlank(s)) {
            if (s.contains("hlsPlayUrl")) {
                JSONObject jsonObject = JSONObject.parseObject(s);
                Map<String, Object> map = (Map<String, Object>) jsonObject;
                return Result.ok().data(map);
            } else {
                logger.error("读取列表失败:{}", s);
                return Result.error().message("帐号异常，请重新登录");
            }
        }
        logger.error("返回数据是null才会打印这句话，请查找原因s:{}", s);
        return Result.error().message("帐号异常，请重新登录");
    }

    public Result ksMyLikeShortVideoData(String pcursor) {
        HashMap<String, String> ksProfileMap = readProperties(ksProfilePath);
        String userAgent = ksProfileMap.get("userAgent");
        String originalPostUrl = ksProfileMap.get("originalPostUrl");
        String shortVideoHost = ksProfileMap.get("shortVideoHost");
        String shortVideoCookie = ksProfileMap.get("shortVideoCookie");
        String contentType = ksProfileMap.get("contentType");
        String myLikePythonPath = ksProfileMap.get("myLikePythonPath");
        String shortVideoMyLikePayload = null;
        if (StringUtils.isBlank(pcursor)) {
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
        }
        String headers = "{'User-Agent': " + "\'" + userAgent + "\'" + ",'Cookie':" + "\'" + _cookie + "\'" + ",'Content-Type':" + "\'" + contentType + "\'}";
        String s = KsCrawlerKit(headers, shortVideoMyLikePayload, originalPostUrl, myLikePythonPath);

        if (StringUtils.isNotBlank(s)) {
            if (s.contains("feeds")) {
                JSONObject jsonObject = JSONObject.parseObject(s);
                try {
                    pcursor = jsonObject.getJSONObject("data").getJSONObject("visionProfileLikePhotoList").getString("pcursor");
                } catch (NullPointerException e) {
                    logger.error("出错了。请查看获取的值是否是想要获取的值：{}", jsonObject);
                    e.getStackTrace();
                }
                //json对象转Map
                Map<String, Object> map = (Map<String, Object>) jsonObject;
                return Result.ok().data(map);
            } else {
                logger.error("读取列表失败:{}", s);
                return Result.error().message("帐号异常，请重新登录");
            }
        }
        logger.error("返回数据是null才会打印这句话，请查找原因s:{}", s);
        return Result.error().message("帐号异常，请重新登录");
    }

    public String myInfoCrawler() {
        KuaishouLiveKit kslk = new KuaishouLiveKit();
        HashMap<String, String> ksProfileMap = kslk.readProperties(ksProfilePath);
        String userAgent = ksProfileMap.get("userAgent");
        String originalPostUrl = ksProfileMap.get("originalPostUrl");
        String shortVideoCookie = ksProfileMap.get("shortVideoCookie");
        String contentType = ksProfileMap.get("contentType");
        String myLikePythonPath = ksProfileMap.get("myLikePythonPath");
        String MyInfoPayload = "{'operationName':'userInfoQuery','variables':{},'query':'query userInfoQuery {  visionOwnerInfo {    id    name    avatar    eid    userId    __typename  }}'}";

        if (StringUtils.isNotBlank(shortVideoCookie)) {
            String headers = "{'User-Agent': " + "\'" + userAgent + "\'" + ",'Cookie':" + "\'" + shortVideoCookie + "\'" + ",'Content-Type':" + "\'" + contentType + "\'}";
            String s = kslk.KsCrawlerKit(headers, MyInfoPayload, originalPostUrl, myLikePythonPath);
            return s;
        } else {
            logger.warn("配置文件里没有shortVideoCookie的值，请重新输入shortVideoCookie");
            return "配置文件里没有短视频Cookie的值，请重新输入短视频Cookie.";
        }
    }

    public String myInfoCrawlerByEId(String eid) {
        KuaishouLiveKit kslk = new KuaishouLiveKit();
        HashMap<String, String> ksProfileMap = kslk.readProperties(ksProfilePath);
        String userAgent = ksProfileMap.get("userAgent");
        String originalPostUrl = ksProfileMap.get("originalPostUrl");
        String shortVideoCookie = ksProfileMap.get("shortVideoCookie");
        String contentType = ksProfileMap.get("contentType");
        String myLikePythonPath = ksProfileMap.get("myLikePythonPath");
        String MyInfoByUserIdPayload = "{'operationName':'visionProfile','variables':{'userId':'" + eid + "'},'query':'query visionProfile($userId: String) {  visionProfile(userId: $userId) {    result    hostName    userProfile {      ownerCount {        fan        photo        follow        photo_public        __typename      }      profile {        gender        user_name        user_id        headurl        user_text        user_profile_bg_url        __typename      }      isFollowing      __typename    }    __typename  }}'}";

        if (StringUtils.isNotBlank(shortVideoCookie)) {
            String headers = "{'User-Agent': " + "\'" + userAgent + "\'" + ",'Cookie':" + "\'" + shortVideoCookie + "\'" + ",'Content-Type':" + "\'" + contentType + "\'}";
            String s = kslk.KsCrawlerKit(headers, MyInfoByUserIdPayload, originalPostUrl, myLikePythonPath);
            return s;
        } else {
            logger.warn("配置文件里没有shortVideoCookie的值，请重新输入shortVideoCookie");
            return "配置文件里没有短视频Cookie的值，请重新输入短视频Cookie.";
        }
    }

    public String myFavoriteCrawlerByAnchorId(String anchorId) {
        KuaishouLiveKit kslk = new KuaishouLiveKit();
        HashMap<String, String> ksProfileMap = kslk.readProperties(ksProfilePath);
        String userAgent = ksProfileMap.get("userAgent");
        String originalPostUrl = ksProfileMap.get("originalPostUrl");
        String shortVideoCookie = ksProfileMap.get("shortVideoCookie");
        String contentType = ksProfileMap.get("contentType");
        String myLikePythonPath = ksProfileMap.get("myLikePythonPath");
        String getAnchorinfoByAnchorId = "{'operationName':'visionProfile','variables':{'userId':'" + anchorId + "'},'query':'query visionProfile($userId: String) {  visionProfile(userId: $userId) {    result    hostName    userProfile {      ownerCount {        fan        photo        follow        photo_public        __typename      }      profile {        gender        user_name        user_id        headurl        user_text        user_profile_bg_url        __typename      }      isFollowing      __typename    }    __typename  }}'}";

        if (StringUtils.isNotBlank(shortVideoCookie)) {
            String headers = "{'User-Agent': " + "\'" + userAgent + "\'" + ",'Cookie':" + "\'" + shortVideoCookie + "\'" + ",'Content-Type':" + "\'" + contentType + "\'}";
            String s = kslk.KsCrawlerKit(headers, getAnchorinfoByAnchorId, originalPostUrl, myLikePythonPath);
            return s;
        } else {
            logger.warn("配置文件里没有shortVideoCookie的值，请重新输入shortVideoCookie");
            return "配置文件里没有短视频Cookie的值，请重新输入短视频Cookie.";
        }
    }

    public List myFavoriteCrawlerList(String pcursor) {
        KuaishouLiveKit kslk = new KuaishouLiveKit();
        HashMap<String, String> ksProfileMap = kslk.readProperties(ksProfilePath);
        String userAgent = ksProfileMap.get("userAgent");
        String originalPostUrl = ksProfileMap.get("originalPostUrl");
        String shortVideoCookie = ksProfileMap.get("shortVideoCookie");
        String contentType = ksProfileMap.get("contentType");
        String myLikePythonPath = ksProfileMap.get("myLikePythonPath");

        if (StringUtils.isNotBlank(shortVideoCookie)) {
            String headers = "{'User-Agent': " + "\'" + userAgent + "\'" + ",'Cookie':" + "\'" + shortVideoCookie + "\'" + ",'Content-Type':" + "\'" + contentType + "\'}";
            String myFavoriteList = null;
            if (pcursor == null || "".equals(pcursor)) {
                myFavoriteList = "{'operationName':'visionProfileUserList','variables':{'ftype':1},'query':'query visionProfileUserList($pcursor: String, $ftype: Int) {  visionProfileUserList(pcursor: $pcursor, ftype: $ftype) {    result    fols {      user_name      headurl      user_text      isFollowing      user_id      __typename    }    hostName    pcursor    __typename  }}'}";
            } else {
                myFavoriteList = "{'operationName':'visionProfileUserList','variables':{'ftype':1,'pcursor':" + pcursor + "},'query':'query visionProfileUserList($pcursor: String, $ftype: Int) {  visionProfileUserList(pcursor: $pcursor, ftype: $ftype) {    result    fols {      user_name      headurl      user_text      isFollowing      user_id      __typename    }    hostName    pcursor    __typename  }}'}";
            }
            String s = kslk.KsCrawlerKit(headers, myFavoriteList, originalPostUrl, myLikePythonPath);
            if (!s.contains("error")) {
                JSONObject jsonObject = JSONObject.parseObject(s).getJSONObject("data").getJSONObject("visionProfileUserList");
                JSONArray fols = jsonObject.getJSONArray("fols");
                return fols;
            } else {
                logger.error("配置文件里没有shortVideoCookie的值，请重新输入shortVideoCookie");
                return new ArrayList();
            }
        } else {
            logger.error("配置文件里没有shortVideoCookie的值，请重新输入shortVideoCookie");
            return new ArrayList();
        }
    }

    /**
     * 关注或者取消关注 直接作用到快手账号
     * # 关注  ftype：1
     * # 取消关注  ftype：2
     */
    public Result focusInOrNot(String anchorEid,String myEid, int isFocusIn) {
        KuaishouLiveKit kslk = new KuaishouLiveKit();
        HashMap<String, String> ksProfileMap = kslk.readProperties(ksProfilePath);
        String userAgent = ksProfileMap.get("userAgent");
        String focusInUrl = ksProfileMap.get("focusInUrl");
        String focusInCookie = ksProfileMap.get("focusInCookie");
        String contentType = ksProfileMap.get("contentType");
        String myLikePythonPath = ksProfileMap.get("myLikePythonPath");
        String referer = "https://www.kuaishou.com/profile/" + myEid;
        String focusInPayload = "{'operationName' : 'visionFollow', 'variables': {'touid': \'" + anchorEid + "\', 'ftype': " + isFocusIn + ", 'followSource': 1}, 'query': 'mutation visionFollow($touid: String, $ftype: Int, $followSource: Int, $expTag: String) {  visionFollow(touid: $touid, ftype: $ftype, followSource: $followSource, expTag: $expTag) {   result   followStatus   hostName    error_msg    __typename  }}'}";

        String headers = "{'User-Agent': " + "\'" + userAgent + "\'" + ",'Cookie':" + "\'" + focusInCookie + "\'" + ",'Content-Type':" + "\'" + contentType + "\'" + ",'Referer':" + "\'" + referer + "\'}";
        String s = kslk.KsCrawlerKit(headers, focusInPayload, focusInUrl, myLikePythonPath);

        if (StringUtils.isNotBlank(s)){
            JSONObject jsonObject = JSON.parseObject(s);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONObject visionFollow = data.getJSONObject("visionFollow");
            try {
                int result = visionFollow.getIntValue("result");
                int followStatus = visionFollow.getIntValue("followStatus");
                if (result == 1){
                    if (followStatus == 1) {
                        logger.info("关注主播,操作成功！");
                        return Result.ok().message("关注主播,操作成功！");
                    } else {
                        logger.info("取消关注主播,操作成功！");
                        return Result.ok().message("取消关注主播,操作成功！");
                    }
                }else {
                    logger.error("关注或者取消关注，操作失败！");
                    return Result.error().message("关注或者取消关注，操作失败！");
                }
            }catch (NullPointerException e){
                logger.error("关注或者取消关注操作失败。原因：关注的Cookie不正确，提示：关注的Cookie和短视频Cookie不一样，需要单独提取");
                return Result.error().message("关注或者取消关注操作失败。原因：关注的Cookie不正确，提示：关注的Cookie和短视频Cookie不一样，需要单独提取");
            }

        }else {
            logger.error("返回数据格式不正确！");
            return Result.error().message("返回数据格式不正确！");
        }
    }
}

