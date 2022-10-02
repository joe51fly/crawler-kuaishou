package com.joe.kuaishou.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joe.kuaishou.bean.AnchorInfo;
import com.joe.kuaishou.common.Result;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author joe
 * @since 2022-06-20
 */
public interface AnchorInfoService extends IService<AnchorInfo> {
    /*
        根据id查询
     */
    AnchorInfo getMyfavorite(Integer id);

    /*
        根据用户id查询
     */
    AnchorInfo getMyfavoriteByAnchorId(String ksAnchorId);

    /*
        查询所有数据
     */
    List<AnchorInfo> getAll();

    /*
  通过isMyfavorite的状态查询我特别关注的主播
   */
    List<AnchorInfo> getMyfavoriteByIsMyfavorite(boolean isMyfavorite);

    /*
        插入一条数据
     */
    boolean insertMyfavorite(AnchorInfo anchorInfo);

    /*
        根据id插入或者更新
     */
//    boolean insertOrUpdateMyfavoriteById(AnchorInfo anchorInfo);

    /*
        批量插入List<Myfavorite>
        @param  我的关注总数
     */
    Integer insertMyfavoriteByList(Integer myFollowCount);

    /*
       根据anchorId更新数据
    */
    boolean updateMyfavoriteByAnchorId(AnchorInfo anchorInfo);

    /*
    更新主播状态为特别关注
    */
    boolean updateIsMyfavoriteByAnchorId(AnchorInfo anchorInfo);

    /*
        根据anchorId删除数据
     */
    boolean deleteMyfavoriteByid(String ksAnchorId);

    Result addOrUpdateAllMyfavorite(String inputKsAnchorId);


    Result addMyfavoriteList();

    Result updateIsMyfavorite(String inputKsAnchorId,boolean inputIsMyfavorite);

    /**
     * 关注或者取消关注主播 直接作用到快手账号
     * @param anchorEid 主播的eid
     * @param isFocusIn 关注 1  取消关注 2
     * @return
     */
    Result focusInOrNot(String anchorEid,int isFocusIn);
}
