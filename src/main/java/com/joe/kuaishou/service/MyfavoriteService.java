package com.joe.kuaishou.service;

import com.joe.kuaishou.bean.Myfavorite;
import com.joe.kuaishou.common.Result;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface MyfavoriteService {
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
        @param  我的关注总数
     */
    Integer insertMyfavoriteByList(Integer myFollowCount);

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

    Result addOrUpdateAllMyfavorite(String inputKsAnchorId);


    Result addMyfavoriteList();

    Result updateIsMyfavorite(String inputKsAnchorId,boolean inputIsMyfavorite);
}
