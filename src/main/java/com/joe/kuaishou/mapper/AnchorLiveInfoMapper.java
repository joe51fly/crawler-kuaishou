package com.joe.kuaishou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.joe.kuaishou.bean.AnchorLiveInfo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 我关注的主播正在直播的信息 Mapper 接口
 * </p>
 *
 * @author joe
 * @since 2022-06-20
 */
@Component//指定这是一个操作数据库的mapper //自动生成的代码没有加这个注解
public interface AnchorLiveInfoMapper extends BaseMapper<AnchorLiveInfo> {
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
    int insertMyfavoriteLiveInfoByList(List<AnchorLiveInfo> anchorLiveInfoList);

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


    boolean createMyfavoriteLiveInfoTempTable();

    int insertMyfavoriteLiveInfoByListForTemp(List<AnchorLiveInfo> anchorLiveInfo);
    /**
     * 获取isMyfavorite为true的主播列表信息
     * @return
     */
    List<AnchorLiveInfo> getMySpecialFollowInfo();

    /**
     * 获取isMyfavorite为false的主播列表信息
     * @return
     */
    List<AnchorLiveInfo> getNotIsMySpecialFollowInfo();

    List<AnchorLiveInfo> getAllFromTemp();
    /**
     * 清空临时表的数据
     * @return
     */
    void truncateTemp();

    /**
     * 设置为置顶 或者取消置顶
     * @param anchorLiveInfo
     * @return
     */
    boolean updateForTheTopByIsTop(AnchorLiveInfo anchorLiveInfo);

    /**
     * 设置或者取消 超级置顶
     * @param anchorLiveInfo
     * @return
     */
    boolean updateSuperSet_top(AnchorLiveInfo anchorLiveInfo);
}
