package com.joe.kuaishou.mapper;

import com.joe.kuaishou.bean.Myfavorite;
import org.springframework.stereotype.Component;

import java.util.List;

@Component//指定这是一个操作数据库的mapper
public interface MyfavoriteMapper {
    /*
        根据id查询
     */
    Myfavorite getMyfavorite(Integer id);
    /*
        根据用户id查询
     */
    Myfavorite getMyfavoriteByAnchorId(String ksAnchorId);

    /*
        查询所有数据
     */
    List<Myfavorite> getAll();

    /*
    通过isMyfavorite的状态查询我特别关注的主播
     */
    List<Myfavorite> getMyfavoriteByIsMyfavorite(boolean isMyfavorite);

    /*
        插入一条数据
     */
    boolean insertMyfavorite(Myfavorite myfavorite);

    /*
        根据id插入或者更新
     */
//    boolean insertOrUpdateMyfavoriteById(Myfavorite Myfavorite);

    /*
        批量插入List<Myfavorite>
     */
    int insertMyfavoriteByList(List<Myfavorite> myfavoriteList);

    /*
       根据anchorId更新数据
    */
    boolean updateMyfavoriteByAnchorId(Myfavorite myfavorite);

    /*
    更新主播状态为特别关注
     */
    boolean updateIsMyfavoriteByAnchorId(Myfavorite myfavorite);

    /*
        根据anchorId删除数据
     */
    boolean deleteMyfavoriteByid(String ksAnchorId);
}
