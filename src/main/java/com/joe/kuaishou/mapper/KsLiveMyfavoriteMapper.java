package com.joe.kuaishou.mapper;

import com.joe.kuaishou.bean.KsLiveMyfavorite;
import org.springframework.stereotype.Component;

import java.util.List;

@Component//指定这是一个操作数据库的mapper
public interface KsLiveMyfavoriteMapper {
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
