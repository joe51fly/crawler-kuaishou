package com.joe.kuaishou.service;

import com.joe.kuaishou.bean.KsLiveMyfavorite;

import java.util.List;

public interface KsLiveMyfavoriteService {
    /*
        根据id查询
     */
    KsLiveMyfavorite getKsLiveMyfavorite(Integer id);

    /*
    根据用户id查询
 */
    KsLiveMyfavorite getKsLiveMyfavoriteByAnchorId(String anchorId);

    /*
        查询所有数据
     */
    List<KsLiveMyfavorite> getAll();

    /*
        插入一条数据
     */
    boolean insertKsLiveMyfavorite(KsLiveMyfavorite ksLiveMyfavorite);

    /*
    根据id插入或者更新
    */
//    boolean insertOrUpdateKsLiveMyfavoriteById(KsLiveMyfavorite ksLiveMyfavorite);

    /*
        插入List<KsLiveMyfavorite>
     */
    int insertKsLiveMyfavoriteByList(List<KsLiveMyfavorite> ksLiveMyfavoriteList);

    /*
       根据id更新数据
    */
    boolean updateKsLiveMyfavoriteByid(KsLiveMyfavorite ksLiveMyfavorite);

    /*
    根据id删除数据
     */
    boolean deleteMyfavoriteByid(Integer id);
}
