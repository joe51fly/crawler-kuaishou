package com.joe.kuaishou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.joe.kuaishou.bean.AnchorInfo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author joe
 * @since 2022-06-20
 */
@Component
public interface AnchorInfoMapper extends BaseMapper<AnchorInfo> {
    /*
        根据id查询
     */
    AnchorInfo getAnchorInfo(Integer id);
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
     */
    int insertMyfavoriteByList(List<AnchorInfo> AnchorInfoList);

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
}
