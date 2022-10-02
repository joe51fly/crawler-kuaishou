package com.joe.kuaishou.tools.thread;

import com.joe.kuaishou.common.Result;
import com.joe.kuaishou.config.BeanContext;
import com.joe.kuaishou.service.MyfavoriteLiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * 刷新页面后另起一个线程来执行 正在直播主播信息入临时库 的操作
 * @author: joe
 * @createTime: 2021-09-24 20:18
 **/
public class InsertMyfavoriteLiveTempInfoRunnable implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(InsertMyfavoriteLiveTempInfoRunnable.class);

    private MyfavoriteLiveService myfavoriteLiveService;

    @Override
    public void run() {
        this.myfavoriteLiveService = BeanContext.getApplicationContext().getBean(MyfavoriteLiveService.class);
        try {
            //插入数据之前先看临时表有没有上次存留的数据 如果有的话清除数据
            List<MyfavoriteLiveInfo> allFromTemp = myfavoriteLiveService.getAllFromTemp();
            if (allFromTemp.size() > 0) {
                myfavoriteLiveService.truncateTemp();
                logger.info("清除临时表数据操作完成");
            }
            Result result = this.myfavoriteLiveService.insertMyfavoriteLiveInfoByList(true);
            if (result.getSuccess()) {
                logger.info(result.ok().getMessage());
            } else {
                logger.info(result.error().getMessage());
            }
        }catch (Exception e){
            logger.error("插入直播主播信息临时表 操作失败");
            e.printStackTrace();
        }
    }
}
