package com.joe.kuaishou.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joe.kuaishou.bean.AnchorLiveInfo;
import com.joe.kuaishou.common.Result;

import java.util.List;

public interface AnchorLiveInfoService extends IService<AnchorLiveInfo> {
    /*
        根据用户userEid查询一条数据
     */
    AnchorLiveInfo getMyfavoriteLiveInfoByEid(String userEid);

    /*
        查询所有数据
     */
    List<AnchorLiveInfo> getAll();

    /*
    通过isMyfavorite的状态查询我特别关注的主播
     */
    List<AnchorLiveInfo> getMyfavoriteLiveInfoByIsMyfavorite(boolean isMyfavorite);

    /*
        插入一条数据
     */
    boolean insertMyfavoriteLiveInfo(AnchorLiveInfo anchorLiveInfo);

    /*
        批量插入List<AnchorLiveInfo>
     */
    Result insertMyfavoriteLiveInfoByList(boolean isTemp);


    /*
       根据Eid更新数据
    */
    boolean updateMyfavoriteLiveInfoByEid(AnchorLiveInfo anchorLiveInfo);

    /*
    更新主播状态为特别关注或者取消特别关注
     */
    boolean updateIsMyfavoriteLiveInfoByIsMyfavorite(AnchorLiveInfo anchorLiveInfo);

    /*
        根据anchorId删除数据
     */
    boolean deleteMyfavoriteLiveInfoByEid(String userEid);



    List<AnchorLiveInfo> getMySpecialFollowInfo();

    /*
        获取新插入的正在直播的特别关注的主播信息
     */
    Result getNewMySpecialFollowInfo();

    /**
     * 获取不是特别关注的正在直播的主播列表
     * @return
     */
    Result getNotIsMySpecialFollowInfo();

    List<AnchorLiveInfo> getAllFromTemp();
    /**
     * 清空临时表的数据
     * @return
     */
    void truncateTemp();

    /**
     * 设置为置顶 或者取消置顶
     * @param userEid
     * @param isSetTop
     * @return
     */
    Result updateForTheTopByIsTop(String userEid,boolean isSetTop);

    /**
     * 设置或者取消 超级置顶
     * @param userEid
     * @param isSetSuper_top
     * @return
     */
    Result updateSuperSet_top(String userEid,boolean isSetSuper_top);
}
