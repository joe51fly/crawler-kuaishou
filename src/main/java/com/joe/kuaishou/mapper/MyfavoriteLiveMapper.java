package com.joe.kuaishou.mapper;

import com.joe.kuaishou.bean.MyfavoriteLiveInfo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component//指定这是一个操作数据库的mapper
public interface MyfavoriteLiveMapper {
    /*
        根据用户userEid查询一条数据
     */
    MyfavoriteLiveInfo getMyfavoriteLiveInfoByEid(String userEid);

    /*
        查询所有数据
     */
    List<MyfavoriteLiveInfo> getAll();

    /*
    通过isMyfavorite的状态查询我特别关注的主播
     */
    List<MyfavoriteLiveInfo> getMyfavoriteLiveInfoByIsMyfavorite(boolean isMyfavorite);

    /*
        插入一条数据
     */
    boolean insertMyfavoriteLiveInfo(MyfavoriteLiveInfo myfavoriteLiveInfo);

    /*
        批量插入List<MyfavoriteLiveInfo>
     */
    int insertMyfavoriteLiveInfoByList(List<MyfavoriteLiveInfo> myfavoriteLiveInfoList);

    /*
       根据Eid更新数据
    */
    boolean updateMyfavoriteLiveInfoByEid(MyfavoriteLiveInfo myfavoriteLiveInfo);

    /*
    更新主播状态为特别关注或者取消特别关注
     */
    boolean updateIsMyfavoriteLiveInfoByIsMyfavorite(MyfavoriteLiveInfo myfavoriteLiveInfo);

    /*
        根据anchorId删除数据
     */
    boolean deleteMyfavoriteLiveInfoByEid(String userEid);


    boolean createMyfavoriteLiveInfoTempTable();

    int insertMyfavoriteLiveInfoByListForTemp(List<MyfavoriteLiveInfo> myfavoriteLiveInfo);
    /**
     * 获取isMyfavorite为true的主播列表信息
     * @return
     */
    List<MyfavoriteLiveInfo> getMySpecialFollowInfo();

    /**
     * 获取isMyfavorite为false的主播列表信息
     * @return
     */
    List<MyfavoriteLiveInfo> getNotIsMySpecialFollowInfo();

    List<MyfavoriteLiveInfo> getAllFromTemp();
    /**
     * 清空临时表的数据
     * @return
     */
    void truncateTemp();

    /**
     * 设置为置顶 或者取消置顶
     * @param myfavoriteLiveInfo
     * @return
     */
    boolean updateForTheTopByIsTop(MyfavoriteLiveInfo myfavoriteLiveInfo);

    /**
     * 设置或者取消 超级置顶
     * @param myfavoriteLiveInfo
     * @return
     */
    boolean updateSuperSet_top(MyfavoriteLiveInfo myfavoriteLiveInfo);
}
