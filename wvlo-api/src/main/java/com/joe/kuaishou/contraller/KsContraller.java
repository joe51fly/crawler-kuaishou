package com.joe.kuaishou.contraller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joe.kuaishou.common.Result;
import com.joe.kuaishou.service.MyInfoService;
import com.joe.kuaishou.service.MyfavoriteLiveService;
import com.joe.kuaishou.service.MyfavoriteService;
import com.joe.kuaishou.tools.thread.InsertMyfavoriteLiveInfoRunnable;
import com.joe.kuaishou.tools.KuaishouLiveKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@CrossOrigin
@RequestMapping(value = "/ks", method = RequestMethod.POST)
@ResponseBody
public class KsContraller {
    private static final Logger logger = LoggerFactory.getLogger(KsContraller.class);
    private static final String ksProfilePath = "ksProfile.properties";

    @Autowired
    MyfavoriteService myfavoriteService;
    @Autowired
    MyInfoService myInfoService;
    @Autowired
    MyfavoriteLiveService myfavoriteLiveService;

    /**
     * @apiDescription 获取我关注的主播正在直播的数据列表
     * @api {POST} /data
     * @apiGroup KsContraller
     * @apiHeader {String} callback=a
     * @apiParam {String} callback 随便什么字符都行
     * @apiSuccessExample {json} 成功响应:
     * {
     *   "success": true,
     *   "code": 20000,
     *   "message": "成功",
     *   "data": {
     *     "result": 1,
     *     "host-name": "bjrz-rs2123.lf",
     *     "follow": [
     *       {
     *         "watchingCount": "0",
     *         "rtCoverUrl": "https://live2.static.yximgs.com/live/game/screenshot/4SLhBtIwqZ4~1626772614861~1",
     *         "playUrls": [
     *           {
     *             "freeTrafficCdn": false,
     *             "pushCdn": "OriginMainApp",
     *             "cdnIp": null,
     *             "ipValidTime": null,
     *             "cdn": "Tencent",
     *             "url": "https://tx-origin.pull.yximgs.com/gifshow/4SLhBtIwqZ4_ma1500.flv?txSecret=ee05382b9a0661abc7bf35f5135f45e8&txTime=60f7e651&stat=Sp2EG8%2BUDQbBDUxqvLx2RhV8FDJW%2FMfKRjTQMuQ6S9sVOT5LXOWzkXPLMCABg7eR&tsc=origin&oidc=txhb"
     *           }
     *         ],
     *         "hlsPlayUrl": "https://bd-origin.hlspull.yximgs.com/gifshow/4SLhBtIwqZ4_ma1500.m3u8?wsTime=1626859089&wsSecret=f2f98ded35b462e515999c290671b155&tsc=origin&oidc=txhbtsc=origin",
     *         "gameInfo": {
     *           "gameId": 0,
     *           "coverUrl": "",
     *           "name": "其他",
     *           "type": 0,
     *           "category": "QT"
     *         },
     *         "caption": "等你来",
     *         "likeCount": "0",
     *         "exp_tag": "1_v/0_unknown0",
     *         "privateLive": false,
     *         "coverUrl": "https://tx2.a.yximgs.com/uhead/AB/2021/07/20/17/BMjAyMTA3MjAxNzEzMjRfOTMyMzY5NTAwXzgzMzU0NjQwODVfbHY=.jpg",
     *         "coverHeight": 1920,
     *         "liveWish": true,
     *         "serverExpTag": "feed_live|4SLhBtIwqZ4|932369500|1_v/0_unknown0",
     *         "ext_params": {
     *           "color": "201C19",
     *           "w": 1080,
     *           "h": 1920
     *         },
     *         "liveStreamId": "4SLhBtIwqZ4",
     *         "coverWidth": 1080,
     *         "startTime": 1626772402219,
     *         "revenueRankWinnerIcon": [],
     *         "user": {
     *           "eid": "3x59tkbj2kecvgi",
     *           "user_name": "🕊紫～霞🕊💕",
     *           "verified": false,
     *           "headurl": "https://tx2.a.yximgs.com/uhead/AB/2021/04/14/09/BMjAyMTA0MTQwOTAzNDdfOTMyMzY5NTAwXzFfaGQ2MjVfNjky_s.jpg",
     *           "headurls": [
     *             {
     *               "cdn": "tx2.a.yximgs.com",
     *               "url": "https://tx2.a.yximgs.com/uhead/AB/2021/04/14/09/BMjAyMTA0MTQwOTAzNDdfOTMyMzY5NTAwXzFfaGQ2MjVfNjky_s.jpg"
     *             },
     *             {
     *               "cdn": "ali2.a.yximgs.com",
     *               "url": "https://ali2.a.yximgs.com/uhead/AB/2021/04/14/09/BMjAyMTA0MTQwOTAzNDdfOTMyMzY5NTAwXzFfaGQ2MjVfNjky_s.jpg"
     *             }
     *           ],
     *           "principalId": "3x59tkbj2kecvgi",
     *           "isFavorited": false,
     *           "visitorBeFollowed": false,
     *           "user_sex": "F",
     *           "user_id": 932369500,
     *           "following": true,
     *           "user_text": "感谢快手官方提供平台❤️\n\n感谢大家关注与支持🍻🍻🍻\n\n大号M894562008:谢谢大家关注🙏🙏🙏",
     *           "live": false
     *         },
     *         "landscape": false
     *       }
     *     ]
     *   }
     * }
     * @apiVersion 1.0.0
     */
    @PostMapping(value = "/data")
    public Result ksData(@RequestParam(name = "callback") String callback) {
        KuaishouLiveKit kslk = new KuaishouLiveKit();
        Result result = kslk.myfavoriteLiveAllData();
        if (result.getSuccess()) {
//            Thread thread = new Thread(new InsertMyfavoriteLiveInfoRunnable(),"InsertMyfavoriteLive");
//            thread.start();
            return Result.ok().data(result.getData());
        } else {
            return Result.error().message(result.getMessage());
        }
    }

    /**
     * @apiDescription 获取我点过赞的短视频
     * @api {POST} /myLikeData
     * @apiGroup KsContraller
     * @apiHeader {String} callback=a
     * @apiHeader {String} pcursor=0,30,60,90
     * @apiParam {String} callback 随便什么字符都行
     * @apiParam {String} pcursor 数据的页数 例子：0,30,60,90
     * @apiParamExample {json} 请求示例:
     * {
     * }
     * @apiSuccessExample {json} 成功响应:
     * {
     * "success": true,
     * "code": 20000,
     * "message": "成功",
     * "data": {
     * "data": {
     * "visionProfileLikePhotoList": {
     * "result": 1,
     * "hostName": null,
     * "webPageArea": "profilexxnull",
     * "__typename": "VisionProfilePhotoList",
     * "feeds": [
     * {
     * "currentPcursor": "",
     * "author": {
     * "following": true,
     * "__typename": "Author",
     * "name": "单身荟舞",
     * "id": "3xa7ieagzqqfbce",
     * "headerUrl": "https://ali2.a.yximgs.com/uhead/AB/2021/05/05/20/BMjAyMTA1MDUyMDM4MTJfMTQxNzgxOTA0NV8yX2hkMTU3XzcwMg==_s.jpg",
     * "headerUrls": null
     * },
     * "__typename": "Feed",
     * "photo": {
     * "videoRatio": 0.5625,
     * "photoUrls": [
     * {
     * "__typename": "Url",
     * "cdn": "v2.kwaicdn.com",
     * "url": "https://v2.kwaicdn.com/upic/2021/07/20/15/BMjAyMTA3MjAxNTM4NDNfMTQxNzgxOTA0NV81MzYxMjUzOTAwOV8yXzM=_b_Bf718b2eb94ade6dffd20c8c331138c6e.mp4?pkey=AAUS4fzoLNv0hWpByZu2S2UcC6pSpY-C9-n5oniAW6YJAulNP5Gcu9GRxA8A77Gz_LSZFeBZYzfDI2rzEjdq88944KZYonae0IFnDhYZ-KidtxGOSEf0-24B9uyJGLC2CuM&tag=1-1626772982-unknown-0-4vy8qicugy-645f6c1fb0c004ea&clientCacheKey=3xm3cui2nmctgmc_b.mp4&tt=b&di=de5ba63c&bp=14764"
     * },
     * {
     * "__typename": "Url",
     * "cdn": "v3.kwaicdn.com",
     * "url": "https://v3.kwaicdn.com/upic/2021/07/20/15/BMjAyMTA3MjAxNTM4NDNfMTQxNzgxOTA0NV81MzYxMjUzOTAwOV8yXzM=_b_Bf718b2eb94ade6dffd20c8c331138c6e.mp4?pkey=AAXBhN-7cV_7eQ9lJd_t4D73avLXvGToCVe8-2q8ZcGd67ZmUgKL7kcvrrE38n14MN87U1rIrEf-ZECHUJ6z2kNtfM7VTNekB9_YAjTNNQO5FI4QNqxcKci6U9qzPB09Tc8&tag=1-1626772982-unknown-1-lz6qr0djri-eecd4a5d9f2958b0&clientCacheKey=3xm3cui2nmctgmc_b.mp4&tt=b&di=de5ba63c&bp=14764"
     * }
     * ],
     * "__typename": "PhotoEntity",
     * "stereoType": 0,
     * "caption": "#日出东方落于西 朝思暮想念于你 #",
     * "likeCount": "184",
     * "realLikeCount": 184,
     * "animatedCoverUrl": "https://ali2.a.yximgs.com/upic/2021/07/20/15/BMjAyMTA3MjAxNTM4NDNfMTQxNzgxOTA0NV81MzYxMjUzOTAwOV8yXzM=_animatedV5_B0ea1cdff6bc59e9e3494042017eceba3.webp?tag=1-1626772982-unknown-0-mw3ehoe9qs-eaf634b622f5d643&clientCacheKey=3xm3cui2nmctgmc_animatedV5.webp&di=de5ba63c&bp=14764",
     * "liked": true,
     * "duration": 8100,
     * "coverUrl": "https://ali2.a.yximgs.com/upic/2021/07/20/15/BMjAyMTA3MjAxNTM4NDNfMTQxNzgxOTA0NV81MzYxMjUzOTAwOV8yXzM=_Bcba4bc4be92b95c3fd1e9523fb7fdfbc.jpg?tag=1-1626772982-unknown-0-bauf2cwiuf-f1fb5933485ca9f4&clientCacheKey=3xm3cui2nmctgmc.jpg&di=de5ba63c&bp=14764",
     * "photoUrl": "https://v2.kwaicdn.com/upic/2021/07/20/15/BMjAyMTA3MjAxNTM4NDNfMTQxNzgxOTA0NV81MzYxMjUzOTAwOV8yXzM=_b_Bf718b2eb94ade6dffd20c8c331138c6e.mp4?pkey=AAUS4fzoLNv0hWpByZu2S2UcC6pSpY-C9-n5oniAW6YJAulNP5Gcu9GRxA8A77Gz_LSZFeBZYzfDI2rzEjdq88944KZYonae0IFnDhYZ-KidtxGOSEf0-24B9uyJGLC2CuM&tag=1-1626772982-unknown-0-4vy8qicugy-645f6c1fb0c004ea&clientCacheKey=3xm3cui2nmctgmc_b.mp4&tt=b&di=de5ba63c&bp=14764",
     * "expTag": "1_a/2001798988773139361_xpcwebprofilexxnull0",
     * "id": "3xm3cui2nmctgmc",
     * "coverUrls": null,
     * "timestamp": 1626766739545
     * },
     * "llsid": "2001798988773139361",
     * "canAddComment": 0,
     * "type": 1,
     * "tags": [
     * {
     * "__typename": "Tag",
     * "name": "日出东方落于西",
     * "type": 1
     * }
     * ],
     * "status": 1
     * },
     * }
     * }
     * @apiVersion 1.0.0
     */
    @PostMapping(value = "/myLikeData")
    public Result ksMyLikeData(@RequestParam(name = "callback") String callback, @RequestParam(name = "pcursor") String pcursor) {
        KuaishouLiveKit kslk = new KuaishouLiveKit();
        Result result = kslk.ksMyLikeShortVideoData(pcursor);
        if (result.getSuccess()) {
            return Result.ok().data(result.getData());
        } else {
            return Result.error().message(result.getMessage());
        }
    }

    /**
     * @apiDescription 测试数据
     * @api {POST} /test/live-data
     * @apiGroup KsContraller
     * @apiParam {String} callback 随便什么字符都行
     * @apiParamExample {json} 请求示例:
     * {
     * }
     * @apiSuccessExample {json} 成功响应:
     * {
     *  "success": true,
     *  "code": 20000,
     *  "message": "",
     *  "data": {}
     * }
     * @apiVersion 1.0.0
     */
    @PostMapping(value = "/test/live-data")
    public Result testKsData(@RequestParam(name = "callback") String callback) throws JsonProcessingException {
        KuaishouLiveKit kslk = new KuaishouLiveKit();
        HashMap<String, String> ksProfileMap = kslk.readProperties(ksProfilePath);
        String s = ksProfileMap.get("testData");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        Map map = mapper.readValue(s, Map.class);
        return Result.ok().data(map);
    }

    @RequestMapping("index")
    public String index() {
        return "index";
    }

}
