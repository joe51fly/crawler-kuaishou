package com.joe.kuaishou.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joe.kuaishou.bean.AnchorInfo;
import com.joe.kuaishou.bean.AnchorLiveInfo;
import com.joe.kuaishou.common.Result;
import com.joe.kuaishou.mapper.AnchorInfoMapper;
import com.joe.kuaishou.mapper.AnchorLiveInfoMapper;
import com.joe.kuaishou.service.AnchorLiveInfoService;
import com.joe.kuaishou.tools.KuaishouLiveKit;
import com.joe.kuaishou.tools.NowDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.*;

@Service
public class AnchorLiveInfoServiceImpl extends ServiceImpl<AnchorLiveInfoMapper, AnchorLiveInfo> implements AnchorLiveInfoService {
    private static final Logger logger = LoggerFactory.getLogger(AnchorLiveInfoServiceImpl.class);

    private static final boolean isLive = true;

    @Autowired
    AnchorLiveInfoMapper anchorLiveInfoMapper;

    @Autowired
    AnchorInfoMapper anchorInfoMapper;


    @Override
    public AnchorLiveInfo getMyfavoriteLiveInfoByEid(String userEid) {
        return anchorLiveInfoMapper.getMyfavoriteLiveInfoByEid(userEid);
    }

    @Override
    public List<AnchorLiveInfo> getAll() {
        return anchorLiveInfoMapper.getAll();
    }

    @Override
    public List<AnchorLiveInfo> getMyfavoriteLiveInfoByIsMyfavorite(boolean isMyfavorite) {
        return anchorLiveInfoMapper.getMyfavoriteLiveInfoByIsMyfavorite(isMyfavorite);
    }

    @Override
    public boolean insertMyfavoriteLiveInfo(AnchorLiveInfo anchorLiveInfo) {
        return anchorLiveInfoMapper.insertMyfavoriteLiveInfo(anchorLiveInfo);
    }

    /**
     * list批量插入-我关注的正在直播的主播的信息
     *
     * @return
     */
    @Transactional
    @Override
    public Result insertMyfavoriteLiveInfoByList(boolean isTemp) {
        // 开始时间
        long start = System.currentTimeMillis();
        boolean isSucceed = false;
        KuaishouLiveKit kslk = new KuaishouLiveKit();
        Result result = kslk.myfavoriteLiveAllData();

        // 每20条数据开启一条线程
        final int threadSize = 20;

        if (result.getSuccess()) {
            JSONArray follow = new JSONObject(result.getData()).getJSONArray("follow");
            List<Object> originalList = follow.toJavaList(Object.class);
            //看看数据库里有没有数据
            int dbSize = getAll().size();

            int dataListSize = originalList.size();
            // 线程数
            int threadNum = dataListSize / threadSize + 1;
            // 定义标记,过滤threadNum为整数
            boolean special = dataListSize % threadSize == 0;
            final int[] m = {0};
            // 创建一个线程池
            ExecutorService exec = Executors.newFixedThreadPool(threadNum);
            // 定义一个任务集合
            List<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
            Callable<Integer> task = null;
            List<Object> cutList = null;

            // 确定每条线程的数据
            for (int l = 0; l < threadNum; l++) {

                if (l == threadNum - 1) {
                    if (special) {
                        break;
                    }
                    cutList = originalList.subList(threadSize * l, dataListSize);
                } else {
                    cutList = originalList.subList(threadSize * l, threadSize * (l + 1));
                }
//                logger.info("第" + (l + 1) + "组：" + cutList.toString());
                final List<Object> listSub = cutList;

                task = new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        logger.info(Thread.currentThread().getName() + "线程：" + listSub);

                        List<AnchorLiveInfo> listForInsert = new ArrayList<AnchorLiveInfo>();

                        Iterator<Object> iterator = listSub.iterator();
                        while (iterator.hasNext()) {
                            Map next = (Map) iterator.next();
                            AnchorLiveInfo anchorLiveInfo = new AnchorLiveInfo();
                            try {
                                String rtCoverUrl = (String) next.get("rtCoverUrl");
                                String hlsPlayUrl = (String) next.get("hlsPlayUrl");
//                                String coverUrl = (String) next.get("coverUrl");
//                                String liveStreamId = (String) next.get("liveStreamId");
//                                String startTime = (String) next.get("startTime");
                                ArrayList playUrls = JSONObject.parseObject(String.valueOf(next.get("playUrls")), ArrayList.class);
                                String o = String.valueOf(playUrls.get(0));
                                String flvUrl = JSONObject.parseObject(o).getString("url");
                                JSONObject user = JSONObject.parseObject(String.valueOf(next.get("user")));
                                String eid = user.getString("eid");
                                /*String user_name = user.getString("user_name");
                                String headurl = user.getString("headurl");
                                String principalId = user.getString("principalId");
                                String user_id = user.getString("user_id");
                                String user_text = user.getString("user_text");*/


                                //anchorLiveInfo.setCoverUrl(coverUrl);
                                anchorLiveInfo.setHlsPlayUrl(hlsPlayUrl);
                                //是否正在直播
//                                anchorLiveInfo.setLive(isLive);
//                                anchorLiveInfo.setLiveStartTime(new Date(Long.parseLong(startTime)));
                                anchorLiveInfo.setPlayUrls(flvUrl);
                                anchorLiveInfo.setRtCoverUrl(rtCoverUrl);
                                anchorLiveInfo.setUpdateTime(new Date());
                                anchorLiveInfo.setUserEid(eid);
/*                                anchorLiveInfo.setUserHeadUrl(headurl);
                                anchorLiveInfo.setUserId(user_id);
                                anchorLiveInfo.setUserLiveStreamId(liveStreamId);
                                anchorLiveInfo.setUserName(user_name);
                                anchorLiveInfo.setUserPrincipalId(principalId);
                                anchorLiveInfo.setUserText(user_text);*/
                            } catch (Exception e) {
                                logger.error("解析json出错，请检查json数据是否是真的", e);
                            }
                            listForInsert.add(anchorLiveInfo);
                        }
                        if (listForInsert.size() == 0) {
                            logger.error("配置文件里没有shortVideoCookie的值，请重新输入shortVideoCookie:{}", listForInsert);
                            return 0;
                        } else {
                            int insertMyfavoriteCount = 0;
                            //入库 最后返回执行完list的总数
                            //这里 insertMyfavoriteCount 为受影响的的行数。实际的插入数量值应该是 insertMyfavoriteCount/2
                            if (isTemp) {
                                insertMyfavoriteCount = anchorLiveInfoMapper.insertMyfavoriteLiveInfoByListForTemp(listForInsert);
                                if (insertMyfavoriteCount > 0) {
                                    //第一次插入的时候不用 除以2
                                    if (dbSize > 0) {
                                        m[0] = m[0] + insertMyfavoriteCount;
                                        logger.info("insertMyfavoriteByList成功插入：{}条数据", insertMyfavoriteCount);
                                    } else {
                                        m[0] = m[0] + insertMyfavoriteCount;
                                        logger.info("insertMyfavoriteByList成功插入：{}条数据", insertMyfavoriteCount);
                                    }
                                    logger.info("已经插入：{}条数据", m[0]);
                                } else {
                                    logger.error("insertMyfavoriteByList插入数据失败：{}条", insertMyfavoriteCount);
                                }
                            } else {
                                insertMyfavoriteCount = anchorLiveInfoMapper.insertMyfavoriteLiveInfoByList(listForInsert);
                                if (insertMyfavoriteCount > 0) {
                                    //第一次插入的时候不用 除以2
                                    if (dbSize > 0) {
                                        m[0] = m[0] + insertMyfavoriteCount / 2;
                                        logger.info("insertMyfavoriteByList成功插入：{}条数据", insertMyfavoriteCount / 2);
                                    } else {
                                        m[0] = m[0] + insertMyfavoriteCount;
                                        logger.info("insertMyfavoriteByList成功插入：{}条数据", insertMyfavoriteCount);
                                    }
                                    logger.info("已经插入：{}条数据", m[0]);
                                } else {
                                    logger.error("insertMyfavoriteByList插入数据失败：{}条", insertMyfavoriteCount);
                                }
                            }
                        }
                        return 1;
                    }
                };
                // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
                tasks.add(task);
            }
            List<Future<Integer>> results = null;
            try {
                //设置超时时间 5s
//                results = exec.invokeAll(tasks, 100, TimeUnit.SECONDS);
                results = exec.invokeAll(tasks);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (results != null) {
                //关闭线程池 发现shutdown()执行后线程未关闭，故使用shutdownNow()
                exec.shutdown();
//                exec.shutdownNow();
                logger.info("线程任务执行结束");
                logger.info("执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");
                return Result.ok().message("批量插入 anchorLiveInfo 成功");
            } else {
                logger.error("出错了");
                return Result.error().message("批量插入 anchorLiveInfo 失败");
            }
        } else {
            return result;
        }
    }

    @Override
    public boolean updateMyfavoriteLiveInfoByEid(AnchorLiveInfo anchorLiveInfo) {
        return anchorLiveInfoMapper.updateMyfavoriteLiveInfoByEid(anchorLiveInfo);
    }

    @Override
    public boolean updateIsMyfavoriteLiveInfoByIsMyfavorite(AnchorLiveInfo anchorLiveInfo) {
        return anchorLiveInfoMapper.updateIsMyfavoriteLiveInfoByIsMyfavorite(anchorLiveInfo);
    }

    @Override
    public boolean deleteMyfavoriteLiveInfoByEid(String userEid) {
        return anchorLiveInfoMapper.deleteMyfavoriteLiveInfoByEid(userEid);
    }

    @Override
    public List<AnchorLiveInfo> getMySpecialFollowInfo() {
        return anchorLiveInfoMapper.getMySpecialFollowInfo();
    }

    @Override
    public Result getNewMySpecialFollowInfo() {
        //插入数据之前先看临时表有没有上次存留的数据 如果有的话清除数据
        List<AnchorLiveInfo> allFromTemp = getAllFromTemp();
        if (allFromTemp.size() > 0) {
            truncateTemp();
            logger.info("清除临时表数据操作完成");
        }
        Result result = insertMyfavoriteLiveInfoByList(true);
        if (result.getSuccess()) {
            logger.info(result.getMessage());
            List<AnchorLiveInfo> myNewSpecialFollowInfo = getMySpecialFollowInfo();
            Map<String, Object> map = new HashMap<>();
            map.put("result", myNewSpecialFollowInfo);
            logger.info("查询正在直播的特别关注的主播信息成功");
            return Result.ok().data(map).message("查询正在直播的特别关注的主播信息成功");
        } else {
            logger.info(result.getMessage());
            return Result.error().message(result.getMessage());
        }
    }

    @Override
    public Result getNotIsMySpecialFollowInfo() {
        Result result = insertMyfavoriteLiveInfoByList(false);
        if (result.getSuccess()) {
            logger.info(result.getMessage());
            List<AnchorLiveInfo> notIsMySpecialFollowInfo = anchorLiveInfoMapper.getNotIsMySpecialFollowInfo();
            Map<String, Object> map = new HashMap<>();
            map.put("result", notIsMySpecialFollowInfo);
            logger.info("成功获取不是特别关注的正在直播的主播列表");
            return Result.ok().data(map).message("成功获取不是特别关注正在直播的主播列表");
        } else {
            logger.info(result.getMessage());
            return Result.error().message(result.getMessage());
        }
    }

    @Override
    public List<AnchorLiveInfo> getAllFromTemp() {
        return anchorLiveInfoMapper.getAllFromTemp();
    }

    @Override
    public void truncateTemp() {
        anchorLiveInfoMapper.truncateTemp();
    }

    @Override
    public Result updateForTheTopByIsTop(String userEid, boolean isSetTop) {
        AnchorInfo anchorInfoByEid = anchorInfoMapper.getMyfavoriteByAnchorId(userEid);
        String userName = anchorInfoByEid.getAnchorName();
        int top = anchorInfoByEid.getIsTop();
        if (isSetTop) {
            if (top < 8) {
                top += 1;
            }
        } else {
            if (top > 0) {
                top -= 1;
            } else {
                top = 0;
            }
        }
        AnchorLiveInfo anchorLiveInfo = new AnchorLiveInfo();
        anchorLiveInfo.setUserEid(userEid);
        anchorLiveInfo.setIsTop(top);
        anchorLiveInfo.setUpdateTime(NowDateUtils.getDaDate());
        boolean b = anchorLiveInfoMapper.updateForTheTopByIsTop(anchorLiveInfo);
        Map<String, Object> map = new HashMap<>();
        map.put("userName", userName);
        if (b) {
            logger.info(userName+" 置顶或者取消置顶成功！");
            return Result.ok().data(map).message("置顶或者取消置顶成功！");
        } else {
            logger.error(userName+" 置顶或者取消置顶失败！");
            return Result.error().message("置顶或者取消置顶失败！");
        }
    }

    @Override
    public Result updateSuperSet_top(String userEid, boolean isSetSuper_top) {
        AnchorInfo anchorInfoByEid = anchorInfoMapper.getMyfavoriteByAnchorId(userEid);
        String userName = anchorInfoByEid.getAnchorName();
        int top = anchorInfoByEid.getIsTop();
        if (isSetSuper_top) {
            top = 9;
        } else {
            top = 0;
        }
        AnchorLiveInfo anchorLiveInfo = new AnchorLiveInfo();
        anchorLiveInfo.setUserEid(userEid);
        anchorLiveInfo.setIsTop(top);
        anchorLiveInfo.setUpdateTime(NowDateUtils.getDaDate());
        Map<String, Object> map = new HashMap<>();
        map.put("userName", userName);
        boolean b = anchorLiveInfoMapper.updateSuperSet_top(anchorLiveInfo);
        if (b) {
            logger.info("设置或者取消 " + userName +" 超级置顶 成功！");
            return Result.ok().data(map).message("设置或者取消 超级置顶 成功！");
        } else {
            logger.error("设置或者取消 " + userName + " 超级置顶 失败！");
            return Result.error().message("设置或者取消 超级置顶 失败！");
        }
    }
}
