package com.joe.kuaishou.tools.thread;

import com.joe.kuaishou.common.Result;
import com.joe.kuaishou.config.BeanContext;
import com.joe.kuaishou.service.MyfavoriteLiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 刷新页面后另起一个线程来执行 正在直播主播信息入库 的操作
 * @author: joe
 * @createTime: 2021-09-24 20:18
 **/
public class InsertMyfavoriteLiveInfoRunnable implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(InsertMyfavoriteLiveInfoRunnable.class);

    private MyfavoriteLiveService myfavoriteLiveService;

    @Override
    public void run() {
        this.myfavoriteLiveService = BeanContext.getApplicationContext().getBean(MyfavoriteLiveService.class);
        Result result = this.myfavoriteLiveService.insertMyfavoriteLiveInfoByList(false);
        if (result.getSuccess()) {
            logger.info(result.ok().getMessage());
        } else {
            logger.info(result.error().getMessage());
        }
    }
}
