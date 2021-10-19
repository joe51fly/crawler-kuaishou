package com.joe.kuaishou.tools;

import com.joe.kuaishou.bean.MyInfo;
import com.joe.kuaishou.service.MyInfoService;
import com.joe.kuaishou.service.MyfavoriteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InsertMyfavoriteListTask {
    private static final Logger logger = LoggerFactory.getLogger(InsertMyfavoriteListTask.class);

    @Autowired
    MyInfoService myInfoService;
    @Autowired
    MyfavoriteService myfavoriteService;
    //每天0点执行一次
    @Scheduled(cron = "0 0 0 */1 * ?")
    public void testScheduled(){
        logger.info("--------------------------------开始定时任务-InsertMyfavoriteListTask--------------------------------------------");
        int myFollowCount = 0;
        //先去更新我的信息，获取我最新的关注总数，赋值给
        boolean b = myInfoService.insertOrUpdateMyInfo();
        if (b) {
            logger.info("更新用户数据成功");
        }else {
            logger.error("更新用户数据失败");
        }
        MyInfo myInfo = myInfoService.getMyInfo();
        if (myInfo != null){
            myFollowCount = myInfo.getMyFollow();
            logger.info("查询自己信息成功：{}",myInfo);
        }else {
            logger.error("查询自己信息失败：{}",myInfo);
        }
        Integer insertMyfavoriteCount = myfavoriteService.insertMyfavoriteByList(myFollowCount);
        if (insertMyfavoriteCount > 0) {
            logger.info("更新数据成功，一共插入：{}条数据",insertMyfavoriteCount);
        } else {
            logger.warn("更新数据失败");
        }
        logger.info("--------------------------------定时任务结束-InsertMyfavoriteListTask-----------------------------------------");
    }
}
