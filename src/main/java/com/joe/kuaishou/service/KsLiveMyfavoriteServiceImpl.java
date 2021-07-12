package com.joe.kuaishou.service;

import com.joe.kuaishou.bean.KsLiveMyfavorite;
import com.joe.kuaishou.mapper.KsLiveMyfavoriteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class KsLiveMyfavoriteServiceImpl implements KsLiveMyfavoriteService {
    @Autowired
    KsLiveMyfavoriteMapper ksLiveMyfavoriteMapper;
    @Override
    public KsLiveMyfavorite getKsLiveMyfavorite(Integer id) {
        return ksLiveMyfavoriteMapper.getKsLiveMyfavorite(id);
    }

    @Override
    public KsLiveMyfavorite getKsLiveMyfavoriteByAnchorId(String anchorId) {
        return ksLiveMyfavoriteMapper.getKsLiveMyfavoriteByAnchorId(anchorId);
    }

    @Override
    public List<KsLiveMyfavorite> getAll() {
        return ksLiveMyfavoriteMapper.getAll();
    }

    @Override
    public boolean insertKsLiveMyfavorite(KsLiveMyfavorite ksLiveMyfavorite) {
        return ksLiveMyfavoriteMapper.insertKsLiveMyfavorite(ksLiveMyfavorite);
    }

//    @Override
////    public boolean insertOrUpdateKsLiveMyfavoriteById(KsLiveMyfavorite ksLiveMyfavorite) {
////        return ksLiveMyfavoriteMapper.insertOrUpdateKsLiveMyfavoriteById(ksLiveMyfavorite);
////    }

    @Override
    public int insertKsLiveMyfavoriteByList(List<KsLiveMyfavorite> ksLiveMyfavoriteList) {
        return ksLiveMyfavoriteMapper.insertKsLiveMyfavoriteByList(ksLiveMyfavoriteList);
    }

    @Override
    public boolean updateKsLiveMyfavoriteByid(KsLiveMyfavorite ksLiveMyfavorite) {
        return ksLiveMyfavoriteMapper.updateKsLiveMyfavoriteByid(ksLiveMyfavorite);
    }

    @Override
    public boolean deleteMyfavoriteByid(Integer id) {
        return ksLiveMyfavoriteMapper.deleteMyfavoriteByid(id);
    }
}
