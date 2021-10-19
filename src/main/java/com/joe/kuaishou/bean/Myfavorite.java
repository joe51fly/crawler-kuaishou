package com.joe.kuaishou.bean;

import java.io.Serializable;
import java.util.Date;

public class Myfavorite implements Serializable {
    private Integer id;
    private String ksAnchorName;
    private String ksAnchorId;
    private String ksAnchorHeaderUrl;
    private String description;
    private boolean isMyfavorite;
    private Date createTime;
    private Date updateTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isMyfavorite() {
        return isMyfavorite;
    }

    public void setMyfavorite(boolean myfavorite) {
        isMyfavorite = myfavorite;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKsAnchorName() {
        return ksAnchorName;
    }

    public void setKsAnchorName(String ksAnchorName) {
        this.ksAnchorName = ksAnchorName;
    }

    public String getKsAnchorId() {
        return ksAnchorId;
    }

    public void setKsAnchorId(String ksAnchorId) {
        this.ksAnchorId = ksAnchorId;
    }

    public String getKsAnchorHeaderUrl() {
        return ksAnchorHeaderUrl;
    }

    public void setKsAnchorHeaderUrl(String ksAnchorHeaderUrl) {
        this.ksAnchorHeaderUrl = ksAnchorHeaderUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "KsLiveMyfavorite{" +
                "id=" + id +
                ", ksAnchorName='" + ksAnchorName + '\'' +
                ", ksAnchorId='" + ksAnchorId + '\'' +
                ", ksAnchorHeaderUrl='" + ksAnchorHeaderUrl + '\'' +
                ", description='" + description + '\'' +
                ", isMyfavorite=" + isMyfavorite +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
